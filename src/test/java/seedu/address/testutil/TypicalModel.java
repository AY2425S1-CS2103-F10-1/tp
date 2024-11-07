package seedu.address.testutil;

import static seedu.address.testutil.TypicalGoods.getTypicalGoodsReceipts;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * A utility class that creates a typical model manager to be used in tests.
 */
public class TypicalModel {
    public static ModelManager getTypicalModel() {
        return new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalGoodsReceipts());
    }
}
