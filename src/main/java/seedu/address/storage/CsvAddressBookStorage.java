package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.CsvUtil;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class to access AddressBook data stored as a csv file on the hard disk.
 */
public class CsvAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(CsvAddressBookStorage.class);

    private Path filePath;

    /**
     * Constructs a {@code CsvAddressBookStorage} with the given file path.
     *
     * @param filePath The file path of the address book data file.
     */
    public CsvAddressBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<List<Person>> people = CsvUtil.readCsvFile(filePath, Person.class);
        if (!people.isPresent()) {
            return Optional.empty();
        }

        AddressBook addressBook = new AddressBook();
        for (Person person : people.get()) {
            addressBook.addPerson(person);
        }

        return Optional.of(addressBook);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        CsvUtil.writeCsvFile(filePath, addressBook.getPersonList());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);
        FileUtil.createIfMissing(filePath);
        CsvUtil.writeCsvFile(filePath, addressBook.getPersonList());
    }
}
