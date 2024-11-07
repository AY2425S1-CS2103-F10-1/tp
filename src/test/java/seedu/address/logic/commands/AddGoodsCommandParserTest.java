package seedu.address.logic.commands;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ARRIVAL_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOODS_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROCUREMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.AddGoodsCommandParser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.goods.Goods;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.GoodsReceipt;

public class AddGoodsCommandParserTest {

    private static final String ARRIVAL_DATE_DESC = " ad/2024-11-11 11:00";
    private static final String GOODS_CATEGORY_DESC = " c/CONSUMABLES";
    private static final String GOODS_NAME_DESC = " gn/Gardenia Milk Bread";
    private static final String PRICE_DESC = " p/5.5";
    private static final String PROCUREMENT_DATE_DESC = " pd/2024-08-08 11:00";
    private static final String QUANTITY_DESC = " q/2";
    private static final String SUPPLIER_NAME_DESC = " n/Alex Yeoh";

    private static final String USER_INPUT = GOODS_NAME_DESC + QUANTITY_DESC + PRICE_DESC + PROCUREMENT_DATE_DESC
            + ARRIVAL_DATE_DESC + SUPPLIER_NAME_DESC + GOODS_CATEGORY_DESC;

    private final AddGoodsCommandParser parser = new AddGoodsCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws ParseException {
        Goods goods = new Goods(
                new GoodsName(GOODS_NAME_DESC.substring(4)),
                ParserUtil.parseGoodsCategory(GOODS_CATEGORY_DESC.substring(3)));
        GoodsReceipt goodsReceipt = new GoodsReceipt(
                goods,
                ParserUtil.parseName(SUPPLIER_NAME_DESC.substring(3)),
                ParserUtil.parseDateTimeValues(PROCUREMENT_DATE_DESC.substring(4)),
                ParserUtil.parseDateTimeValues(ARRIVAL_DATE_DESC.substring(4)),
                false,
                ParserUtil.parseGoodsQuantity(QUANTITY_DESC.substring(3)),
                ParserUtil.parseGoodsPrice(PRICE_DESC.substring(3)));
        AddGoodsCommand expectedCommand = new AddGoodsCommand(goodsReceipt);
        assertParseSuccess(parser, USER_INPUT, expectedCommand);
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        Map<String, Prefix> argsPrefix = Map.of(
                GOODS_NAME_DESC, PREFIX_GOODS_NAME,
                QUANTITY_DESC, PREFIX_QUANTITY,
                PRICE_DESC, PREFIX_PRICE,
                PROCUREMENT_DATE_DESC, PREFIX_PROCUREMENT_DATE,
                ARRIVAL_DATE_DESC, PREFIX_ARRIVAL_DATE,
                SUPPLIER_NAME_DESC, PREFIX_NAME,
                GOODS_CATEGORY_DESC, PREFIX_CATEGORY);
        for (String arg : argsPrefix.keySet()) {
            String newUserInput = USER_INPUT + arg;
            assertParseFailure(parser, newUserInput,
                    Messages.getErrorMessageForDuplicatePrefixes(argsPrefix.get(arg)));
        }
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        List<String> args = List.of(
                GOODS_NAME_DESC, QUANTITY_DESC, PRICE_DESC, PROCUREMENT_DATE_DESC,
                ARRIVAL_DATE_DESC, SUPPLIER_NAME_DESC, GOODS_CATEGORY_DESC);
        for (int i = 0; i < args.size(); i++) {
            List<String> left = args.subList(0, i);
            List<String> right = args.subList(i + 1, args.size());
            String newUserInput = Stream.of(left, right)
                    .flatMap(Collection::stream)
                    .collect(Collectors.joining(""));
            assertParseFailure(parser, newUserInput,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGoodsCommand.MESSAGE_USAGE));
        }
    }
}
