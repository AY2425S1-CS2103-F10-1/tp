package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns a person that satisfies the predicate.
     */
    Optional<Person> findPerson(Predicate<Person> predicate);

    /**
     * Returns a list of person
     */
    List<Person> getPersonList();

    /**
     * Replaces goods data with the data in {@code goodsReceipt}.
     */
    void setGoods(ReadOnlyReceiptLog goodsReceipts);


    /**
     * Returns the goods list.
     */
    ReadOnlyReceiptLog getGoods();

    /**
     * Adds the given goods.
     * {@code goodsReceipt} must not already exist in the address book.
     */
    void addGoods(GoodsReceipt goodsReceipt);

    /**
     * Deletes all the goods which match the GoodsName given.
     * @param goodsName The GoodsName indicating the goods to be deleted.
     */
    boolean deleteGoods(GoodsName goodsName);

    /** Returns an unmodifiable view of the filtered goodsReceipt list */
    ObservableList<GoodsReceipt> getFilteredReceiptsList();

    /**
     * Filters a list of goods by the given {@code predicate}.
     */
    public List<GoodsReceipt> getFilteredGoods(Predicate<GoodsReceipt> predicate);

    /**
     * Updates the filter of the filtered goods receipts list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredReceiptsList(Predicate<GoodsReceipt> predicate);

    /**
     * Returns the total quantity of the filtered goods list.
     */
    public int getFilteredGoodsQuantityStatistics();

    /**
     * Returns the total cost of the filtered goods list.
     */
    public double getFilteredGoodsCostStatistics();
}
