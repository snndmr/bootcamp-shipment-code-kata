package com.trendyol.shipment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShipmentCalculator {
    private static final int SAME_SIZE_THRESHOLD = 3;

    public static ShipmentSize calculateShipmentSize(List<Product> products) {
        if (isBasketNullOrEmpty(products)) {
            throw new ShipmentCalculationException("Basket is empty.");
        }

        Map<ShipmentSize, Integer> shipmentSizeMap = countShipmentSizes(products);

        if (basketContainsLessThanThreshold(products)) {
            return getLargestShipmentSize(shipmentSizeMap);
        }

        ShipmentSize maxShipmentSize = findMaxOccurringShipmentSize(shipmentSizeMap);

        return maxShipmentSize != null ? getNextShipmentSize(maxShipmentSize) : getLargestShipmentSize(shipmentSizeMap);
    }

    public static boolean isBasketNullOrEmpty(List<Product> products) {
        return products == null || products.isEmpty();
    }

    public static Map<ShipmentSize, Integer> countShipmentSizes(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.summingInt(e -> 1)));
    }

    public static boolean basketContainsLessThanThreshold(List<Product> products) {
        return products.size() < SAME_SIZE_THRESHOLD;
    }

    public static ShipmentSize getLargestShipmentSize(Map<ShipmentSize, Integer> shipmentSizeMap) {
        return Collections.max(shipmentSizeMap.keySet());
    }

    public static ShipmentSize findMaxOccurringShipmentSize(Map<ShipmentSize, Integer> shipmentSizeMap) {
        return shipmentSizeMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= SAME_SIZE_THRESHOLD)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public static ShipmentSize getNextShipmentSize(ShipmentSize shipmentSize) {
        return Arrays.stream(ShipmentSize.values())
                .filter(size -> size.ordinal() > shipmentSize.ordinal())
                .findFirst()
                .orElse(shipmentSize);
    }
}