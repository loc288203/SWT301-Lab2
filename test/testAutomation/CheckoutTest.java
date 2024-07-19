    import org.openqa.selenium.By;
    import org.openqa.selenium.JavascriptExecutor;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.chrome.ChromeDriver;

    public class CheckoutTest {

        public static void main(String[] args) throws InterruptedException {
            // Set path to chromedriver executable (adjust path as needed)
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\PHUONG THAO\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            // Initialize Chrome driver
            WebDriver driver = new ChromeDriver();
            // Test data for different cases
            String[][] testData = {
                {"@HoangLong", "1234567890", "123 Ha Noi", "abc"},
                {"Hoang Long", "123ABC", "123 Ha Noi", "abc"},
                {"Hoang Long", "1234567890", "123 Ha Noi @City", "abc"},
                {" ", "1234567890", "123 Ha Noi", "abc"},
                {"Hoang Long", " ", "123 Ha Noi", "abc"},
                {"Hoang Long", "1234567890", " ", "abc"},
                {"Hoang Long", "1234567890", "123 Ha noi", "!@#$%^&*()"},
                {"Hoang Long", "1234567890", "123 Ha noi", ""},
                {"Hoang Long", "1234567890", "123 Main St", "Leave package at front door"}
            };
            // Expected messages for each test case
            String[] expectedMessages = {
                "Invalid name. Please enter a valid name without special characters.",
                "Invalid phone number. Please enter a valid phone number without letters or special characters.",
                "Invalid address. Please enter a valid address without special characters.",
                "Please fill in this field.",
                "Please fill in this field.",
                "Please fill in this field.",
                "", // No error expected for special characters in note
                "", // No error expected for empty note
                "" // No error expected for valid data
            };
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
            for (int i = 0; i < testData.length; i++) {
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
                nameInput.sendKeys(testData[i][0]);
                WebElement phoneInput = driver.findElement(By.id("phone"));
                phoneInput.clear();
                phoneInput.sendKeys(testData[i][1]);
                WebElement addressInput = driver.findElement(By.id("address"));
                addressInput.clear();
                addressInput.sendKeys(testData[i][2]);
                WebElement noteInput = driver.findElement(By.id("note"));
                noteInput.clear();
                noteInput.sendKeys(testData[i][3]);

                // Submit the form (place order)
                WebElement placeOrderButton = driver.findElement(By.cssSelector(".checkout button[type='submit']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderButton);
                Thread.sleep(1000);

                // Check for client-side validation error messages
                boolean clientSideError = false;

                if (testData[i][0].isEmpty()) {
                    clientSideError = nameInput.getAttribute("validationMessage").contains("Please fill in this field.");
                } else if (testData[i][1].isEmpty()) {
                    clientSideError = phoneInput.getAttribute("validationMessage").contains("Please fill in this field.");
                } else if (testData[i][2].isEmpty()) {
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

                if (clientSideError || actualMessage.equals(expectedMessages[i])) {
                    System.out.println("Correct error message displayed for test case " + (i + 1) + ": " + actualMessage);
                } else {
                    System.out.println("Incorrect error message for test case " + (i + 1) + ". Expected: " + expectedMessages[i] + ", but got: " + actualMessage);
                }

                // Navigate back to the home page for the next test case
                driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/home");
            }

            // Close the browser
            driver.quit();
        }
    }
