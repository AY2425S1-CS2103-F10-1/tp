package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.goodsreceipt.GoodsReceiptUtil;
import seedu.address.model.goodsreceipt.exceptions.IllegalSupplierNameException;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<GoodsReceipt> filteredReceipts;
    private final ReceiptLog goodsList;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook,
                        ReadOnlyUserPrefs userPrefs,
                        ReadOnlyReceiptLog goodsList) {
        requireAllNonNull(addressBook, userPrefs, goodsList);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.goodsList = new ReceiptLog(goodsList);
        filterIllegalSupplierNames();
        filteredReceipts = new FilteredList<>(this.goodsList.getReceiptList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ReceiptLog());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        requireNonNull(target);
        addressBook.removePerson(target);
        int previousSize = goodsList.size();
        goodsList.removeIf(receipt -> receipt.isFromSupplier(target.getName()));
        int removedReceiptCount = goodsList.size() - previousSize;
        logger.fine(String.format(
                "Deleted suppler of name %s. Removed %d receipts.",
                target.getName(), removedReceiptCount));
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    @Override
    public Optional<Person> findPerson(Predicate<Person> predicate) {
        return addressBook.findPerson(predicate);
    }

    @Override
    public List<Person> getPersonList() {
        return addressBook.getPersonList().stream().toList();
    }

    //=========== Goods ================================================================================

    @Override
    public void setGoods(ReadOnlyReceiptLog goodsReceipts) {
        requireNonNull(goodsReceipts);
        goodsList.resetData(goodsReceipts);
    }

    @Override
    public ReadOnlyReceiptLog getGoods() {
        return goodsList;
    }

    @Override
    public void addGoods(GoodsReceipt goodsReceipt) {
        requireNonNull(goodsReceipt);

        if (hasExistingSupplier(goodsReceipt)) {
            goodsList.addReceipt(goodsReceipt);
        } else {
            throw new IllegalSupplierNameException();
        }
    }

    @Override
    public ObservableList<GoodsReceipt> getFilteredReceiptsList() {
        return filteredReceipts;
    }

    @Override
    public void updateFilteredReceiptsList(Predicate<GoodsReceipt> predicate) {
        requireNonNull(predicate);
        filteredReceipts.setPredicate(predicate);
    }

    @Override
    public List<GoodsReceipt> getFilteredGoods(Predicate<GoodsReceipt> predicate) {
        filteredReceipts.setPredicate(predicate);
        return this.goodsList.findReceipts(predicate);
    }

    @Override
    public boolean deleteGoods(GoodsName goodsName) {
        return goodsList.removeIf(receipt -> Objects.equals(receipt.getGoods().getGoodsName(), goodsName));
    }

    @Override
    public int getFilteredGoodsQuantityStatistics() {
        return GoodsReceiptUtil.sumQuantity(filteredReceipts);
    }

    @Override
    public double getFilteredGoodsCostStatistics() {
        return GoodsReceiptUtil.sumTotals(filteredReceipts);
    }

    private boolean hasExistingSupplier(GoodsReceipt goodsReceipt) {
        return addressBook.getPersonList()
                .stream()
                .map(Person::getName)
                .anyMatch(personName -> personName.equals(goodsReceipt.getSupplierName()));
    }

    private void filterIllegalSupplierNames() {
        goodsList.removeIf(receipt -> !hasExistingSupplier(receipt));
    }
}
