package com.trendyol.shipment;

import java.util.Comparator;
public enum ShipmentSize {

    SMALL(0),
    MEDIUM(1),
    LARGE(2),
    X_LARGE(3);

    public final Integer size;

    ShipmentSize(int size) {
        this.size = size;
    }

    public static final Comparator<ShipmentSize> shipmentSizeComparator = new Comparator<ShipmentSize>() {
        @Override
        public int compare(ShipmentSize o1, ShipmentSize o2) {
            return o1.size - o2.size;
        }
    };
}
