package seedu.address.storage;

import com.opencsv.bean.AbstractBeanField;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CsvConverters {
    public static class PathConverter extends AbstractBeanField<Path> {
        @Override
        protected Object convert(String value) {
            return Paths.get(value);
        }
    }

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
            // Assuming GuiSettings has a method to convert to CSV string
            return ((GuiSettings) value).toCsvString();
        }
    }

    public static class NameConverter extends AbstractBeanField<Name> {
        @Override
        protected Object convert(String value) {
            return new Name(value);
        }
    }

    public static class AddressConverter extends AbstractBeanField<Address> {
        @Override
        protected Object convert(String value) {
            return new Address(value);
        }
    }

    public static class PhoneConverter extends AbstractBeanField<Phone> {
        @Override
        protected Object convert(String value) {
            System.out.println(value);
            return new Phone(value);
        }
    }

    public static class EmailConverter extends AbstractBeanField<Email> {
        @Override
        protected Object convert(String value) {
            return new Email(value);
        }
    }

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
