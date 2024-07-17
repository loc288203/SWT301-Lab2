package dal;

import java.util.List;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for OrderDBcontext class.
 */
public class OrderDBcontextTest {
    
    private OrderDBcontext dbContext;

    @Before
    public void setUp() {
        dbContext = new OrderDBcontext();
        assertNotNull("Database connection should be established", dbContext.connection);
        assertTrue("Database connection should be valid", isConnectionValid());
    }
    
    private boolean isConnectionValid() {
        try {
            return dbContext.connection != null && !dbContext.connection.isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void testMain() {
        // This test doesn't do anything. You might consider removing it.
        // However, for the sake of completeness, let's make sure it doesn't cause any exceptions.
        try {
            OrderDBcontext.main(new String[]{});
        } catch (Exception e) {
            fail("Main method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateReturnId() {
        // Arrange
        Order order = new Order();
        order.setAccountId(1); // Ensure this account exists in your test database
        order.setTotalPrice(100.0);
        order.setNote("Test order");
        order.setShippingId(1); // Ensure this shipping id exists in your test database
        
        // Act
        int generatedId = dbContext.createReturnId(order);
        
        // Assert
        System.out.println("Generated ID: " + generatedId);
        assertTrue("The generated ID should be greater than 0, but was: " + generatedId, generatedId > 0);
    }

    @Test
    public void testGetAllOrder() {
        // Arrange
        // Ensure there is at least one order in the test database
        
        // Act
        List<Order> orders = dbContext.getAllOrder();
        
        // Assert
        assertNotNull("The order list should not be null", orders);
        assertTrue("The order list should contain orders", orders.size() > 0);
    }
}   