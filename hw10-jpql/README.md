<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="main-content">
<h1>
    ⬛ Project: hw10-jpql
</h1>
    <div class="task">
        <b>Status:</b> ✅ Resolved
        <br><b>Name:</b> «Использование Hibernate»
        <br><b>Goal:</b> На практике освоить основы Hibernate. Понять как аннотации-hibernate влияют на формирование sql-запросов.
        <h2>Steps:</h2>
        <ol>
            <li>
                Работа должна использовать базу данных в docker-контейнере. За основу возьмите пример из вебинара про 
                JPQL (class DbServiceDemo).
            </li>
            <li>
                Добавьте в Client поля:
                <ul>
                    <br>адрес (OneToOne)
                        <br>class Address {
                        <br>&nbsp&nbsp&nbsp&nbsp&nbsp private String street;
                    <br>}
                </ul>
                <ul>
                    <br>телефон (OneToMany)
                        <br>class Phone {
                        <br>&nbsp&nbsp&nbsp&nbsp&nbsp private String number;
                    <br>}
                </ul>
                <br>
            </li>
            <li>
                Разметьте классы таким образом, чтобы при сохранении/чтении объека Client каскадно сохранялись/читались
                вложенные объекты. ВАЖНО.
            </li>
            <li>
                Hibernate должен создать только три таблицы: для телефонов, адресов и клиентов.
                При сохранении нового объекта не должно быть update-ов. Посмотрите в логи и проверьте, 
                что эти два требования выполняются.
            </li>
        </ol>
    </div>
</div>
</body>
</html>