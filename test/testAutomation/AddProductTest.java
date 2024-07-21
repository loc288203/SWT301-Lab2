
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class AddProductTest {

    public static void main(String[] args) throws InterruptedException {
        // Set path to chromedriver executable (adjust path as needed)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\PHUONG THAO\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Initialize Chrome driver
        WebDriver driver = new ChromeDriver();

        // Define an explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Test data for different cases
        String[][] testData = {
            {"", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "13000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "-1200000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "123456", "Good material", "13000", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "12+=@#", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "30 000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "30 000", "", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "30 000", "Ao Dolce 1", ""},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "1300000", "Ao Dolce 1", "Beautiful"}
        };

        try {// Navigate to the login page
            driver.get("http://localhost:8080/ASSGN_PRJ_Web_Ban_Quan_Ao/login.jsp");

            // Enter username
            WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("Username")));
            usernameInput.sendKeys("admin");

            // Enter password
            WebElement passwordInput = driver.findElement(By.name("Password"));
            passwordInput.sendKeys("1");

            // Submit the form
            WebElement signInButton = driver.findElement(By.cssSelector(".form-signin button[type='submit']"));
            signInButton.click();

            // Wait for login to complete
            wait.until(ExpectedConditions.urlContains("home"));

            // Navigate to the product page
            driver.get("http://localhost:8080/ASSGN_PRJ_Web_Ban_Quan_Ao/home");

            for (int i = 0; i < testData.length; i++) {
                // Find and click on Add to Cart button of a specific product (replace with appropriate selector)
                WebElement clickmanager = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='manager']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickmanager);

                // Navigate to the add product modal
                WebElement addProductModalButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='#addEmployeeModal']")));
                addProductModalButton.click();

                // Fill in product information
                WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
                nameInput.clear();
                nameInput.sendKeys(testData[i][0]);

                WebElement imageInput = driver.findElement(By.name("image"));
                imageInput.clear();
                imageInput.sendKeys(testData[i][1]);

                WebElement priceInput = driver.findElement(By.name("price"));
                priceInput.clear();
                priceInput.sendKeys(testData[i][2]);

                WebElement titleInput = driver.findElement(By.name("title"));
                titleInput.clear();
                titleInput.sendKeys(testData[i][3]);

                WebElement descriptionInput = driver.findElement(By.name("description"));
                descriptionInput.clear();
                descriptionInput.sendKeys(testData[i][4]);

                // Store the current URL before placing the order
                String initialUrl = driver.getCurrentUrl();

                // Submit the form (add product)
                WebElement addProductButton = driver.findElement(By.cssSelector("[value='Add']"));
                addProductButton.click();

                // Verify if the product was added successfully (you can add assertions or checks here)
                try {
                    wait.until(ExpectedConditions.urlToBe(initialUrl));
                    if (driver.getCurrentUrl().equals(initialUrl)) {
                        System.out.println("Product added successfully for test case " + (i + 1) + "!");
                    } else {
                        System.out.println("Product addition failed for test case " + (i + 1) + "!");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to verify the URL for test case " + (i + 1) + ": " + e.getMessage());
                }

                // Navigate back to the home page for the next test case
                driver.get("http://localhost:8080/ASSGN_PRJ_Web_Ban_Quan_Ao/home");
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
