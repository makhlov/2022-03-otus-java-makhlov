import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.interaction.Atm;
import ru.otus.atm.interaction.exception.AtmInteractionException;
import ru.otus.atm.interaction.factory.AtmCreator;
import ru.otus.atm.interaction.factory.AtmCreatorDefault;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.atm.money.Currency.EUR;
import static ru.otus.atm.money.Currency.RUB;

class ApplicationTest {
    private Atm atm;

    @BeforeEach
    void setUp(){
        List<Currency> currencies = new ArrayList<>() {{
            add(RUB);
            add(EUR);
        }};

        AtmCreator atmCreator = new AtmCreatorDefault(currencies);
        atm = atmCreator.create();
    }

    @Test
    void insertBanknotes() {
        atm.insertMoney(List.of(
                new Banknote(1000, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB)
        ));
        assertEquals(2200, atm.getBalanceForCurrency(RUB));
    }

    @Test
    void requestMoney() throws AtmInteractionException {
        atm.insertMoney(List.of(
                new Banknote(1000, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB)
        ));

        var expected = List.of(
                new Banknote(100, RUB),
                new Banknote(500, RUB)
        );

        List<Banknote> banknotes = atm.requestMoney(RUB, "600");
        assertTrue(banknotes.containsAll(expected));
    }
}