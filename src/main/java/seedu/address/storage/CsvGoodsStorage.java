package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.CsvUtil;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyReceiptLog;
import seedu.address.model.ReceiptLog;
import seedu.address.model.goodsReceipt.GoodsReceipt;

/**
 * A class to access Goods data stored as a csv file on the hard disk.
 */
public class CsvGoodsStorage implements GoodsStorage {

    private static final Logger logger = LogsCenter.getLogger(CsvGoodsStorage.class);

    private Path filePath;

    public CsvGoodsStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getGoodsFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyReceiptLog> readGoods() throws DataLoadingException {
        return readGoods(filePath);
    }

    @Override
    public Optional<ReadOnlyReceiptLog> readGoods(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        ReceiptLog r = new ReceiptLog();
        r.setReceipts(CsvUtil.readCsvFile(filePath, GoodsReceipt.class).get());
        return Optional.of(r);
    }

    @Override
    public void saveGoods(ReadOnlyReceiptLog goods) throws IOException {
        saveGoods(goods, filePath);
    }

    @Override
    public void saveGoods(ReadOnlyReceiptLog goods, Path filePath) throws IOException {
        requireNonNull(goods);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        CsvUtil.writeCsvFile(filePath, goods.getReceiptList());
    }
}
