package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOODS_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.ListGoodsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.goods.GoodsCategories;
import seedu.address.model.goodsReceipt.CategoryPredicate;
import seedu.address.model.goodsReceipt.GoodsNamePredicate;
import seedu.address.model.goodsReceipt.GoodsReceipt;
import seedu.address.model.goodsReceipt.SupplierNamePredicate;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ListGoodsCommand object with the given predicate.
 */
public class ListGoodsCommandParser implements Parser<ListGoodsCommand> {
    // Dummy predicate for no provided args
    public static final Predicate<GoodsReceipt> DUMMY_PREDICATE = new Predicate<GoodsReceipt>() {
        @Override
        public boolean test(GoodsReceipt gr) {
            return true;
        }
    };

    /**
     * Parses the given {@code String} of arguments in the context of the ListGoodsCommand
     * and returns a ListGoodsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListGoodsCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CATEGORY, PREFIX_GOODS_NAME, PREFIX_NAME);
        Predicate<GoodsReceipt> predicate = null;

        // check for the three optional inputs and chain if needed
        if (arePrefixesPresent(argMultimap, PREFIX_CATEGORY)) {
            GoodsCategories category = ParserUtil.parseGoodsCategory(argMultimap.getValue(PREFIX_CATEGORY).get());
            Predicate<GoodsReceipt> catPredicate = new CategoryPredicate(category);
            predicate = (predicate == null) ? catPredicate : predicate.and(catPredicate);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_GOODS_NAME)) {
            // Allow for partial matches here
            String matchName = argMultimap.getValue(PREFIX_GOODS_NAME).get();
            Predicate<GoodsReceipt> goodsNamePredicate = new GoodsNamePredicate(matchName);
            predicate = (predicate == null) ? goodsNamePredicate : predicate.and(goodsNamePredicate);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            Predicate<GoodsReceipt> supplierNamePredicate = new SupplierNamePredicate(name);
            predicate = (predicate == null) ? supplierNamePredicate : predicate.and(supplierNamePredicate);
        }

        // default predicate to return all items
        if (predicate == null) {
            predicate = DUMMY_PREDICATE;
        }

        return new ListGoodsCommand(predicate);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
