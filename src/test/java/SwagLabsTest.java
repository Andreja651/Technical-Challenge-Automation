import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class SwagLabsTest {

    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        // Set preferences to ignore password manager prompts in Chrome
        final Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        // Initialize the ChromeDriver with the specified options
        driver = new ChromeDriver(options);

        // Navigate to the Sauce Demo login page
        driver.get("https://www.saucedemo.com/");

        // Maximize the browser window and perform login
        driver.manage().window().maximize();
        driver.findElement(By.id("user-name")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.id("login-button")).click();
    }

    @AfterClass
    public void tearDown() {
        // Close the browser after tests are completed
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void givenUsernameAndPassword_whenCheckout_thenSuccess() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");

        // Maximize the browser window and perform login
        driver.manage().window().maximize();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the page is fully loaded
        driver.findElement(By.id("user-name")).sendKeys(USERNAME);
        Thread.sleep(1000); // Wait for 1 second before entering the password
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        Thread.sleep(1000); // Wait for 1 second before clicking the login button
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the login is processed

        // Randomly select 3 products to add to the cart
        List<String> products = new ArrayList<>();
        products.add("add-to-cart-sauce-labs-backpack");
        products.add("add-to-cart-sauce-labs-bike-light");
        products.add("add-to-cart-sauce-labs-bolt-t-shirt");
        products.add("add-to-cart-sauce-labs-fleece-jacket");
        products.add("add-to-cart-sauce-labs-onesie");
        products.add("add-to-cart-test.allthethings()-t-shirt-(red)");
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) (Math.random() * products.size());
            String productId = products.get(randomIndex);
            products.remove(randomIndex); // Remove the selected product to avoid duplicates
            driver.findElement(By.id(productId)).click();
            Thread.sleep(1000); // Wait for 1 second after adding each product
        }

        // Navigate to the cart
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the cart page is loaded

        // Continue to checkout
        driver.findElement(By.id("checkout")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the checkout page is loaded

        // Fill in the checkout information and proceed
        driver.findElement(By.id("first-name")).sendKeys("Andreja");
        Thread.sleep(1000); // Wait for 1 second before entering the last name
        driver.findElement(By.id("last-name")).sendKeys("Mihajlovski");
        Thread.sleep(1000); // Wait for 1 second before entering the postal code
        driver.findElement(By.id("postal-code")).sendKeys("1234");
        Thread.sleep(1000); // Wait for 1 second before clicking the continue button
        driver.findElement(By.id("continue")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the overview page is loaded

        // Verify that the overview page shows 3 items
        List<String> overviewItems = driver.findElements(By.className("inventory_item_name")).stream()
                .map(WebElement::getText)
                .toList();
        assertEquals(overviewItems.size(), 3, "Overview page should contain exactly 3 items.");

        // Complete the checkout process
        driver.findElement(By.id("finish")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the completion page is loaded

        // Verify that the completion page shows a success message
        String completionMessage = driver.findElement(By.className("complete-header")).getText();
        assertEquals(completionMessage, "Thank you for your order!");

        // Log out from the application
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(1000); // Wait for 1 second before clicking the logout button
        driver.findElement(By.id("logout_sidebar_link")).click();
        Thread.sleep(2000); // Wait for 2 seconds to ensure the logout is processed
    }
}
