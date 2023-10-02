package com.trendyol.shipment;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Basket {
    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (getProducts().isEmpty()) {
            throw new IllegalStateException("Basket is empty.");
        }

        Map<ShipmentSize, Long> sizeCounts = getProducts().stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));

        Optional<Map.Entry<ShipmentSize, Long>> maxEntry = sizeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        ShipmentSize maxShipmentSize = maxEntry.map(Map.Entry::getKey)
                .orElse(ShipmentSize.SMALL);

        return maxEntry
                .filter(entry -> entry.getValue() >= 3)
                .map(entry -> determineSizeOneStepUp(maxShipmentSize))
                .orElse(determineLargestShipmentSize());
    }

    private ShipmentSize determineLargestShipmentSize() {
        return getProducts().stream()
                .map(Product::getSize)
                .max(Comparator.naturalOrder())
                .orElse(ShipmentSize.SMALL);
    }

    private ShipmentSize determineSizeOneStepUp(ShipmentSize maxShipmentSize) {
        return maxShipmentSize == ShipmentSize.SMALL
                ? ShipmentSize.MEDIUM
                : (maxShipmentSize == ShipmentSize.MEDIUM
                ? ShipmentSize.LARGE
                : ShipmentSize.X_LARGE);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

