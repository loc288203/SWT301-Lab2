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

    // Test inserting duplicate category
    Exception duplicateException = assertThrows(RuntimeException.class, () -> {
        categoryDBContext.insertCategory(categoryName);
    });
    String expectedDuplicateMessage = "Category 'New Category' already exists";
    assertTrue("Expected exception message should match", duplicateException.getMessage().contains(expectedDuplicateMessage));

    // Test inserting category with null name
    Exception nullNameException = assertThrows(IllegalArgumentException.class, () -> {
        categoryDBContext.insertCategory(null);
    });
    String expectedNullMessage = "Category name cannot be null";
    assertTrue("Expected exception message should match", nullNameException.getMessage().contains(expectedNullMessage));
}


    @Test
    public void testGetCategoryById() {
        // Case 1: Category Exists
        int categoryId = 1; // Use an existing ID from your database
        Category category = categoryDBContext.getCategoryById(categoryId);
        assertNotNull("Category should not be null", category);
        assertEquals("Category ID should match", categoryId, category.getCid());

        // Case 2: Category Does Not Exist
        categoryId = 999; // Use a non-existing ID from your database
        category = categoryDBContext.getCategoryById(categoryId);
        assertNull("Category should be null", category);

        // Case 3: Category ID is Zero (Boundary Case)
        try {
            categoryId = 0;
            categoryDBContext.getCategoryById(categoryId);
            fail("Expected an IllegalArgumentException to be thrown for ID zero");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Case 4: Category ID is Negative (Boundary Case)
        try {
            categoryId = -1;
            categoryDBContext.getCategoryById(categoryId);
            fail("Expected an IllegalArgumentException to be thrown for negative ID");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testUpdateCategory() {
        // Case 1: Update Category Successfully
        int categoryId = 1; // Use an existing ID
        Category category = categoryDBContext.getCategoryById(categoryId);
        assertNotNull("Category should not be null before update", category);

        String newName = "Updated Category";
        category.setCname(newName);
        categoryDBContext.updateCategory(category);

        Category updatedCategory = categoryDBContext.getCategoryById(categoryId);
        assertEquals("Updated category name should match", newName, updatedCategory.getCname());

        // Case 2: Update Category with Empty Name
        try {
            category.setCname("");
            categoryDBContext.updateCategory(category);
            fail("Expected an IllegalArgumentException to be thrown for empty name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Case 3: Update Non-Existing Category
        try {
            categoryId = 999; // Use a non-existing ID
            category = categoryDBContext.getCategoryById(categoryId);
            assertNull("Category should be null", category);
            categoryDBContext.updateCategory(category);
            fail("Expected an IllegalArgumentException to be thrown for non-existing category");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Case 4: Update Category with Null Name
        try {
            categoryId = 1; // Use an existing ID again
            category = categoryDBContext.getCategoryById(categoryId);
            assertNotNull("Category should not be null before update", category);
            category.setCname(null);
            categoryDBContext.updateCategory(category);
            fail("Expected an IllegalArgumentException to be thrown for null name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

    }
}
