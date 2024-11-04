package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGoods.getTypicalGoodsReceipts;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.goods.Goods;
import seedu.address.model.goods.GoodsCategories;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.GoodsBuilder;
import seedu.address.testutil.GoodsReceiptBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void deletePerson_supplierHasGoods_removedGoods() {
        GoodsReceipt goodsReceipt = new GoodsReceiptBuilder()
                .withSupplierName(ALICE.getName())
                .build();
        modelManager.addPerson(ALICE);
        modelManager.addGoods(goodsReceipt);
        modelManager.deletePerson(ALICE);
        List<GoodsReceipt> goodsList = modelManager
                .getFilteredGoods(r -> r.isFromSupplier(ALICE.getName()));
        assertEquals(goodsList.size(), 0);
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs, getTypicalGoodsReceipts());
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs, getTypicalGoodsReceipts());
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs, getTypicalGoodsReceipts())));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs, getTypicalGoodsReceipts())));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs, getTypicalGoodsReceipts())));
    }

    @Test
    public void setGoods_nullGoods_throwsNullPointerException() {
        // TODO: Implement this
    }

    @Test
    public void setGoods_validGoods_setsGoods() {
        // TODO: Implement this
    }

    @Test
    public void getGoods_modifyList_throwsUnsupportedOperationException() {
        // TODO: Implement this
    }

    @Test
    public void addGoods_nullGoods_throwsNullPointerException() {
        // TODO: Implement this
    }

    @Test
    public void addGoods_validGoods_addsGoods() {
        // TODO: Implement this
    }

    @Test
    public void getFilteredGoods() {
        // TODO: Implement this
    }

    @Test
    public void getFilteredPersonsWithGoodsCategoryTagsAdded() {
        modelManager.addPerson(ALICE);
        Goods apple = new GoodsBuilder()
                .withName("Apple")
                .withGoodsCategory(GoodsCategories.SPECIALTY)
                .build();
        Goods banana = new GoodsBuilder()
                .withName("Banana")
                .withGoodsCategory(GoodsCategories.CONSUMABLES)
                .build();
        GoodsReceipt appleReceipt = new GoodsReceiptBuilder()
                .withSupplierName(ALICE.getName())
                .withGoods(apple)
                .build();
        GoodsReceipt bananaReceipt = new GoodsReceiptBuilder()
                .withSupplierName(ALICE.getName())
                .withGoods(banana)
                .build();
        modelManager.addGoods(appleReceipt);
        modelManager.addGoods(bananaReceipt);
        modelManager.updateFilteredPersonList(p -> p.equals(ALICE));
        Person alice = modelManager
                .getObservableFilteredPersonsWithGoodsCategoryTagsAdded()
                .stream().findAny().get();
        Set<Tag> expectedTags = Stream
                .of(ALICE.getTags(), List.of(new Tag("SPECIALTY"), new Tag("CONSUMABLES")))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        assertEquals(alice.getTags(), expectedTags);
    }

    @Test
    public void deleteGoods_removedGoods() {
        GoodsReceipt goodsReceipt = new GoodsReceiptBuilder()
                .withGoodsName(new GoodsName("Calbee"))
                .withSupplierName(ALICE.getName())
                .build();
        modelManager.addPerson(ALICE);
        modelManager.addGoods(goodsReceipt);
        modelManager.deleteGoods(goodsReceipt.getGoods().getGoodsName());
        List<GoodsReceipt> goodsList = modelManager
                .getFilteredGoods(r -> r.getGoods().getGoodsName().equals("Calbee"));
        assertEquals(goodsList.size(), 0);
    }
}
