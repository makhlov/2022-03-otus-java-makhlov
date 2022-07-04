package ru.otus.mainops;

import org.hibernate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.HibernateUtils;
import ru.otus.mainops.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static ru.otus.core.HibernateUtils.*;

class MainOperationsTest {

    private Avatar avatar;
    private OtusStudent student;

    private SessionFactory sf;

    @BeforeEach
    void setUp() {
        avatar = new Avatar(0, "http://any-addr.ru/");
        student = new OtusStudent(0, "AnyName", avatar);

        sf = buildSessionFactory(OtusStudent.class, OtusTeacher.class, Avatar.class);
        sf.getStatistics().setStatisticsEnabled(true);
    }

    @DisplayName("persist не вставляет сущность в БД без транзакции")
    @Test
    public void shouldNeverPersistEntityToDBWhenTransactionDoesNotExists() {
        /* Студент попал в context, но не попал в БД.
         * Почему? Потому что любые изменения в БД должны выполнятся в транзакции, а она тут не создается
         * Сессия закрылась, контекст закрылся, в базу ничего не пролезло
         */
        doInSession(sf, session -> session.persist(student));

        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(0);
    }

    //А вот тут уже пролезет, т.к. есть транзакция
    @DisplayName("persist вставляет сущность и ее связь в БД при наличии транзакции")
    @Test
    public void shouldNeverEntityWithRelationToDBWhenTransactionExists() {
        doInSessionWithTransaction(sf, session -> session.persist(student));

        /*
            Также мы ожидаем, что будет два запроса:
            1. Сохранение студента
            2. Сохранение аватара
         */
        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @DisplayName("выкидывает исключение если вставляемая сущность в состоянии detached")
    @Test
    public void shouldThrowExceptionWhenPersistDetachedEntity() {
        //Такого аватара в БД нет и мы дали ему id, а id должно генерится на основе чего-то
        var avatar = new Avatar(1L, "http://any-addr.ru/");

        assertThatCode(() ->
                doInSessionWithTransaction(sf, session -> session.persist(avatar))
        ).hasCauseInstanceOf(PersistentObjectException.class);
    }


    @DisplayName("persist выкидывает исключение если вставляемая сущность " +
            "содержит дочернюю в состоянии transient при выключенной каскадной операции PERSIST")
    @Test
    public void shouldThrowExceptionWhenPersistEntityWithChildInTransientStateAndDisabledCascadeOperation() {
        //У Teacher отключены каскадные операции для аватара
        //Тут вылетает TransientObjectException: аватар не сохранен в БД
        // Пробуем сохранить teacher, у него внутри ссылка на avatar, значит надо прописать id аватара
        //Но аватар еще не сохранен в БД, поэтому мы не можем получить id
        //Можно было бы его сохранить при каскадных операциях, но они отключены
        var teacher = new OtusTeacher(0, "AnyName", avatar);
        assertThatCode(() ->
                doInSessionWithTransaction(sf, session -> session.persist(teacher))
        ).hasCauseInstanceOf(TransientObjectException.class);
    }


    @DisplayName("изменения в сущности под управлением контекста попадают в БД " +
            "при закрытии сессии")
    @Test
    public void shouldSaveEntityChangesToDBAfterSessionClosing() {
        var newName = "NameAny";

        doInSessionWithTransaction(sf, session -> {
            session.persist(student); //кладем в контекст

            // Как сломать: отключаем dirty checking (одно из двух)
            // session.setHibernateFlushMode(FlushMode.MANUAL);
            // session.detach(student);

            student.setName(newName); //Просто меняем name
        });

        //Проверяем, что произошел как минимум 1 upd
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(1);

        //Проверяем, что сменилось имя, не смотря на то, что мы ничего не фиксировали и не говорили
        //что надо что-то update'ить
        doInSessionWithTransaction(sf, session -> {
            var actualStudent = session.find(OtusStudent.class, student.getId());
            assertThat(actualStudent.getName()).isEqualTo(newName);
        });
    }


    @DisplayName("merge при сохранении transient сущности работает как persist," +
            "а при сохранении detached делает дополнительный запрос в БД")
    @Test
    public void shouldWorkAsPersistWhenSaveTransientEntityAndDoAdditionalSelectWhenSaveDetachedEntity() {
        doInSessionWithTransaction(sf, session -> session.merge(avatar));

        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(0);
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(0);

        avatar.setId(1L);
        avatar.setPhotoUrl("http://any-addr2.ru/");
        sf.getStatistics().clear();

        doInSessionWithTransaction(sf, session -> session.merge(avatar));

        assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(1);
        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(0);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("при доступе к LAZY полю за пределами сессии выкидывается исключение")
    @Test
    public void shouldThrowExceptionWhenAccessingToLazyField() {
        doInSessionWithTransaction(sf, session -> session.persist(student));

        OtusStudent actualStudent;
        try (var session = sf.openSession()) {
            actualStudent = session.find(OtusStudent.class, 1L);
        }
        assertThatCode(() -> actualStudent.getAvatar().getPhotoUrl())
                .isInstanceOf(LazyInitializationException.class);
    }

    @DisplayName("find загружает сущность со связями")
    @Test
    public void shouldFindEntityWithChildField() {
        doInSessionWithTransaction(sf, session -> session.persist(student));

        try (var session = sf.openSession()) {
            var actualStudent = session.find(OtusStudent.class, 1L);
            assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(1);

            sf.getStatistics().clear();

            assertThat(actualStudent.getAvatar()).isNotNull().isEqualTo(avatar);
            assertThat(sf.getStatistics().getEntityLoadCount()).isEqualTo(1);
        }
    }
}