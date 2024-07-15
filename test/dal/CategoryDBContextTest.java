package dal;

import model.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryDBContextTest {
    
    private CategoryDBContext categoryDBContext;

    public CategoryDBContextTest() {
    }

    @Before
    public void setUp() {
        categoryDBContext = new CategoryDBContext();
        // Optionally, set up initial database state if needed
    }

    @After
    public void tearDown() {
        // Optionally, clean up resources or reset database state
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = categoryDBContext.getAllCategories();
        assertNotNull("Category list should not be null", categories);
        assertTrue("Category list should not be empty", categories.size() > 0);
    }

    @Test
public void testInsertCategory() {
    String categoryName = "New Category";
    int newCategoryId = categoryDBContext.insertCategory(categoryName);

    // Fetch the category back to verify insertion
    Category fetchedCategory = categoryDBContext.getCategoryById(newCategoryId);
    assertNotNull("Fetched category should not be null", fetchedCategory);
    assertEquals("Category name should match", categoryName, fetchedCategory.getCname());
}


    @Test
    public void testGetCategoryById() {
        int categoryId = 1; // Use an existing ID from your database
        Category category = categoryDBContext.getCategoryById(categoryId);
        assertNotNull("Category should not be null", category);
        assertEquals("Category ID should match", categoryId, category.getCid());
    }

    @Test
    public void testUpdateCategory() {
        int categoryId = 1; // Use an existing ID
        Category category = categoryDBContext.getCategoryById(categoryId);
        assertNotNull("Category should not be null before update", category);
        
        String newName = "Updated Category";
        category.setCname(newName);
        categoryDBContext.updateCategory(category);

        Category updatedCategory = categoryDBContext.getCategoryById(categoryId);
        assertEquals("Updated category name should match", newName, updatedCategory.getCname());
    }
}
