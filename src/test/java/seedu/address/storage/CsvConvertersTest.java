package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Tests Csv Converters for OpenCSV.
 */
public class CsvConvertersTest {

    @Test
    public void pathConverter_convertPath_success() {
        CsvConverters.PathConverter pathConverter = new CsvConverters.PathConverter();
        Path path = Path.of("src/test/resources/data/sampleData.csv");
        assertEquals(path, pathConverter.convert("src/test/resources/data/sampleData.csv"));
    }

    @Test
    public void guiSettingsConverter_convertGuiSettings_success() {
        CsvConverters.GuiSettingsConverter guiSettingsConverter = new CsvConverters.GuiSettingsConverter();
        GuiSettings guiSettings = new GuiSettings(1000, 500, 300, 200);
        assertEquals(guiSettings, guiSettingsConverter.convert("1000.0,500.0,300,200"));
    }

    @Test
    public void guiSettingsConverter_convertInvalidGuiSettings_throwsIllegalArgumentException() {
        CsvConverters.GuiSettingsConverter guiSettingsConverter = new CsvConverters.GuiSettingsConverter();
        assertThrows(IllegalArgumentException.class, () -> guiSettingsConverter.convert("1000.0,500.0,300"));
        assertThrows(IllegalArgumentException.class, () -> guiSettingsConverter.convert("Default Name"));

    }

    @Test
    public void guiSettingsConverter_convertToWrite_success() {
        CsvConverters.GuiSettingsConverter guiSettingsConverter = new CsvConverters.GuiSettingsConverter();
        GuiSettings guiSettings = new GuiSettings(1000, 500, 300, 200);
        assertEquals("1000.0,500.0,300,200", guiSettingsConverter.convertToWrite(guiSettings));
    }

    @Test
    public void nameConverter_convertName_success() {
        CsvConverters.NameConverter nameConverter = new CsvConverters.NameConverter();
        Name name = new Name("John Doe");
        assertEquals(name, nameConverter.convert("John Doe"));
    }

    @Test
    public void nameConverter_convertInvalidName_throwsIllegalArgumentException() {
        CsvConverters.NameConverter nameConverter = new CsvConverters.NameConverter();
        assertThrows(IllegalArgumentException.class, () -> nameConverter.convert("John,Doe"));
        assertThrows(IllegalArgumentException.class, () -> nameConverter.convert("John Doe,"));
    }

    @Test
    public void phoneConverter_convertPhone_success() {
        CsvConverters.PhoneConverter phoneConverter = new CsvConverters.PhoneConverter();
        Phone phone = new Phone("12345678");
        assertEquals(phone, phoneConverter.convert("12345678"));
    }

    @Test
    public void phoneConverter_convertInvalidPhone_throwsIllegalArgumentException() {
        CsvConverters.PhoneConverter phoneConverter = new CsvConverters.PhoneConverter();
        assertThrows(IllegalArgumentException.class, () -> phoneConverter.convert("17"));
        assertThrows(IllegalArgumentException.class, () -> phoneConverter.convert("aaa123456789"));
    }

    @Test
    public void emailConverter_convertEmail_success() {
        CsvConverters.EmailConverter emailConverter = new CsvConverters.EmailConverter();
        Email email = new Email("default@example.com");
        assertEquals(email, emailConverter.convert("default@example.com"));
    }

    @Test
    public void emailConverter_convertInvalidEmail_throwsIllegalArgumentException() {
        CsvConverters.EmailConverter emailConverter = new CsvConverters.EmailConverter();
        assertThrows(IllegalArgumentException.class, () -> emailConverter.convert("defaultexample"));
        assertThrows(IllegalArgumentException.class, () -> emailConverter.convert("default@example,com"));
    }

    @Test
    public void addressConverter_convertAddress_success() {
        CsvConverters.AddressConverter addressConverter = new CsvConverters.AddressConverter();
        Address address = new Address("123 Main St");
        assertEquals(address, addressConverter.convert("123 Main St"));
    }

    @Test
    public void addressConverter_convertInvalidAddress_throwsIllegalArgumentException() {
        CsvConverters.AddressConverter addressConverter = new CsvConverters.AddressConverter();
        assertThrows(IllegalArgumentException.class, () -> addressConverter.convert("     "));
        assertThrows(IllegalArgumentException.class, () -> addressConverter.convert(""));
    }

    @Test
    public void tagsConverter_convertTag_success() {
        CsvConverters.TagsConverter tagConverter = new CsvConverters.TagsConverter();
        Set<Tag> tags = Set.of(new Tag("friend"));
        assertEquals(tags, tagConverter.convert("friend"));
    }

    @Test
    public void tagsConverter_convertInvalidTags_throwsIllegalArgumentException() {
        CsvConverters.TagsConverter tagConverter = new CsvConverters.TagsConverter();
        assertThrows(IllegalArgumentException.class, () -> tagConverter.convert(" , "));
    }

    @Test
    public void tagsConverter_convertTags_success() {
        CsvConverters.TagsConverter tagConverter = new CsvConverters.TagsConverter();
        Set<Tag> tags = Set.of(new Tag("friend"), new Tag("colleague"));
        assertEquals(tags, tagConverter.convert("friend,colleague"));
    }

    @Test
    public void tagsConverter_convertMultipleTags_success() {
        CsvConverters.TagsConverter tagConverter = new CsvConverters.TagsConverter();
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));
        tags.add(new Tag("colleague"));
        assertEquals("friend,colleague", tagConverter.convertToWrite(tags));
    }
}
