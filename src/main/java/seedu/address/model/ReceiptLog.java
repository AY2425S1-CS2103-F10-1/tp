package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import javafx.collections.ObservableList;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.goodsreceipt.UniqueGoodsReceiptList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameReceipt comparison)
 */
public class ReceiptLog implements ReadOnlyReceiptLog {

    private final UniqueGoodsReceiptList receipts = new UniqueGoodsReceiptList();

    public ReceiptLog() {}

    /**
     * Creates a receipt log using the receipts in the {@code toBeCopied}
     */
    public ReceiptLog(ReadOnlyReceiptLog toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Resets data
     */
    public void resetData(ReadOnlyReceiptLog toBeCopied) {
        requireNonNull(toBeCopied);
        setReceipts(toBeCopied.getReceiptList());
    }

    /**
     * Checks if receipt is in the receipt log.
     */
    public boolean hasReceipt(GoodsReceipt receipt) {
        requireNonNull(receipt);
        return receipts.contains(receipt);
    }

    @Override
    public ObservableList<GoodsReceipt> getReceiptList() {
        return receipts.asUnmodifiableObservableList();
    }

    /**
     * Adds a receipt to the receipt log.
     * The receipt must not already exist in the receipt log
     */
    public void addReceipt(GoodsReceipt add) {
        receipts.add(add);
    }


    /**
     * Deletes a receipt in the receipt log.
     */
    public void deleteReceipt(GoodsReceipt remove) {
        receipts.remove(remove);
    }

    /**
     * sets receipts
     */
    public void setReceipts(List<GoodsReceipt> receipts) {
        this.receipts.setReceipts(receipts);
    }

    /**
     * Finds a receipt
     */
    public List<GoodsReceipt> findReceipts(Predicate<GoodsReceipt> predicate) {
        return StreamSupport
                .stream(receipts.spliterator(), false)
                .filter(predicate).toList();
    }

    public boolean removeIf(Predicate<? super GoodsReceipt> predicate) {
        return receipts.removeIf(predicate);
    }

    public int size() {
        return receipts.size();
    }
}
