package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ARRIVAL_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOODS_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROCUREMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.goodsreceipt.GoodsReceipt;

/**
 * Adds trackable goods linked to contacted suppliers.
 */
public class AddGoodsCommand extends Command {
    public static final String COMMAND_WORD = "addgoods";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds linked goods to the address book. \n"
            + "Parameters: "
            + PREFIX_GOODS_NAME + "GOODS_NAME "
            + PREFIX_QUANTITY + "QUANTITY "
            + PREFIX_PRICE + "PRICE "
            + PREFIX_CATEGORY + "CATEGORY "
            + PREFIX_PROCUREMENT_DATE + "PROCUREMENT_DATE "
            + PREFIX_ARRIVAL_DATE + "ARRIVAL_DATE "
            + PREFIX_NAME + "SUPPLIER_NAME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GOODS_NAME + "Gardenia Milk Bread "
            + PREFIX_QUANTITY + "2 "
            + PREFIX_PRICE + "5 "
            + PREFIX_CATEGORY + "CONSUMABLES "
            + PREFIX_PROCUREMENT_DATE + "2024-08-08 11:00 "
            + PREFIX_ARRIVAL_DATE + "2024-11-11 11:00 "
            + PREFIX_NAME + "Alex Yeoh \n";

    public static final String MESSAGE_SUCCESS = "New goods added: %1$s";

    public static final String MESSAGE_DUPLICATE_RECEIPT = "This goods already exists in the address book";

    private final GoodsReceipt goodsReceipt;

    /**
     * Creates an AddGoodsCommand that adds goods.
     * @param goodsReceipt
     */
    public AddGoodsCommand(GoodsReceipt goodsReceipt) {
        requireNonNull(goodsReceipt);
        this.goodsReceipt = goodsReceipt;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.findPerson(p -> goodsReceipt.isFromSupplier(p.getName())).isEmpty()) {
            throw new CommandException(Messages.MESSAGE_PERSON_NOT_FOUND);
        }
        if (model.findGoodsReceipt(goodsReceipt::equals).isPresent()) {
            throw new CommandException(MESSAGE_DUPLICATE_RECEIPT);
        }
        model.addGoods(goodsReceipt);
        return new CommandResult(String.format(MESSAGE_SUCCESS, goodsReceipt.getGoods().toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddGoodsCommand otherAddCommand)) {
            return false;
        }

        return goodsReceipt.equals(otherAddCommand.goodsReceipt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("goodsReceipt", goodsReceipt)
                .toString();
    }
}
