package com.trendyol.shipment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {

    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (isEmptyBasket()) {
            return null;
        }

        Map<ShipmentSize, Long> shipmentSizesCount = countShipmentSizes();

        ShipmentSize largestSize = getLargestShipmentSize(shipmentSizesCount);

        return determineShipmentSize(shipmentSizesCount, largestSize);
    }

    private boolean isEmptyBasket() {
        return products == null || products.isEmpty();
    }

    private Map<ShipmentSize, Long> countShipmentSizes() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));
    }

    private ShipmentSize getLargestShipmentSize(Map<ShipmentSize, Long> shipmentSizesCount) {
        return shipmentSizesCount.keySet().stream()
                .max(ShipmentSize::compareTo)
                .orElse(null);
    }

    private ShipmentSize determineShipmentSize(Map<ShipmentSize, Long> shipmentSizesCount, ShipmentSize largestSize) {
        for (Map.Entry<ShipmentSize, Long> entry : shipmentSizesCount.entrySet()) {
            if (entry.getValue() >= 3) {
                return entry.getKey() == ShipmentSize.X_LARGE
                        ? ShipmentSize.X_LARGE
                        : getNextShipmentSize(entry.getKey());
            }
        }

        if (products.size() < 3) {
            return largestSize;
        }

        return largestSize;
    }

    private ShipmentSize getNextShipmentSize(ShipmentSize size) {
        switch (size) {
            case SMALL:
                return ShipmentSize.MEDIUM;
            case MEDIUM:
                return ShipmentSize.LARGE;
            case LARGE:
                return ShipmentSize.X_LARGE;
            default:
                return ShipmentSize.X_LARGE;
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
