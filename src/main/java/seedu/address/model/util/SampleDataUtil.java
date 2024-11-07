package seedu.address.model.util;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyReceiptLog;
import seedu.address.model.ReceiptLog;
import seedu.address.model.goods.Goods;
import seedu.address.model.goods.GoodsCategories;
import seedu.address.model.goods.GoodsName;
import seedu.address.model.goodsreceipt.Date;
import seedu.address.model.goodsreceipt.GoodsReceipt;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                new Address("421 Marina Bay Road #12-34 Tan Kim PTE Building Singapore 123456"),
                getTagSet("colleagues"))
        };
    }

    public static GoodsReceipt[] getSampleGoodReceipts(ReadOnlyAddressBook addressBook) {
        ObservableList<Person> personObservableList = addressBook.getPersonList();
        Random rand = new Random();

        if (personObservableList.isEmpty()) {
            return new GoodsReceipt[] {};
        }

        Name[] names = new Name[3];
        for (int i = 0; i < 3; i++) {
            names[i] = personObservableList.get(rand.nextInt(personObservableList.size())).getName();
        }

        return new GoodsReceipt[] {
            new GoodsReceipt(
                    new Goods(new GoodsName("Laptop"), GoodsCategories.LIFESTYLE),
                    names[0],
                    new Date("2023-01-01 10:00"),
                    new Date("2023-01-05 15:00"),
                    true,
                    10,
                    1500.00
            ),
            new GoodsReceipt(
                    new Goods(new GoodsName("Apple"), GoodsCategories.CONSUMABLES),
                    names[1],
                    new Date("2023-02-01 11:00"),
                    new Date("2023-02-03 16:00"),
                    false,
                    20,
                    800.00
            ),
            new GoodsReceipt(
                    new Goods(new GoodsName("Pokemon Cards"), GoodsCategories.SPECIALTY),
                    names[2],
                    new Date("2023-03-01 12:00"),
                    new Date("2023-03-04 17:00"),
                    true,
                    15,
                    600.00
            )
        };
    }


    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    public static ReadOnlyReceiptLog getSampleReceiptLog(ReadOnlyAddressBook addressBook) {
        ReceiptLog sampleReceiptLog = new ReceiptLog();

        for (GoodsReceipt sampleGoodsReceipt : getSampleGoodReceipts(addressBook)) {
            sampleReceiptLog.addReceipt(sampleGoodsReceipt);
        }
        return sampleReceiptLog;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
