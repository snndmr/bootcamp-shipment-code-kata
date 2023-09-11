package com.trendyol.shipment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket {

    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (products == null || products.isEmpty()) {
            return null;
        }

        Map<ShipmentSize, Long> shipmentSizeMap = new HashMap<>();

        for (Product product : products) {
            ShipmentSize size = product.getSize();
            shipmentSizeMap.put(size, shipmentSizeMap.getOrDefault(size, 0L) + 1);
        }

        if (products.size() < 3) {
            return Collections.max(shipmentSizeMap.keySet());
        }

        ShipmentSize maxShipmentSize = null;

        for (Map.Entry<ShipmentSize, Long> entry : shipmentSizeMap.entrySet()) {
            if (entry.getValue() >= 3) {
                maxShipmentSize = entry.getKey();
                break;
            }
        }

        if (maxShipmentSize != null) {
            int ordinal = maxShipmentSize.ordinal();
            if (ordinal < ShipmentSize.values().length - 1) {
                return ShipmentSize.values()[ordinal + 1];
            } else {
                return maxShipmentSize;
            }
        } else {
            return Collections.max(shipmentSizeMap.keySet());
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
