package io.github.vovten.eventflow.example;

import com.custom.BroadCastOrderCreatedEvent;
import io.github.vovten.eventflow.event.Envelope;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.example.event.DomainOrderEvent;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import io.github.vovten.eventflow.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * Creates a new order and publishes an event.
     * <p>
     * The {@code scenario} parameter switches between different event/publish combinations
     * for testing the event-flow library (especially the EventKeyResolver feature).
     *
     * @param request  the order request containing customer name, product, quantity, and price
     * @param scenario publish scenario: default, null-id, raw-external, domain, domain-legacy,
     *                 broadcast, internal, internal-prepare, envelope-domain
     * @return the created order with generated ID
     */
    @Transactional
    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request,
                                             @RequestParam(required = false, defaultValue = "default") String scenario) {
        Order order = new Order(
                orderCounter.getAndIncrement(),
                request.getCustomerName(),
                request.getProduct(),
                request.getQuantity(),
                request.getPrice()
        );
        UUID processId = UUID.randomUUID();
        switch (scenario) {
            case "default" -> eventPublisher.prepare(new ExternalOrderCreatedEvent(order.getId()))
                    .withProcessId(processId)
                    .withMetadata("product-id", request.getProduct())
                    .publish();
            case "null-id" -> eventPublisher.prepare(new ExternalOrderCreatedEvent(null))
                    .withProcessId(processId)
                    .publish();
            case "raw-external" -> eventPublisher.publish(new ExternalOrderCreatedEvent(order.getId()));
            case "domain" -> eventPublisher.publish(new DomainOrderEvent(order.getId()));
            case "domain-legacy" -> eventPublisher.publish(new io.github.vovten.eventflow.example.DomainOrderEvent(order.getId() + 1));
            case "broadcast" -> eventPublisher.publish(new BroadCastOrderCreatedEvent(order.getId()));
            case "internal" -> eventPublisher.publish(new InternalOrderCreatedEvent(order.getId()));
            case "internal-prepare" -> eventPublisher.prepare(new InternalOrderCreatedEvent(order.getId())).publish();
            case "envelope-domain" -> eventPublisher.publish(new io.github.vovten.eventflow.example.event.EnvelopeDomainOrderEvent(order.getId()));
            default -> throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
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