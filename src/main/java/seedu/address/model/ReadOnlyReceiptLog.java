package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.goodsReceipt.GoodsReceipt;

/**
 * Unmodifiable view of a Receipt list
 */
public interface ReadOnlyReceiptLog {

    ObservableList<GoodsReceipt> getReceiptList();
}
