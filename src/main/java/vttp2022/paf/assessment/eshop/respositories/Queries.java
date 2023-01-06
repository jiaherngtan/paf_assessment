package vttp2022.paf.assessment.eshop.respositories;

public class Queries {

    public static final String SQL_GET_CUSTOMER_BY_NAME = "SELECT * FROM customers WHERE name = ?";

    public static final String SQL_INSERT_ORDER = "INSERT INTO `order`(order_id,name,item,quantity) VALUES(?, ?, ?, ?)";

    public static final String SQL_GET_ORDER_BY_ID = "SELECT * FROM `order` JOIN customers ON `order`.name = customers.name WHERE order_id = ?";

    public static final String SQL_INSERT_ORDER_STATUS = "INSERT INTO order_status(order_id,delivery_id,status,status_update) VALUES(?, ?, ?, ?)";

    public static final String SQL_CHECK_STATUS_COUNT_BY_USER = "SELECT COUNT(*) FROM order_status JOIN `order` ON order_status.order_id = `order`.order_id WHERE name = ? AND status = ?";

}
