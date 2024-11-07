package seedu.address.logic.commands;

import static seedu.address.logic.commands.AddGoodsCommand.MESSAGE_DUPLICATE_RECEIPT;
import static seedu.address.logic.commands.AddGoodsCommand.MESSAGE_SUCCESS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalModel.getTypicalModel;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.ModelManager;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.person.Person;
import seedu.address.testutil.GoodsReceiptBuilder;

public class AddGoodsCommandTest {

    @Test
    public void execute_nonExistentSupplier_failure() {
        ModelManager model = getTypicalModel();
        GoodsReceipt goodsReceipt = model.findGoodsReceipt(r -> true).get();
        Person person = model.findPerson(p -> goodsReceipt.isFromSupplier(p.getName())).get();
        model.deletePerson(person);
        AddGoodsCommand command = new AddGoodsCommand(goodsReceipt);
        String expectedMessage = Messages.MESSAGE_PERSON_NOT_FOUND;
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_duplicatedGoods_failure() {
        ModelManager model = getTypicalModel();
        GoodsReceipt goodsReceipt = model.findGoodsReceipt(r -> true).get();
        AddGoodsCommand command = new AddGoodsCommand(goodsReceipt);
        String expectedMessage = MESSAGE_DUPLICATE_RECEIPT;
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_existentSupplierUniqueGoods_success() {
        ModelManager model = getTypicalModel();
        Person person = model.findPerson(p -> true).get();
        GoodsReceipt goodsReceipt = new GoodsReceiptBuilder()
                .withSupplierName(person.getName())
                .withGoodsName(new GoodsName("blah"))
                .build();
        AddGoodsCommand command = new AddGoodsCommand(goodsReceipt);
        ModelManager expectedModel = getTypicalModel();
        expectedModel.addGoods(goodsReceipt);
        String expectedString = String.format(MESSAGE_SUCCESS, goodsReceipt.getGoods().toString());
        assertCommandSuccess(command, model, expectedString, expectedModel);
    }
}
