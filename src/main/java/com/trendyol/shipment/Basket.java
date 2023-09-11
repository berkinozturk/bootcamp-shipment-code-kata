package com.trendyol.shipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {
    private List<Product> products = new ArrayList<>();
    private static final Integer MINIMUM_SAME_SIZE_PRODUCT_COUNT = 3;
    private static final Integer MINIMUM_PRODUCT_COUNT_FOR_LARGEST_SHIPMENT = 3;

    public ShipmentSize getShipmentSize() {
        if (isEmptyBasket()) {
            return null;
        }

        Map<ShipmentSize, Long> productCountByShipmentSizeMap = getProductCountByShipmentSize();

        return determineShipmentSize(productCountByShipmentSizeMap);
    }

    private boolean isEmptyBasket() {
        return products.isEmpty();
    }

    private Map<ShipmentSize, Long> getProductCountByShipmentSize() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));
    }

    private ShipmentSize getLargestShipmentSize(Map<ShipmentSize, Long> shipmentSizesCount) {
        return shipmentSizesCount.keySet().stream()
                .max(ShipmentSize.shipmentSizeComparator)
                .orElse(null);
    }

    private ShipmentSize determineShipmentSize(
            Map<ShipmentSize, Long> productCountByShipmentSizeMap
    ) {
        ShipmentSize largestSize = getLargestShipmentSize(productCountByShipmentSizeMap);

        if (products.size() < MINIMUM_PRODUCT_COUNT_FOR_LARGEST_SHIPMENT) {
            return largestSize;
        }

        for (Map.Entry<ShipmentSize, Long> entry : productCountByShipmentSizeMap.entrySet()) {
            if (entry.getValue() >= MINIMUM_SAME_SIZE_PRODUCT_COUNT) {
                return entry.getKey() == ShipmentSize.X_LARGE
                        ? ShipmentSize.X_LARGE
                        : getNextShipmentSize(entry.getKey());
            }
        }
        return largestSize;
    }

    private ShipmentSize getNextShipmentSize(ShipmentSize size) {
        return switch (size) {
            case SMALL -> ShipmentSize.MEDIUM;
            case MEDIUM -> ShipmentSize.LARGE;
            default -> ShipmentSize.X_LARGE;
        };
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
