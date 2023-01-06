package vttp2022.paf.assessment.eshop.respositories;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.services.OrderException;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Task 3
	@Transactional(rollbackFor = OrderException.class)
	public void saveOrder(Order order) throws OrderException {

		List<LineItem> lineItems = order.getLineItems();

		List<Object[]> data = lineItems.stream()
				.map(li -> {
					Object[] o = new Object[4];
					o[0] = order.getOrderId();
					o[1] = order.getName();
					o[2] = li.getItem();
					o[3] = li.getQuantity();
					return o;
				}).toList();

		jdbcTemplate.batchUpdate(SQL_INSERT_ORDER, data);
	}

	public Optional<Order> findOrderByOrderId(String orderId) {

		try {
			Order order = new Order();
			List<LineItem> lineItems = new LinkedList<>();
			final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_ORDER_BY_ID, orderId);
			while (rs.next()) {
				// to redo line 52-55
				order.setName(rs.getString("name"));
				order.setAddress(rs.getString("address"));
				order.setEmail(rs.getString("email"));
				LineItem li = new LineItem();
				li.setItem(rs.getString("item"));
				li.setQuantity(rs.getInt("quantity"));
				lineItems.add(li);
			}
			order.setOrderId(orderId);
			order.setLineItems(lineItems);
			return Optional.of(order);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void saveOrderStatus(OrderStatus orderStatus) {
		jdbcTemplate.update(SQL_INSERT_ORDER_STATUS,
				orderStatus.getOrderId(),
				orderStatus.getDeliveryId(),
				orderStatus.getStatus(),
				new Date());
	}

	public Integer getOrderStatusCount(String name, String status) {
		return jdbcTemplate.queryForObject(
				SQL_CHECK_STATUS_COUNT_BY_USER, Integer.class, name, status);
	}
}
