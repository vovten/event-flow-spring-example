package io.github.vovten.eventflow.example;

/**
 * Represents an order in the system.
 * Contains order details including customer information, product, quantity, and pricing.
 */
public class Order {
    private Long id;
    private String customerName;
    private String product;
    private int quantity;
    private double price;

    public Order() {
    }

    public Order(Long id, String customerName, String product, int quantity, double price) {
        this.id = id;
        this.customerName = customerName;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalAmount() {
        return quantity * price;
    }
}
