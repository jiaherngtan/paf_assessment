package vttp2022.paf.assessment.eshop.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

@Service
public class WarehouseService {

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {

		// Task 4
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (LineItem li : order.getLineItems()) {
			JsonObjectBuilder objBuilder = Json.createObjectBuilder();
			objBuilder.add("item", li.getItem());
			objBuilder.add("quantity", li.getQuantity());
			arrBuilder.add(objBuilder);
		}
		JsonArray lineItemsObj = arrBuilder.build();

		JsonObjectBuilder objBuilder = Json.createObjectBuilder();
		objBuilder.add("orderId", defaultValue(order.getOrderId(), "null"));
		objBuilder.add("name", defaultValue(order.getName(), "null"));
		objBuilder.add("address", defaultValue(order.getAddress(), "null"));
		objBuilder.add("email", defaultValue(order.getEmail(), "null"));
		objBuilder.add("lineItems", defaultValue(lineItemsObj, null));
		objBuilder.add("createdBy", "Tan Jia Herng");

		String url = "http://paf.chuklee.com/dispatch/" + order.getOrderId();

		RestTemplate restTemplate = new RestTemplate();

		String requestJson = objBuilder.build().toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		String response = restTemplate.postForObject(url, entity, String.class);
		System.out.println("response: " + response);

		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(order.getOrderId());
		orderStatus.setStatus("pending");

		if (response.contains("\"deliveryId\"")) {
			String[] str = response.split("\\s+");
			String deliveryId = str[3].replaceAll("[\"}]", "");
			System.out.println(deliveryId);
			orderStatus.setDeliveryId(deliveryId);
			orderStatus.setStatus("dispatched");
		}

		return orderStatus;
	}

	public <T> T defaultValue(T actualVal, T defaultVal) {
		if (null == actualVal)
			return defaultVal;
		return actualVal;
	}
}
