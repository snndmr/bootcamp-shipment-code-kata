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
        assertTrue(ShipmentCalculator.isBasketEmpty(products));
    }

    @Test
    void isBasketEmpty_NonEmptyBasket_ShouldReturnFalse() {
        products.add(Product.create(ShipmentSize.SMALL));
        assertFalse(ShipmentCalculator.isBasketEmpty(products));
    }

    @Test
    void countShipmentSizes_ShouldCountSizesCorrectly() {
        products.addAll(Arrays.asList(Product.create(ShipmentSize.SMALL), Product.create(ShipmentSize.MEDIUM), Product.create(ShipmentSize.LARGE), Product.create(ShipmentSize.SMALL)));

        Map<ShipmentSize, Long> shipmentSizeMap = ShipmentCalculator.countShipmentSizes(products);

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
        Map<ShipmentSize, Long> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 3L, ShipmentSize.MEDIUM, 2L, ShipmentSize.LARGE, 1L);

        ShipmentSize largestSize = ShipmentCalculator.getLargestShipmentSize(shipmentSizeMap);

        assertEquals(ShipmentSize.LARGE, largestSize);
    }

    @Test
    void findMaxOccurringShipmentSize_ShouldReturnMaxOccurringSize() {
        Map<ShipmentSize, Long> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 2L, ShipmentSize.MEDIUM, 3L, ShipmentSize.LARGE, 1L);

        ShipmentSize maxOccurringSize = ShipmentCalculator.findMaxOccurringShipmentSize(shipmentSizeMap);

        assertEquals(ShipmentSize.MEDIUM, maxOccurringSize);
    }

    @Test
    void findMaxOccurringShipmentSize_NoneAboveThresholdWithEqualCounts_ShouldReturnNull() {
        Map<ShipmentSize, Long> shipmentSizeMap = Map.of(ShipmentSize.SMALL, 2L, ShipmentSize.MEDIUM, 2L, ShipmentSize.LARGE, 2L);

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