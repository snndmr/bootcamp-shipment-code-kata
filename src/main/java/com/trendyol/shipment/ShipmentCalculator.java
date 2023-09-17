package com.trendyol.shipment;

import java.util.*;
import java.util.stream.Collectors;

public class ShipmentCalculator {
    private static final int SAME_SIZE_THRESHOLD = 3;

    private ShipmentCalculator() {}

    public static ShipmentSize calculateShipmentSize(List<Product> products) {
        if (isBasketNullOrEmpty(products)) {
            throw new NoProductsOnBasketException();
        }

        Map<ShipmentSize, Integer> shipmentSizeMap = countShipmentSizes(products);

        if (basketContainsLessThanThreshold(products)) {
            return getLargestShipmentSize(shipmentSizeMap);
        }

        return findMaxOccurringShipmentSize(shipmentSizeMap)
                .map(ShipmentCalculator::getNextShipmentSize)
                .orElse(getLargestShipmentSize(shipmentSizeMap));
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

    public static Optional<ShipmentSize> findMaxOccurringShipmentSize(Map<ShipmentSize, Integer> shipmentSizeMap) {
        return shipmentSizeMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= SAME_SIZE_THRESHOLD)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static ShipmentSize getNextShipmentSize(ShipmentSize shipmentSize) {
        return Arrays.stream(ShipmentSize.values())
                .filter(size -> size.ordinal() > shipmentSize.ordinal())
                .findFirst()
                .orElse(shipmentSize);
    }
}