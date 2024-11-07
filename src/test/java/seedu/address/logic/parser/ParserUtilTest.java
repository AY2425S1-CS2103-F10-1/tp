package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "12367456";
    private static final String VALID_ADDRESS = "123 Orchard Road #12-34 ABC Building Singapore 123456";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    private static final String DATETIME_INVALID_FORMAT = "2024-12-12 11, 00";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseGoodsName_invalidValue_throwsParseException() {
        String invalidGoodsName = "Bread*Invalid";
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsName(invalidGoodsName));
    }

    @Test
    public void parseGoodsName_validValue_success() {
        String validGoodsName = "Milk Bread";
        assertDoesNotThrow(() -> ParserUtil.parseGoodsName(validGoodsName));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }


    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseGoodsCategory_invalidValue_throwsParseException() {
        String invalidCategory = "NOTGOODS";
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsCategory(invalidCategory));
    }

    @Test
    public void parseGoodsCategory_validValue_success() {
        String validCategory = "CONSUMABLES";
        assertDoesNotThrow(() -> ParserUtil.parseGoodsCategory(validCategory));
    }

    @Test
    public void parseDateTimeValues_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDateTimeValues(null));
    }

    @Test
    public void parseDateTimeValues_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateTimeValues(DATETIME_INVALID_FORMAT));
    }

    @Test
    public void parseGoodsQuantity_invalidFormat_failure() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsQuantity("#"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsQuantity("1.1"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsQuantity("1 "));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsQuantity(" 1"));
    }

    @Test
    public void parseGoodsQuantity_validFormatButNegative_failure() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsQuantity("-1"));
    }

    @Test
    public void parseGoodsQuantity_validFormat_success() {
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("1"));
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("0"));
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("1234567890"));
    }

    @Test
    public void parseGoodsPrice_invalidFormat_failure() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("#"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("$"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("$1.1"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("$1"));
    }

    @Test
    public void parseGoodsPrice_validFormatButNegative_failure() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("-1"));
        assertThrows(ParseException.class, () -> ParserUtil.parseGoodsPrice("-1.1"));
    }

    @Test
    public void parseGoodsPrice_validFormat_success() {
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("1"));
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("0"));
        assertDoesNotThrow(() -> ParserUtil.parseGoodsQuantity("1234567890"));
    }

    @Test
    public void parseProcurementDate_futureDate_failure() {
        // Test case may not work in year 292278994.
        Date future = new Date(Long.MAX_VALUE);
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(future);
        assertThrows(ParseException.class, () -> ParserUtil.parseProcurementDate(dateString));
    }

    @Test
    public void parseProcurementDate_notFutureDate_success() {
        Date now = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(now);
        assertDoesNotThrow(() -> ParserUtil.parseProcurementDate(dateString));
    }
}
