package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOODS_NAME;

import seedu.address.model.Model;
import seedu.address.model.goods.GoodsName;

/**
 * Deletes all goods that match the given goodsName.
 */
public class DeleteGoodsCommand extends Command {
    public static final String COMMAND_WORD = "deletegoods";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes all goods that match the specified name."
            + "Parameters: "
            + PREFIX_GOODS_NAME + "GOODS_NAME"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GOODS_NAME + "Calbee Seaweed Chips";

    public static final String MESSAGE_SUCCESS = "%1$s has been deleted.";
    public static final String MESSAGE_FAILURE = "%s does not currently exist.";

    private final GoodsName goodsName;

    public DeleteGoodsCommand(String goodsName) {
        this.goodsName = new GoodsName(goodsName);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.deleteGoods(goodsName);

        return new CommandResult(String.format(MESSAGE_SUCCESS, goodsName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteGoodsCommand otherDeleteGoodsCommand)) {
            return false;
        }

        return goodsName.equals(otherDeleteGoodsCommand.goodsName);
    }
}
