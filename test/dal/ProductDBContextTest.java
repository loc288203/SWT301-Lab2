/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package dal;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import dal.ProductDBContext;
import model.Product;

public class ProductDBContextTest {
    
    private ProductDBContext productDB;
    
    @Before
    public void setUp() throws Exception {
        productDB = new ProductDBContext();
    }
    
    @After
    public void tearDown() throws Exception {
        productDB = null;
    }
    
    @Test
    public void testGetAllProducts() {
        List<Product> products = productDB.getAllProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void testGetProductsByCategoryId() {
        int categoryId = 1; // Adjust as needed for your test data
        List<Product> products = productDB.getProductsByCategoryId(categoryId);
        assertNotNull(products);
        assertTrue(products.size() > 0);
        for (Product product : products) {
            assertEquals(categoryId, product.getCategoryId());
        }
    }
    
    @Test
    public void testGetProductById() {
        int productId = 1; // Adjust as needed for your test data
        Product product = productDB.getProductById(productId);
        assertNotNull(product);
        assertEquals(productId, product.getId());
    }
    
    @Test
    public void testSearch() {
        String keyword = "test"; // Adjust as needed for your test data
        List<Product> products = productDB.search(keyword);
        assertNotNull(products);
        assertTrue(products.size() > 0);
        for (Product product : products) {
            assertTrue(product.getName().contains(keyword));
        }
    }
    
    @Test
    public void testGetTotalProducts() {
        int totalProducts = productDB.getTotalProducts();
        assertTrue(totalProducts > 0);
    }
    
    @Test
    public void testGetProductsWithPagging() {
        int page = 1;
        int pageSize = 5;
        List<Product> products = productDB.getProductsWithPagging(page, pageSize);
        assertNotNull(products);
        assertTrue(products.size() <= pageSize);
    }

    @Test
    public void testInsertProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setImageUrl("test.jpg");
        product.setPrice(9.99);
        product.setTiltle("Test Title");
        product.setDescription("Test Description");
        product.setCategoryId(1);
        product.setSell_ID(1);
        
        productDB.inSertProduct(product);
        
        List<Product> products = productDB.search("Test Product");
        assertTrue(products.size() > 0);
        boolean found = false;
        for (Product p : products) {
            if (p.getName().equals("Test Product")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testUpdateProduct() {
        int productId = 1; // Adjust as needed for your test data
        Product product = productDB.getProductById(productId);
        assertNotNull(product);
        
        product.setName("Updated Product");
        productDB.updateProduct(product);
        
        Product updatedProduct = productDB.getProductById(productId);
        assertEquals("Updated Product", updatedProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        int productId = 1; // Adjust as needed for your test data
        productDB.deleteProduct(productId);
        
        Product product = productDB.getProductById(productId);
        assertNull(product);
    }

    @Test
    public void testGetProductsBySellId() {
        int sellId = 1; // Adjust as needed for your test data
        List<Product> products = productDB.getProductsBySellId(sellId);
        assertNotNull(products);
        assertTrue(products.size() > 0);
        for (Product product : products) {
            assertEquals(sellId, product.getSell_ID());
        }
    }

    @Test
    public void testGetAllProductsLast() {
        List<Product> products = productDB.getAllProductsLast();
        assertNotNull(products);
        assertTrue(products.size() > 0);
        assertTrue(products.size() <= 4);
    }
}
