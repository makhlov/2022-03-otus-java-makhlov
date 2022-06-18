package ru.otus.atm.mechanism.cluster;

import ru.otus.atm.mechanism.cell.exception.CellOperationException;
import ru.otus.atm.mechanism.cluster.exception.ClusterOperationException;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;
import ru.otus.atm.mechanism.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClusterManagerImpl implements ClusterManager<Banknote> {
    private final Currency clusterCurrency;
    private final Map<Integer, Cell<Banknote>> cellHolder;

    public ClusterManagerImpl(Currency clusterCurrency, Map<Integer, Cell<Banknote>> cellHolder) {
        this.clusterCurrency = clusterCurrency;
        this.cellHolder = cellHolder;
    }

    @Override
    public void putBanknote(List<Banknote> banknotes) throws ClusterOperationException {
        for (var banknote : banknotes) {
            Cell<Banknote> cell = cellHolder.get(banknote.denomination());
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
    public List<Banknote> requestBanknote(Integer nominal, int amount) throws ClusterOperationException {
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
    public int getBanknoteAmountFromCell(Integer denomination) {
        return cellHolder.get(denomination).getBanknotesAmount();
    }

    @Override
    public Integer getTotalSumFromCluster() {
        int result = 0;
        List<Cell<Banknote>> clustersCells = new ArrayList<>(cellHolder.values());
        for (var cell : clustersCells) {
            result += cell.getBanknotesAmount() * cell.getDenomination();
        }
        return result;
    }

    @Override
    public Currency getClusterCurrency() {
        return clusterCurrency;
    }
}
