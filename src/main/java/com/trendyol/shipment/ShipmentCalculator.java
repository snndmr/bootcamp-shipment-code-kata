package com.trendyol.shipment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentCalculator {
    private static final int SAME_SIZE_THRESHOLD = 3;

    public static ShipmentSize calculateShipmentSize(List<Product> products) {
        if (isBasketEmpty(products)) {
            throw new ShipmentCalculationException("Basket is empty.");
        }

        Map<ShipmentSize, Long> shipmentSizeMap = countShipmentSizes(products);

        if (basketContainsLessThanThreshold(products)) {
            return getLargestShipmentSize(shipmentSizeMap);
        }

        ShipmentSize maxShipmentSize = findMaxOccurringShipmentSize(shipmentSizeMap);

        return maxShipmentSize != null ? getNextShipmentSize(maxShipmentSize) : getLargestShipmentSize(shipmentSizeMap);
    }

    public static boolean isBasketEmpty(List<Product> products) {
        return products == null || products.isEmpty();
    }

    public static Map<ShipmentSize, Long> countShipmentSizes(List<Product> products) {
        Map<ShipmentSize, Long> shipmentSizeMap = new HashMap<>();

        for (Product product : products) {
            ShipmentSize size = product.getSize();
            shipmentSizeMap.put(size, shipmentSizeMap.getOrDefault(size, 0L) + 1);
        }

        return shipmentSizeMap;
    }

    public static boolean basketContainsLessThanThreshold(List<Product> products) {
        return products.size() < SAME_SIZE_THRESHOLD;
    }

    public static ShipmentSize getLargestShipmentSize(Map<ShipmentSize, Long> shipmentSizeMap) {
        return Collections.max(shipmentSizeMap.keySet());
    }

    public static ShipmentSize findMaxOccurringShipmentSize(Map<ShipmentSize, Long> shipmentSizeMap) {
        for (Map.Entry<ShipmentSize, Long> entry : shipmentSizeMap.entrySet()) {
            if (entry.getValue() >= SAME_SIZE_THRESHOLD) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static ShipmentSize getNextShipmentSize(ShipmentSize shipmentSize) {
        int ordinal = shipmentSize.ordinal();
        if (ordinal < ShipmentSize.values().length - 1) {
            return ShipmentSize.values()[ordinal + 1];
        } else {
            return shipmentSize;
        }
    }
}