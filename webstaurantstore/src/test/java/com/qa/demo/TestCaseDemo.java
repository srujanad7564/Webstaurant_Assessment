package com.qa.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestCaseDemo {
    WebDriver driver;
    WebDriverWait wait;

    //WebElement productOptions1 = driver.findElement(By.cssSelector(".suffix_dropdown configure item_number:last-child"));
    
    By searchBox = By.id("searchval");
    By productTitles = By.cssSelector("[data-testid='itemDescription']");
    By pageLinks = By.cssSelector(".pagerLink");
    By addToCartButtons = By.cssSelector("input[value='Add to Cart']");
    By viewCartButton = By.cssSelector(".btn.btn-small.btn-primary");
    By emptyCartButton = By.cssSelector(".emptyCartButton");
    By confirmEmptyCartButton = By.xpath("//*[@id='td']/div[11]/div/div/div/footer/button[1]");
    By productOptions = By.xpath("body > div:nth-child(5) > div:nth-child(1) > div:nth-child(4) > div:nth-child(4) > div:nth-child(3) > div:nth-child(60) > div:nth-child(4) > form:nth-child(1) > div:nth-child(1) > select:nth-child(1)");
   // 
    @Test
    public void testCaseDemo() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.get("https://www.webstaurantstore.com/");
        driver.manage().window().maximize();
        

        // Search for the product
        searchProduct("stainless work table");

        // Verify product titles
        List<WebElement> titles = getProductTitles();
        for (WebElement title : titles) {
            if (!title.getText().contains("Table")) {
                System.out.println("Product does not contain 'Table' in its title: " + title.getText());
            }
        }

        // Navigate to the last page
        navigateToLastPage();

        // Add the last item to the cart
        addLastItemToCart();

        // View and empty the cart
        viewCart();
        emptyCart();
        confirmEmptyCart();

        // Verify that the cart is empty
        String actualMessage = getEmptyCartMessage();
        assertEquals("Your cart is empty.", actualMessage);

        driver.quit();
    }

    public void searchProduct(String productName) {
        WebElement searchBoxElement = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        searchBoxElement.sendKeys(productName);
        searchBoxElement.submit();
    }

    
    public List<WebElement> getProductTitles() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitles));
        return driver.findElements(productTitles);
    }

    public void navigateToLastPage() {
        List<WebElement> pageLinksList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(pageLinks));
        pageLinksList.get(pageLinksList.size() - 2).click();
    }

 // Add a locator for the type of table selection element
    
 	By tableTypeDropdown = By.xpath("(//select[@title='Type'])[3]");
 	public void selectTableType() {
		WebElement tableTypeElement = wait.until(ExpectedConditions.elementToBeClickable(tableTypeDropdown));
		if (tableTypeElement.isDisplayed())
		{
			Select dropdown = new Select(tableTypeElement);
			dropdown.selectByIndex(1);
		} else
		{
			System.out.println("No dropdown found.");
		}
 	}
    
	public void addLastItemToCart() {
	
		List<WebElement> addToCartButtonsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));
		if (!addToCartButtonsList.isEmpty()) {
			
			WebElement lastItem = addToCartButtonsList.get(addToCartButtonsList.size() - 1);
			selectTableType();
			lastItem.click();
            
    
		} else {
			System.out.println("No 'Add to Cart' buttons found.");
		}
    }

    public void viewCart() {
        WebElement viewCartElement = wait.until(ExpectedConditions.elementToBeClickable(viewCartButton));
        viewCartElement.click();
    }

    public void emptyCart() {
        WebElement emptyCartElement = wait.until(ExpectedConditions.elementToBeClickable(emptyCartButton));
        emptyCartElement.click();
    }

    public void confirmEmptyCart() {
        WebElement confirmEmptyCartElement = wait.until(ExpectedConditions.elementToBeClickable(confirmEmptyCartButton));
        confirmEmptyCartElement.click();
    }

    public String getEmptyCartMessage() {
        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty-cart__text .header-1")));
        return emptyCartMessage.getText();
    }
}
