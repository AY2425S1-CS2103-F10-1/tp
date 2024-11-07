package seedu.address.model.goodsreceipt;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.storage.CsvConverters.GoodsConverter;
import static seedu.address.storage.CsvConverters.GoodsDateConverter;
import static seedu.address.storage.CsvConverters.PersonNameConverter;

import java.time.LocalDateTime;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;

import seedu.address.model.goods.Goods;
import seedu.address.model.person.Name;

/**
 * The {@code GoodsReceipt} class represents the receipt of goods from a supplier
 * Guarantees: Immutable;
 */
public class GoodsReceipt {

    @CsvCustomBindByName(column = "Goods", converter = GoodsConverter.class, required = true)
    private final Goods goods;

    @CsvCustomBindByName(column = "Supplier Name", converter = PersonNameConverter.class, required = true)
    private final Name supplierName;

    @CsvCustomBindByName(column = "Procurement Date", converter = GoodsDateConverter.class, required = true)
    @CsvDate(value = "yyyy-MM-dd HH:mm")
    private final Date procurementDate;

    @CsvCustomBindByName(column = "Arrival Date", converter = GoodsDateConverter.class, required = true)
    @CsvDate(value = "yyyy-MM-dd HH:mm")
    private final Date arrivalDate;

    @CsvBindByName(column = "Is Delivered", required = true)
    private final boolean isDelivered;

    @CsvBindByName(column = "Quantity", required = true)
    @CsvNumber(value = "#")
    private final int quantity;

    @CsvBindByName(column = "Price", required = true)
    @CsvNumber(value = "0.00")
    private final double price;

    /**
     * Constructs a {@code GoodsReceipt}.
     * For use in Csv files. Empty constructor is required for CsvBeanReader.
     */
    public GoodsReceipt() {
        this.goods = null;
        this.supplierName = null;
        this.procurementDate = null;
        this.arrivalDate = null;
        this.isDelivered = false;
        this.quantity = 0;
        this.price = 0.0;
    }

    /**
     * Constructs a {@code GoodsReceipt}.
     *
     * @param goods The goods associated with this receipt.
     * @param supplierName The name of the supplier.
     * @param procurementDate The date when the goods were procured.
     * @param arrivalDate The date when the goods are expected to or have arrived.
     * @param isDelivered A boolean indicating whether the goods have been delivered.
     * @param quantity The quantity of goods in this receipt.
     * @param price The price of the goods in this receipt.
     */
    public GoodsReceipt(Goods goods, Name supplierName, Date procurementDate, Date arrivalDate,
                        boolean isDelivered, int quantity, double price) {
        requireAllNonNull(goods, quantity, procurementDate, arrivalDate, isDelivered);
        checkArgument(isValidProcurementDate(procurementDate));
        checkArgument(isValidQuantity(quantity));
        checkArgument(isValidPrice(price));
        this.supplierName = supplierName;
        this.goods = goods;
        this.procurementDate = procurementDate;
        this.arrivalDate = arrivalDate;
        this.isDelivered = isDelivered;
        this.quantity = quantity;
        this.price = price;

    }

    /**
     * Returns True if the quantity is valid.
     *
     * @param quantity The price of the goods.
     * @return True if the quantity is valid.
     */
    public static boolean isValidQuantity(double quantity) {
        return 0 <= quantity;
    }

    /**
     * Returns True if the price is valid.
     *
     * @param price The price of the goods.
     * @return True if the price is valid.
     */
    public static boolean isValidPrice(double price) {
        return 0 <= price;
    }


    /**
     * Returns True if the procurement date is valid.
     */
    public static boolean isValidProcurementDate(Date date) {
        return !date.getDateTime().isAfter(LocalDateTime.now());
    }

    /**
     * Returns True if supplier is valid
     */
    public boolean isFromSupplier(Name supplier) {
        requireNonNull(supplier);
        return supplierName.equals(supplier);
    }

    /**
     * Returns quantity of the goods.
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Returns if goods are delivered
     */
    public boolean isDelivered() {
        return isDelivered;
    }

    /**
     * Returns goods.
     */
    public Goods getGoods() {
        return this.goods;
    }

    /**
     * Returns supplier name.
     */
    public Name getSupplierName() {
        return this.supplierName;
    }

    /**
     * Returns procurement date.
     */
    public Date getProcurementDate() {
        return this.procurementDate;
    }

    /**
     * Returns arrival date.
     */
    public Date getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Returns total price of the goods.
     */
    public double getPriceTotal() {
        return this.quantity * this.price;
    }

    /**
     * Return a new goods object with the new delivery status.
     */
    public GoodsReceipt markAsDelivered() {
        return new GoodsReceipt(this.goods, this.supplierName, this.procurementDate, this.arrivalDate, true,
                this.quantity, this.price);
    }

    /**
     * Format goods as text for viewing.
     */
    @Override
    public String toString() {
        String deliveryStatus = this.isDelivered ? "Delivered" : "Pending";
        return String.format("[Quantity %d] %s (%s)", this.quantity, this.goods.toString(), deliveryStatus);
    }


    /**
     * Checks if receipt is the same (Logic may be different from hashcode)
     * E.g. delivered variable is not checked here
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GoodsReceipt otherReceipt)) {
            return false;
        }

        return this.goods.equals(otherReceipt.goods)
                &&
                this.supplierName.equals(otherReceipt.supplierName)
                &&
                this.arrivalDate.equals(otherReceipt.arrivalDate)
                &&
                this.procurementDate.equals(otherReceipt.procurementDate)
                &&
                this.quantity == otherReceipt.quantity
                &&
                this.price == otherReceipt.price;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(goods, supplierName, arrivalDate, procurementDate, quantity, price);
    }

}
