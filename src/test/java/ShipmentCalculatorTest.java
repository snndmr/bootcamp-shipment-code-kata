import com.trendyol.shipment.Product;
import com.trendyol.shipment.ShipmentCalculator;
import com.trendyol.shipment.ShipmentSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ShipmentCalculatorTest {

    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
    }

    @Test
    void isBasketEmpty_EmptyBasket_ShouldReturnTrue() {
        assertTrue(ShipmentCalculator.isBasketNullOrEmpty(products));
    }

    @Test
    void isBasketEmpty_NonEmptyBasket_ShouldReturnFalse() {
        products.add(Product.create(ShipmentSize.SMALL));
        assertFalse(ShipmentCalculator.isBasketNullOrEmpty(products));
    }

    @Test
    void countShipmentSizes_ShouldCountSizesCorrectly() {
        products.addAll(Arrays.asList(Product.create(ShipmentSize.SMALL), Product.create(ShipmentSize.MEDIUM), Product.create(ShipmentSize.LARGE), Product.create(ShipmentSize.SMALL)));

        Map<ShipmentSize, Integer> shipmentSizeMap = ShipmentCalculator.countShipmentSizes(products);

        assertEquals(2, shipmentSizeMap.get(ShipmentSize.SMALL));
        assertEquals(1, shipmentSizeMap.get(ShipmentSize.MEDIUM));
        assertEquals(1, shipmentSizeMap.get(ShipmentSize.LARGE));
        assertNull(shipmentSizeMap.get(ShipmentSize.X_LARGE));
    }

    @Test
    void basketContainsLessThanThreshold_ShouldReturnTrue() {
        products.addAll(Arrays.asList(Product.create(ShipmentSize.SMALL), Product.create(ShipmentSize.SMALL)));

        assertTrue(ShipmentCalculator.basketContainsLessThanThreshold(products));
    }

    @Test
    void basketDoesNotContainLessThanThreshold_ShouldReturnFalse() {
        products.addAll(Arrays.asList(Product.create(ShipmentSize.SMALL), Product.create(ShipmentSize.SMALL), Product.create(ShipmentSize.SMALL)));

        assertFalse(ShipmentCalculator.basketContainsLessThanThreshold(products));
    }

    @Test
    void getLargestShipmentSize_ShouldReturnLargestSize() {
        Map<ShipmentSize, Integer> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 3, ShipmentSize.MEDIUM, 2, ShipmentSize.LARGE, 1);

        ShipmentSize largestSize = ShipmentCalculator.getLargestShipmentSize(shipmentSizeMap);

        assertEquals(ShipmentSize.LARGE, largestSize);
    }

    @Test
    void findMaxOccurringShipmentSize_ShouldReturnMaxOccurringSize() {
        Map<ShipmentSize, Integer> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 2, ShipmentSize.MEDIUM, 3, ShipmentSize.LARGE, 1);

        ShipmentSize maxOccurringSize = ShipmentCalculator.findMaxOccurringShipmentSize(shipmentSizeMap);

        assertEquals(ShipmentSize.MEDIUM, maxOccurringSize);
    }

    @Test
    void findMaxOccurringShipmentSize_NoneAboveThresholdWithEqualCounts_ShouldReturnNull() {
        Map<ShipmentSize, Integer> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 2, ShipmentSize.MEDIUM, 2, ShipmentSize.LARGE, 2);

        ShipmentSize maxOccurringSize = ShipmentCalculator.findMaxOccurringShipmentSize(shipmentSizeMap);

        assertNull(maxOccurringSize);
    }

    @Test
    void getNextShipmentSize_ShouldReturnNextSize() {
        assertEquals(ShipmentSize.MEDIUM, ShipmentCalculator.getNextShipmentSize(ShipmentSize.SMALL));
        assertEquals(ShipmentSize.LARGE, ShipmentCalculator.getNextShipmentSize(ShipmentSize.MEDIUM));
        assertEquals(ShipmentSize.X_LARGE, ShipmentCalculator.getNextShipmentSize(ShipmentSize.LARGE));
        assertEquals(ShipmentSize.X_LARGE, ShipmentCalculator.getNextShipmentSize(ShipmentSize.X_LARGE));
    }
}