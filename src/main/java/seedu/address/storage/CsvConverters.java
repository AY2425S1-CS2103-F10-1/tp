package seedu.address.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.opencsv.bean.AbstractBeanField;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Custom converters for OpenCSV to convert between CSV strings and Java objects.
 */
public class CsvConverters {
    /**
     * Custom converter for Path type.
     */
    public static class PathConverter extends AbstractBeanField<Path> {
        @Override
        protected Object convert(String value) {
            return Paths.get(value);
        }
    }

    /**
     * Custom converter for GuiSettings type.
     */
    public static class GuiSettingsConverter extends AbstractBeanField<GuiSettings> {

        @Override
        protected Object convert(String value) {
            // Assuming GuiSettings has a constructor that takes a CSV string
            String[] values = value.split(",");

            if (values.length != 4) {
                throw new IllegalArgumentException("Invalid CSV format for GuiSettings");
            }

            return new GuiSettings(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]));
        }

        @Override
        protected String convertToWrite(Object value) {
            return ((GuiSettings) value).toCsvString();
        }
    }

    /**
     * Custom converter for Name type.
     */
    public static class NameConverter extends AbstractBeanField<Name> {
        @Override
        protected Object convert(String value) {
            return new Name(value);
        }
    }

    /**
     * Custom converter for Address type.
     */
    public static class AddressConverter extends AbstractBeanField<Address> {
        @Override
        protected Object convert(String value) {
            return new Address(value);
        }
    }

    /**
     * Custom converter for Phone type.
     */
    public static class PhoneConverter extends AbstractBeanField<Phone> {
        @Override
        protected Object convert(String value) {
            return new Phone(value);
        }
    }

    /**
     * Custom converter for Email type.
     */
    public static class EmailConverter extends AbstractBeanField<Email> {
        @Override
        protected Object convert(String value) {
            return new Email(value);
        }
    }

    /**
     * Custom converter for Tags type.
     */
    public static class TagsConverter extends AbstractBeanField<Set<Tag>> {

        @Override
        protected Object convert(String value) {
            Set<Tag> tags = new HashSet<>();

            for (String tagName : value.split(",")) {
                tags.add(new Tag(tagName));
            }
            return tags;
        }

        @Override
        protected String convertToWrite(Object value) {
            StringBuilder sb = new StringBuilder();
            for (Tag tag : (Set<Tag>) value) {
                sb.append(tag.tagName).append(",");
            }

            if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }
}
