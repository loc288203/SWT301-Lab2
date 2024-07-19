import dal.OrderDBcontext;
import dal.OrderDetailDBcontext;
import dal.ShippingDBcontext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;
import model.Order;
import model.Shipping;

@WebServlet(name = "CheckOutController", urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            Map<Integer, Cart> carts = (Map<Integer, Cart>) session.getAttribute("carts");
            if (carts == null) {
                carts = new LinkedHashMap<>();
            }

            // Calculate total money
            double totalMoney = 0;
            for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
                Integer productId = entry.getKey();
                Cart cart = entry.getValue();
                totalMoney += cart.getQuantity() * cart.getProduct().getPrice();
            }
            request.setAttribute("totalMoney", totalMoney);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String note = request.getParameter("note");

         // Validate name
    if (name == null || name.trim().isEmpty()) {
        request.setAttribute("error", "Please fill in this field.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    } else if (!Pattern.matches("^[a-zA-Z\\s]+$", name)) {
        request.setAttribute("error", "Invalid name. Please enter a valid name without special characters.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    }

    // Validate phone
    if (phone == null || phone.trim().isEmpty()) {
        request.setAttribute("error", "Please fill in this field.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    } else if (!Pattern.matches("^[0-9]+$", phone)) {
        request.setAttribute("error", "Invalid phone number. Please enter a valid phone number without letters or special characters.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    }

    // Validate address
    if (address == null || address.trim().isEmpty()) {
        request.setAttribute("error", "Please fill in this field.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    } else if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", address)) {
        request.setAttribute("error", "Invalid address. Please enter a valid address without special characters.");
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
        return;
    }

        // Save to database
        // Save Shipping
        Shipping shipping = new Shipping(name, phone, address);
        int shippingId = new ShippingDBcontext().createReturnId(shipping); // Return the auto-incremented ID of the record just saved in the database

        // Save Order
        HttpSession session = request.getSession();
        Map<Integer, Cart> carts = (Map<Integer, Cart>) session.getAttribute("carts");
        if (carts == null) {
            carts = new LinkedHashMap<>();
        }

        // Calculate total price
        double totalPrice = 0;
        for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
            Integer productId = entry.getKey();
            Cart cart = entry.getValue();
            totalPrice += cart.getQuantity() * cart.getProduct().getPrice();
        }

        Account a = (Account) request.getSession().getAttribute("acc");
        Order order = new Order(a.getUid(), totalPrice, note, shippingId);
        int orderId = new OrderDBcontext().createReturnId(order);
        
        // Save OrderDetail
        new OrderDetailDBcontext().saveCart(orderId, carts);

        session.removeAttribute("carts");
        request.setAttribute("cartss", carts);
        request.setAttribute("totalPrice", totalPrice);
        request.getRequestDispatcher("thanks.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
