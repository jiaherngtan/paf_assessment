package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Optional<Customer> findCustomerByName(String name) {
		// Task 3
		try {
			final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_CUSTOMER_BY_NAME, name);
			Customer customer = new Customer();
			while (rs.next()) {
				customer.setName(name);
				customer.setAddress(rs.getString("address"));
				customer.setEmail(rs.getString("email"));
			}
			return Optional.of(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

}
