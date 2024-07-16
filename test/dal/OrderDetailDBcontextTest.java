package dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Cart;
import model.OrderDetail;
import org.junit.Test;
import static org.junit.Assert.*;

public class OrderDetailDBcontextTest {
    
    public OrderDetailDBcontextTest() {
    }

    @Test
    public void testSaveCart() {
        OrderDetailDBcontext db = new OrderDetailDBcontext();
        Map<Integer, Cart> carts = new HashMap<>();
        // Add test data to carts
        // Example: carts.put(1, new Cart(new Product("Product1", "Image1", 100.0), 2));
        db.saveCart(1, carts);
        List<OrderDetail> orderDetails = db.getAllOrderDetailById(1);
        assertFalse(orderDetails.isEmpty());
    }

    @Test
    public void testGetAllOrderDetailById() {
        OrderDetailDBcontext db = new OrderDetailDBcontext();
        List<OrderDetail> orderDetails = db.getAllOrderDetailById(1);
        assertNotNull(orderDetails);
        // Add more assertions based on expected data
    }

    @Test
    public void testDelete() {
        OrderDetailDBcontext db = new OrderDetailDBcontext();
        db.delete(1);
        List<OrderDetail> orderDetails = db.getAllOrderDetailById(1);
        assertTrue(orderDetails.isEmpty());
    }

    @Test
    public void testMain() {
        OrderDetailDBcontext.main(new String[]{});
        // Add assertions if necessary
    }
}
