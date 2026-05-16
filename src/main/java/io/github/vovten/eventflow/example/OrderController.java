package io.github.vovten.eventflow.example;

import com.custom.BroadCastOrderCreatedEvent;
import io.github.vovten.eventflow.channel.BroadcastEventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.example.event.EnvelopeDomainOrderEvent;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.OrderCreatedEvent;
import io.github.vovten.eventflow.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * REST controller for managing order operations.
 * Provides endpoints to create orders and publish corresponding events.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private EventPublisher eventPublisher;

    private final AtomicLong orderCounter = new AtomicLong(1);

    /**
     * Creates a new order and publishes a broadcast event.
     *
     * @param request the order request containing customer name, product, quantity, and price
     * @return the created order with generated ID
     */
    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = new Order(
                orderCounter.getAndIncrement(),
                request.getCustomerName(),
                request.getProduct(),
                request.getQuantity(),
                request.getPrice()
        );
        eventPublisher.publish(new OrderCreatedEvent(order.getId()));

//        eventPublisher.prepare(new OrderCreatedEvent(1))
//                .withProcessId(UUID.randomUUID())
//                .publish();
//
//        eventPublisher.publish(new InternalOrderCreatedEvent(order.getId())); // try different type of events here
//        eventPublisher.publish(new ExternalOrderCreatedEvent(order.getId())); // try different type of events here
//        eventPublisher.publish(new BroadCastOrderCreatedEvent(order.getId())); // try different type of events here
        return ResponseEntity.ok(order);
    }

    /**
     * Request DTO for creating an order.
     * Contains customer name, product, quantity, and price information.
     */
    public static class OrderRequest {
        private String customerName;
        private String product;
        private int quantity;
        private double price;

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
    }
}
