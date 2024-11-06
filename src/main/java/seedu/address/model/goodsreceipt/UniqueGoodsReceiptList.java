package seedu.address.model.goodsreceipt;


import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.goodsreceipt.exceptions.DuplicateReceiptException;
import seedu.address.model.goodsreceipt.exceptions.ReceiptNotFoundException;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Unique goodsReceipt list
 */
public class UniqueGoodsReceiptList implements Iterable<GoodsReceipt> {

    /**
     * List that allows real time updates in the UI
     */
    private final ObservableList<GoodsReceipt> internalList = FXCollections.observableArrayList();
    private final ObservableList<GoodsReceipt> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent receipt as the given argument.
     */
    public boolean contains(GoodsReceipt toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(x -> x.isSameReceipt(toCheck));
    }

    /**
     * Adds a goodsReceipt to the list.
     * The goodsReceipt must not already exist in the list.
     */
    public void add(GoodsReceipt toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateReceiptException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the receipt {@code target} in the list with {@code editedReceipt}.
     * {@code target} must exist in the list.
     * The receipt identity of {@code editedReceipt} must not be the same as another existing receipt in the list.
     */
    public void setReceipt(GoodsReceipt target, GoodsReceipt editedReceipt) {
        requireAllNonNull(target, editedReceipt);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ReceiptNotFoundException();
        }

        if (!target.isSameReceipt(editedReceipt) && contains(editedReceipt)) {
            throw new DuplicateReceiptException();
        }

        internalList.set(index, editedReceipt);
    }

    /**
     * Removes the equivalent receipt from the list.
     * The receipt must exist in the list.
     */
    public void remove(GoodsReceipt toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ReceiptNotFoundException();
        }
    }

    public void setReceipts(UniqueGoodsReceiptList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }


    /**
     * Replaces the contents of this list with {@code receipts}.
     * {@code receipt} must not contain duplicate receipts.
     */
    public void setReceipts(List<GoodsReceipt> receipts) {
        requireAllNonNull(receipts);
        if (!receiptsAreUnique(receipts)) {
            throw new DuplicatePersonException();
        }
        internalList.setAll(receipts);
    }


    /**
     * Replaces the contents of this list with {@code receipts}.
     * {@code receipts} must not contain duplicate receipts.
     */
    public void setGoodsReceipts(List<GoodsReceipt> goodsReceipts) {
        requireAllNonNull(goodsReceipts);
        if (!receiptsAreUnique(goodsReceipts)) {
            throw new DuplicateReceiptException();
        }

        internalList.setAll(goodsReceipts);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<GoodsReceipt> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<GoodsReceipt> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueGoodsReceiptList otherUniqueReceiptList)) {
            return false;
        }

        return internalList.equals(otherUniqueReceiptList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code receipts} contains only unique receipts.
     */
    private boolean receiptsAreUnique(List<GoodsReceipt> receipts) {
        for (int i = 0; i < receipts.size() - 1; i++) {
            for (int j = i + 1; j < receipts.size(); j++) {
                if (receipts.get(i).isSameReceipt(receipts.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean removeIf(Predicate<? super GoodsReceipt> predicate) {
        return internalList.removeIf(predicate);
    }

    public int size() {
        return internalList.size();
    }
}
