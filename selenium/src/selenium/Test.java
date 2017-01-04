package selenium;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Test {
	public static void main(String[] args) throws Exception{
		
		File file = new File("D:/CodingSoft/workspace/learning20160729/selenium/resource/chromedriver.exe");  
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());  
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:8080/epms/login.htm"); 
		driver.manage().window().maximize(); 
		WebElement account = driver.findElement(By.id("account"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement submit = driver.findElement(By.className("submit"));
		account.sendKeys("jbinfo");
		password.sendKeys("111111");
		submit.click();
		Thread.sleep(1000);
		driver.switchTo().frame("left"); 
		WebElement xmrbgl = driver.findElement(By.linkText("项目日报管理"));
		xmrbgl.click();
		WebElement kqgl = driver.findElement(By.id("menu_KQGL"));
		WebElement rbgl = kqgl.findElement(By.partialLinkText("日报管理"));
		rbgl.click();
		driver.switchTo().window(driver.getWindowHandle());
		driver.switchTo().frame("main"); 
		Thread.sleep(1000);
		WebElement add = driver.findElement(By.partialLinkText("添加"));
		add.click();
		Thread.sleep(1000);
		WebElement pcode = driver.findElement(By.id("pcode0"));
		pcode.findElements(By.tagName("option")).get(3).click();
		
		driver.findElement(By.id("workinfo0")).sendKeys("2016-12-24");
		driver.findElement(By.id("workDate0")).sendKeys("2016-12-24");
		
		
		WebElement submitButton = driver.findElement(By.id("submitButton"));
		//submitButton.click();
	}
}
