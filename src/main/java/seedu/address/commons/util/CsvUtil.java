package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;

/**
 * Converts a Java object to a CSV file and vice versa.
 */
public class CsvUtil {

    private static final Logger logger = LogsCenter.getLogger(CsvUtil.class);

    /**
     * Reads a CSV file and returns a list of objects of the specified class.
     *
     * @param filePath the path to the CSV file
     * @param clazz the class of the objects to be read from the CSV file
     * @param <T> the type of the objects to be read from the CSV file
     * @return an Optional containing a list of objects read from the CSV file,
     *      or Optional.empty() if the file does not exist
     * @throws DataLoadingException if there is an error reading the CSV file
     */
    public static <T> Optional<List<T>> readCsvFile(Path filePath, Class<T> clazz) throws DataLoadingException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        logger.info("CSV file " + filePath + " found.");

        try (FileReader reader = new FileReader(filePath.toFile())) {
            List<T> beans = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .build()
                    .parse();
            return Optional.of(beans);
        } catch (IOException e) {
            logger.warning("Error reading from CSV file " + filePath + ": " + e);
            throw new DataLoadingException(e);
        }
    }

    /**
     * Writes a list of objects to a CSV file.
     *
     * @param filePath the path to the CSV file
     * @param beans the list of objects to be written to the CSV file
     * @param <T> the type of the objects to be written to the CSV file
     * @throws IOException if there is an error writing to the CSV file
     */
    public static <T> void writeCsvFile(Path filePath, List<T> beans) throws IOException {
        requireNonNull(filePath);
        requireNonNull(beans);

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
            beanToCsv.write(beans);
        } catch (CsvException e) {
            logger.warning("Error writing to CSV file " + filePath + ": " + e);
            throw new IOException(e);
        }
    }
}
