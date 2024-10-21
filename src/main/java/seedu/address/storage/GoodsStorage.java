package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyReceiptLog;
import seedu.address.model.goodsReceipt.GoodsReceipt;


/**
 * Represents a storage for {@link seedu.address.model.goods.Goods}.
 */
public interface GoodsStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getGoodsFilePath();

    /**
     * Returns Goods data as a {@link GoodsReceipt}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyReceiptLog> readGoods() throws DataLoadingException;

    /**
     * @see #getGoodsFilePath()
     */
    Optional<ReadOnlyReceiptLog> readGoods(Path filePath) throws DataLoadingException;

    /**
     * TODO: Add ReadOnly Feature to the parameter
     * Saves the given {@link ReadOnlyAddressBook} to the storage.
     * @param goods cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveGoods(ReadOnlyReceiptLog goods) throws IOException;

    /**
     * TODO: Add ReadOnly Feature to the parameter
     * @see #saveGoods(List)
     */
    void saveGoods(ReadOnlyReceiptLog goods, Path filePath) throws IOException;
}
