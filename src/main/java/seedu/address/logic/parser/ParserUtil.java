package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.goods.GoodsCategories;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.Date;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String QUANTITY_MESSAGE_CONSTRAINT = "Quantity must be an non-negative integer.";
    public static final String PRICE_MESSAGE_CONSTRAINT = "Price must be a non-negative number.";
    public static final String PROCUREMENT_DATE_MESSAGE_CONSTRAINT = "Procurement date must not be in the future.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String goodsName} into a {@code GoodsName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code goodsName} is invalid.
     */
    public static GoodsName parseGoodsName(String goodsName) throws ParseException {
        String trimmedName = goodsName.trim();
        if (!GoodsName.isValidName(trimmedName)) {
            throw new ParseException(GoodsName.MESSAGE_CONSTRAINTS);
        }
        return new GoodsName(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses {@code String category} into a {@code GoodsCategories}
     * @param category A string containing the category name
     *
     * @throws ParseException if the given {@code category} is invalid
     */
    public static GoodsCategories parseGoodsCategory(String category) throws ParseException {
        requireNonNull(category);
        switch (category) {
        case "CONSUMABLES":
            return GoodsCategories.CONSUMABLES;
        case "LIFESTYLE":
            return GoodsCategories.LIFESTYLE;
        case "SPECIALTY":
            return GoodsCategories.SPECIALTY;
        default:
            throw new ParseException(GoodsCategories.MESSAGE_UNKNOWN_CATEGORY);
        }
    }

    /**
     * Parses {@code Collection<String> goods categories} into a {@code Set<GoodsCategories>}.
     */
    public static Set<GoodsCategories> parseGoodsCategories(Collection<String> goodCategories) throws ParseException {
        requireNonNull(goodCategories);
        final Set<GoodsCategories> goodsCategoriesSet = new HashSet<>();
        for (String goodCategory : goodCategories) {
            goodsCategoriesSet.add(parseGoodsCategory(goodCategory));
        }
        return goodsCategoriesSet;
    }

    /**
     * Parses {@code String dateTime} into a {@code Date}
     * @param datetime A string containing the datetime string
     *
     * @throws ParseException if the given {@code dateTime} does not match the format
     */
    public static Date parseDateTimeValues(String datetime) throws ParseException {
        requireNonNull(datetime);
        Date date;
        try {
            date = new Date(datetime);
        } catch (DateTimeException e) {
            throw new ParseException(Date.MESSAGE_INVALID_FORMAT);
        }

        return date;
    }

    /**
     * Parses a string of datetime to a valid procurement date.
     */
    public static Date parseProcurementDate(String datetime) throws ParseException {
        Date d = parseDateTimeValues(datetime);
        if (d.getDateTime().isAfter(LocalDateTime.now())) {
            throw new ParseException(PROCUREMENT_DATE_MESSAGE_CONSTRAINT);
        }
        return d;
    }

    /**
     * Parses a string of datetime to a valid arrival date.
     */
    public static Date parseArrivalDate(String datetime) throws ParseException {
        return parseDateTimeValues(datetime);
    }

    /**
     * Parses a string quantity to an int.
     */
    public static int parseGoodsQuantity(String quantity) throws ParseException {
        int v;
        try {
            v = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new ParseException(QUANTITY_MESSAGE_CONSTRAINT);
        }
        if (v < 0) {
            throw new ParseException(QUANTITY_MESSAGE_CONSTRAINT);
        }
        return v;
    }

    /**
     * Parses a string price to a double.
     */
    public static double parseGoodsPrice(String price) throws ParseException {
        double v;
        try {
            v = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            throw new ParseException(PRICE_MESSAGE_CONSTRAINT);
        }
        if (v < 0) {
            throw new ParseException(PRICE_MESSAGE_CONSTRAINT);
        }
        return v;
    }
}
