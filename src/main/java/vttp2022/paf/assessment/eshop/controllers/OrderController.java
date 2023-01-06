package vttp2022.paf.assessment.eshop.controllers;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@Controller
public class OrderController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private WarehouseService warehouseService;

	// Task 3
	@PostMapping(path = "/api/order")
	public ResponseEntity<String> processOrder(@RequestBody Order body) {

		String name = body.getName();

		try {
			// check if customer is valid
			Customer customer = customerRepository.findCustomerByName(name).get();

			// populate the model
			Order order = new Order();
			String orderId = UUID.randomUUID().toString().substring(0, 8);
			order.setOrderId(orderId);
			order.setName(name);
			order.setAddress(customer.getAddress());
			order.setEmail(customer.getEmail());
			order.setOrderDate(new Date());
			order.setLineItems(body.getLineItems());

			// save the order to the database
			try {
				orderRepository.saveOrder(order);
				OrderStatus orderStatus = warehouseService.dispatch(order);
				orderRepository.saveOrderStatus(orderStatus);
				if (null != orderStatus.getDeliveryId()) {
					System.out.println("delivery id is not null");
					JsonObject result = Json.createObjectBuilder()
							.add("orderId", orderStatus.getOrderId())
							.add("deliveryId", orderStatus.getDeliveryId())
							.add("status", orderStatus.getStatus())
							.build();
					return ResponseEntity
							.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(result.toString());
				} else {
					System.out.println("delivery id is null");
					JsonObject result = Json.createObjectBuilder()
							.add("orderId", orderStatus.getOrderId())
							.add("status", orderStatus.getStatus())
							.build();
					return ResponseEntity
							.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(result.toString());
				}
			} catch (Exception e) {
				JsonObject errObj = Json.createObjectBuilder()
						.add("error", e.getMessage())
						.build();
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.contentType(MediaType.APPLICATION_JSON)
						.body(errObj.toString());
			}
		} catch (Exception e) {
			JsonObject errObj = Json.createObjectBuilder()
					.add("error:", "Customer " + name + " not found")
					.build();
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(errObj.toString());
		}
	}

	// Task 6
	@GetMapping(path = "/api/order/{name}/status", produces = "application/json")
	public ResponseEntity<String> getOrderByCustomer(@PathVariable String name) {

		int numOfDispatched = orderRepository.getOrderStatusCount(name, "dispatched");
		int numOfPending = orderRepository.getOrderStatusCount(name, "pending");

		JsonObject jsonObj = Json.createObjectBuilder()
				.add("name:", name)
				.add("dispatched:", numOfDispatched)
				.add("pending:", numOfPending)
				.build();

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(jsonObj.toString());
	}

	// @GetMapping(path = "/customer/{name}")
	// public ResponseEntity<String> checkIfCustomerExists(@PathVariable String
	// name) {
	// try {
	// Optional<Customer> ops = customerRepository.findCustomerByName(name);
	// Customer customer = ops.get();

	// JsonObject jsonObj = Json.createObjectBuilder()
	// .add("name:", customer.getName())
	// .add("address:", customer.getAddress())
	// .add("email:", customer.getEmail())
	// .build();
	// return ResponseEntity
	// .status(HttpStatus.OK)
	// .contentType(MediaType.APPLICATION_JSON)
	// .body(jsonObj.toString());

	// } catch (Exception e) {
	// JsonObject jsonObj = Json.createObjectBuilder()
	// .add("error:", "Customer " + name + " not found")
	// .build();
	// return ResponseEntity
	// .status(HttpStatus.NOT_FOUND)
	// .contentType(MediaType.APPLICATION_JSON)
	// .body(jsonObj.toString());
	// }
	// }
}
