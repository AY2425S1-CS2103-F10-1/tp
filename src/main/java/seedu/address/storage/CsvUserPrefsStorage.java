package seedu.address.storage;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.CsvUtil;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Represents a storage for {@link seedu.address.model.UserPrefs}.
 */
public class CsvUserPrefsStorage implements UserPrefsStorage {

    private Path filePath;

    /**
     * Constructs a {@code CsvUserPrefsStorage} with the given file path.
     *
     * @param filePath The file path of the user prefs data file.
     */
    public CsvUserPrefsStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getUserPrefsFilePath() {
        return filePath;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        Optional<List<UserPrefs>> optionalList = CsvUtil.readCsvFile(filePath, UserPrefs.class);
        return optionalList.flatMap(list -> list.stream().findFirst());
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        CsvUtil.writeCsvFile(filePath, singletonList(userPrefs));
    }
}
