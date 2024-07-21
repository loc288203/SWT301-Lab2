
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditProductTest {

    public static void main(String[] args) throws InterruptedException {
        // Set path to chromedriver executable (adjust path as needed)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\PHUONG THAO\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        // Initialize Chrome driver
        WebDriver driver = new ChromeDriver();
        // Test data for different cases
        String[][] testData = {
            {"", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "13000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "", "13000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "-1200000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "12+=@#", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "13000", "", "Beautiful"},
            {"Tee Trap", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "13000", "Ao Dolce 1", "Beautiful"},
            {"Tee Trap Updated", "https://nosaucetheplug.com/cdn/shop/files/trapstar-t-shirts-x-small-trapstar-decoded-paisley-monochrome-edition-t-shirt-black-39661334659306_800x.png?v=1709584056", "15000", "Ao Dolce 2", "More Beautiful"}
        };

        // Navigate to the login page
        driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/login.jsp");

        // Enter username
        WebElement usernameInput = driver.findElement(By.name("Username"));
        usernameInput.sendKeys("admin");

        // Enter password
        WebElement passwordInput = driver.findElement(By.name("Password"));
        passwordInput.sendKeys("1");

//         Submit the form
        WebElement signInButton = driver.findElement(By.cssSelector(".form-signin button[type='submit']"));
        signInButton.click();

        // Wait for login to complete
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("home"));

        // Navigate to the product management page
        driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/manager");

        for (int i = 0; i < testData.length; i++) {
            try {
                // Find and click on the edit button of a specific product (replace with appropriate selector)
                WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='load?pid=19']"))); // Update the selector as needed
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButton);

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

                // Submit the form (update product)
                WebElement updateProductButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))); // Ensure the correct selector
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updateProductButton);
                Thread.sleep(2000); // Wait for 2 seconds to let the update process

                // Check for server-side validation error messages
                String actualMessage = "";
                try {
                    WebElement errorMessage = driver.findElement(By.cssSelector(".alert-danger"));
                    actualMessage = errorMessage.getText();
                } catch (Exception e) {
                    // Handle the case where no error message is found
                    actualMessage = "";
                }

                if (actualMessage.isEmpty()) {
                    System.out.println("Product updated successfully for test case " + (i + 1) + "!");
                } else {
                    System.out.println("Product update failed for test case " + (i + 1) + ": " + actualMessage);
                }
            } catch (Exception e) {
                System.out.println("An error occurred during test case " + (i + 1) + ": " + e.getMessage());
            } finally {
                // Navigate back to the manager page for the next test case
                driver.get("http://localhost:9999/ASSGN_PRJ_Web_Ban_Quan_Ao/manager");
                Thread.sleep(1000); // Wait for 2 seconds to let the page load
            }
        }

        // Close the browser
        driver.quit();
    }
}
