import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.interaction.Atm;
import ru.otus.atm.interaction.exception.AtmInteractionException;
import ru.otus.atm.interaction.factory.AtmCreator;
import ru.otus.atm.interaction.factory.AtmCreatorDefault;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;
import ru.otus.atm.money.factory.BanknoteCreator;
import ru.otus.atm.money.factory.BanknotePaperCreator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.atm.money.Currency.EUR;
import static ru.otus.atm.money.Currency.RUB;

class ApplicationTest {
    private BanknoteCreator bc;
    private Atm atm;

    @BeforeEach
    void setUp(){
        List<Currency> currencies = new ArrayList<>() {{
            add(RUB);
            add(EUR);
        }};

        bc = new BanknotePaperCreator();
        AtmCreator atmCreator = new AtmCreatorDefault(currencies);
        atm = atmCreator.create();
    }

    @Test
    void insertBanknotes() {
        atm.insertMoney(List.of(
                bc.create(RUB, "1000"),
                bc.create(RUB, "500"),
                bc.create(RUB, "500"),
                bc.create(RUB, "500"),
                bc.create(RUB, "100"),
                bc.create(RUB, "100")
        ));
        assertEquals(0, atm.getBalanceForCurrency(RUB).compareTo(new BigInteger("2700")));
    }

    @Test
    void requestMoney() throws AtmInteractionException {
        atm.insertMoney(List.of(
                bc.create(RUB, "1000"),
                bc.create(RUB, "500"),
                bc.create(RUB, "500"),
                bc.create(RUB, "500"),
                bc.create(RUB, "100"),
                bc.create(RUB, "100"),
                bc.create(RUB, "100"),
                bc.create(RUB, "100")
        ));

        var expected = List.of(
                bc.create(RUB, "100"),
                bc.create(RUB, "500")
        );

        List<Banknote> banknotes = atm.requestMoney(RUB, "600");
        assertTrue(banknotes.containsAll(expected));
    }
}