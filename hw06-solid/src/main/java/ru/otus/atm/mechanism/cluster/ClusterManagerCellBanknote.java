package ru.otus.atm.mechanism.cluster;

import ru.otus.atm.mechanism.cell.exception.CellOperationException;
import ru.otus.atm.mechanism.cluster.exception.ClusterOperationException;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;
import ru.otus.atm.mechanism.cell.Cell;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClusterManagerCellBanknote implements ClusterManager<Banknote> {
    private final Currency clusterCurrency;
    private final Map<BigInteger, Cell<Banknote>> cellHolder;

    public ClusterManagerCellBanknote(Currency clusterCurrency, Map<BigInteger, Cell<Banknote>> cellHolder) {
        this.clusterCurrency = clusterCurrency;
        this.cellHolder = cellHolder;
    }

    @Override
    public void putBanknote(List<Banknote> banknotes) throws ClusterOperationException {
        for (var banknote : banknotes) {
            Cell<Banknote> cell = cellHolder.get(banknote.getDenomination());
            try {
                cell.insertBanknote(banknote);
            } catch (CellOperationException exception) {
                throw new ClusterOperationException(
                        "An error occurred while trying to insert into a cell: " + exception.getMessage(),
                        exception
                );
            }
        }
    }

    @Override
    public List<Banknote> requestBanknote(BigInteger nominal, int amount) throws ClusterOperationException {
        List<Banknote> result = new ArrayList<>();
        Cell<Banknote> banknoteCell = cellHolder.get(nominal);
        try {
            for (int i = 0; i < amount; i++) {
                result.add(banknoteCell.extract());
            }
        } catch (CellOperationException exception) {
            throw new ClusterOperationException(
                "An error occurred while trying to extract from a cell" + exception.getMessage(),
                 exception
            );
        }
        return result;
    }

    @Override
    public int getBanknoteAmountFromCell(BigInteger denomination) {
        return cellHolder.get(denomination).getBanknotesAmount();
    }

    @Override
    public BigInteger getTotalSumFromCluster() {
        BigInteger result = new BigInteger("0");
        List<Cell<Banknote>> clustersCells = new ArrayList<>(cellHolder.values());
        for (var cell : clustersCells) {
            result = result
                        .add(new BigInteger(String.valueOf(cell.getBanknotesAmount()))
                        .multiply(cell.getDenomination()));
        }
        return result;
    }

    @Override
    public Currency getClusterCurrency() {
        return clusterCurrency;
    }
}
