package testAutomation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest {

    WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Set path to chromedriver executable (adjust path as needed)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\PHUONG THAO\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        // Initialize Chrome driver
        driver = new ChromeDriver();
        // Navigate to the login page
        driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/login.jsp");

        // Enter username
        WebElement usernameInput = driver.findElement(By.name("Username"));
        usernameInput.sendKeys("admin");

        // Enter password
        WebElement passwordInput = driver.findElement(By.name("Password"));
        passwordInput.sendKeys("1");

        // Submit the form
        WebElement signInButton = driver.findElement(By.cssSelector(".form-signin button[type='submit']"));
        signInButton.click();

        // Wait for login to complete (you might need to add explicit waits here)
        // Navigate to the product page and add a product to cart
        driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/home");
    }

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {
        return new Object[][] {
            {"@HoangLong", "1234567890", "123 Ha Noi", "abc", "Invalid name. Please enter a valid name without special characters."},
            {"Hoang Long", "123ABC", "123 Ha Noi", "abc", "Invalid phone number. Please enter a valid phone number without letters or special characters."},
            {"Hoang Long", "1234567890", "123 Ha Noi @City", "abc", "Invalid address. Please enter a valid address without special characters."},
            {" ", "1234567890", "123 Ha Noi", "abc", "Please fill in this field."},
            {"Hoang Long", " ", "123 Ha Noi", "abc", "Please fill in this field."},
            {"Hoang Long", "1234567890", " ", "abc", "Please fill in this field."},
            {"Hoang Long", "1234567890", "123 Ha noi", "!@#$%^&*()", ""},
            {"Hoang Long", "1234567890", "123 Ha noi", "", ""},
            {"Hoang Long", "1234567890", "123 Main St", "Leave package at front door", ""}
        };
    }

    @Test(dataProvider = "checkoutData")
    public void testCheckout(String name, String phone, String address, String note, String expectedMessage) throws InterruptedException {
        // Find and click on Add to Cart button of a specific product (replace with appropriate selector)
        WebElement addToCartButton = driver.findElement(By.cssSelector("a[href='add-to-cart?productId=19']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);

        // Wait for cart to update (you might need to add explicit waits here)
        // Navigate to the cart page
        WebElement cartButton = driver.findElement(By.cssSelector("a[href='carts']"));
        cartButton.click();

        // Click on Checkout button
        WebElement checkoutButton = driver.findElement(By.cssSelector("a[href='checkout']"));
        checkoutButton.click();

        // Wait for checkout page to load (you might need to add explicit waits here)
        // Fill in customer information
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.clear();
        nameInput.sendKeys(name);
        WebElement phoneInput = driver.findElement(By.id("phone"));
        phoneInput.clear();
        phoneInput.sendKeys(phone);
        WebElement addressInput = driver.findElement(By.id("address"));
        addressInput.clear();
        addressInput.sendKeys(address);
        WebElement noteInput = driver.findElement(By.id("note"));
        noteInput.clear();
        noteInput.sendKeys(note);

        // Submit the form (place order)
        WebElement placeOrderButton = driver.findElement(By.cssSelector(".checkout button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderButton);
        Thread.sleep(1000);

        // Check for client-side validation error messages
        boolean clientSideError = false;

        if (name.isEmpty()) {
            clientSideError = nameInput.getAttribute("validationMessage").contains("Please fill in this field.");
        } else if (phone.isEmpty()) {
            clientSideError = phoneInput.getAttribute("validationMessage").contains("Please fill in this field.");
        } else if (address.isEmpty()) {
            clientSideError = addressInput.getAttribute("validationMessage").contains("Please fill in this field.");
        }

        // Check for server-side validation error messages
        String actualMessage = "";
        try {
            WebElement errorMessage = driver.findElement(By.cssSelector(".alert-danger"));
            actualMessage = errorMessage.getText();
        } catch (Exception e) {
            // Handle the case where no error message is found
            actualMessage = "";
        }

        if (clientSideError || actualMessage.equals(expectedMessage)) {
            System.out.println("Correct error message displayed for: " + name + ", " + phone + ", " + address + ", " + note + ": " + actualMessage);
        } else {
            System.out.println("Incorrect error message. Expected: " + expectedMessage + ", but got: " + actualMessage);
        }

        Assert.assertTrue(clientSideError || actualMessage.equals(expectedMessage), "Validation message not as expected.");

        // Navigate back to the home page for the next test case
        driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/home");
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        driver.quit();
    }
}
