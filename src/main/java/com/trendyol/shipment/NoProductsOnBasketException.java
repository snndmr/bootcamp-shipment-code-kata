package com.trendyol.shipment;

public class NoProductsOnBasketException extends RuntimeException {
    private static final String NO_PRODUCTS_ON_BASKET = "Basket is empty.";

    public NoProductsOnBasketException() {
        super(NO_PRODUCTS_ON_BASKET);
    }
}