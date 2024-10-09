package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.person.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests Csv Read and Write
 */
public class CsvUtilTest {

    @TempDir
    public Path testFolder;

    private Path filePath;

    @BeforeEach
    public void setUp() {
        filePath = testFolder.resolve("test.csv");
    }

    @Test
    public void readCsvFile_noFile_emptyResult() throws DataLoadingException {
        Optional<List<Person>> result = CsvUtil.readCsvFile(filePath, Person.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void readCsvFile_existingFile_correctData() throws DataLoadingException, IOException {
        String csvContent = "name,phone,email,address,tags\n"
                + "John Doe,12345678,johndoe@example.com,123 Main St,friend\n";
        Files.write(filePath, csvContent.getBytes());

        Optional<List<Person>> result = CsvUtil.readCsvFile(filePath, Person.class);
        assertTrue(result.isPresent());
        List<Person> people = result.get();
        assertEquals(1, people.size());
        Person person = people.get(0);
        assertEquals("John Doe", person.getName().fullName);
        assertEquals("12345678", person.getPhone().value);
        assertEquals("johndoe@example.com", person.getEmail().value);
        assertEquals("123 Main St", person.getAddress().value);
    }

    @Test
    public void readCsvFile_invalidFile_throwException() throws IOException {
        Files.write(filePath, "invalid content".getBytes());

        assertThrows(DataLoadingException.class, () -> CsvUtil.readCsvFile(filePath, Person.class));
    }

    @Test
    public void readCsvFile_missingData_exceptionThrown() throws IOException {
        String csvContent = "name,phone,email,address\n"
                + "John Doe,12345678,,123 Main St\n";

        Files.write(filePath, csvContent.getBytes());

        assertThrows(DataLoadingException.class, () -> CsvUtil.readCsvFile(filePath, Person.class));
    }


    @Test
    public void readCsvFile_missingField_exceptionThrown() throws IOException {
        String csvContent = "name,phone\n"
                + "John Doe,12345678\n";

        Files.write(filePath, csvContent.getBytes());

        assertThrows(DataLoadingException.class, () -> CsvUtil.readCsvFile(filePath, Person.class));
    }
}
