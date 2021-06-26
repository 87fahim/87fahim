package testNG;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FirstTestNG {
	private WebDriver driver;
	private BT BR;
	private String url;

	@Test
	public void validTest() throws InterruptedException {
		Assert.assertEquals(driver.getTitle(), "Login - iBilling", "WRONG PAGE!!!");

		driver.findElement(By.id("username")).sendKeys("demo@techfios.com");
		driver.findElement(By.id("password")).sendKeys("abc123");
		driver.findElement(By.xpath("//button[@class='btn btn-success block full-width']")).click();

		WebElement expected = driver.findElement(By.xpath("//h2[contains(text(),' Dashboard ')]"));
		Assert.assertEquals(expected.getText(), "Dashboard", "WRONG PAGE AFTER LOG IN!!");
	}
	@BeforeClass
	public void readConfig() {
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			url = prop.getProperty("url");
			String temp = prop.getProperty("browser").toUpperCase();
			convertToEnum(temp);
		} catch (IOException e) {
			System.out.printf("This message come from %s and ,readConfig method.\n",this.getClass());
			e.printStackTrace();
		}
		

	}
	private void convertToEnum(String browserType) {
		if(browserType.equals(BT.CHROME.toString()))
			BR = BT.CHROME;
		else if(browserType.equals(BT.FIREFOX.toString()))
			BR = BT.FIREFOX;
		
		else if(browserType.equals(BT.EDGE.toString()))
			BR = BT.EDGE;
		else 
			BR = null;
	}

	@BeforeMethod
	public void lauchBrowser() {
		if (BR == BT.FIREFOX)
			fireFox();
		else if (BR == BT.EDGE)
			edge();
		else if (BR == BT.CHROME)
			chrome();
		else {
			System.out.println("Please choose a driver!!");
			return;
		}
		driverSetup();
	}

	@AfterMethod
	public void closeBrowser() throws InterruptedException {
		wait(3);
		driver.quit();
	}

	private void driverSetup() {
		driver.get(url);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private void edge() {
		System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
		driver = new EdgeDriver();
	}

	private void chrome() {
		System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
		driver = new ChromeDriver();
	}

	private void fireFox() {
		System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
		driver = new FirefoxDriver();
	}

	private void wait(int seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private enum BT {
		CHROME, FIREFOX, EDGE;

	}
}
