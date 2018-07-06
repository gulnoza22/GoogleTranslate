package com.googletranslate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleTranslateTest {
	WebDriver driver;

	@BeforeClass
	public void setup() {
		
		/**
		 * Before we had to download a binary for WebDriver to handle the browser,
		 * For that to work we needed to set absolute path to that binary as Java variable,
		 * If binary gets updated, we had to update that binary manually - which took time,
		 * Therefore we use a very neat Boni Garcia Java library that does it for us.
		 */
		// Before we used System.setProperty in selenium, but now since we are
		// using Maven project tool we have boni garcia dependency in our pom.xml file
		// thats why we are able to use simpler way of choosing browser.
		WebDriverManager.chromedriver().setup();
		// we are creating Chrome browser object
		driver = new ChromeDriver();
		// we are maximizing a window, mac users - please use fullScreen() method
		driver.manage().window().maximize();
		// it will wait for 10 seconds to load all the elements on the page
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	// first of all we have to create a method
	public void googleTranslate() throws InterruptedException {
		
		// here we are providing a word we wanna learn the translation of
		String word = "Good";
		// we are navigating to Google Translate page
		driver.get("https://translate.google.com/");
		// we are verifying if we landed on the right page by inspecting one of the elements
		String actualElement = driver.findElement(By.id("gt-appname")).getText();
		Assert.assertEquals(actualElement,"Translate");
		// inspecting id of English language button and clicking on it
		driver.findElement(By.id("sugg-item-en")).click();
		// inspecting the source field we want to write our word
		driver.findElement(By.cssSelector("#source")).sendKeys(word);
		// inspecting dropdown button and clicking on it.
		driver.findElement(By.xpath("//div[@id='gt-tl-gms']")).click();

		// adding all languages into a list
		List<WebElement> listOfLanguages = driver.findElements(By.xpath("//div[starts-with(@id,'goog-menuitem-group')]//div[@class='goog-menuitem-content']"));
		                                                                
		// printing the count of languages
		System.out.println("There are " + listOfLanguages.size() + " languages in Google Translate.");
		System.out.println("============================================");

		// we have to create  an empty list for translated words to add them inside our loop
		List<String> translatedWord = new ArrayList<>();
		
		// Since We want our languages to be printed by sorted look, we are using SortedMap so that our languages are printed in alphabetical order
		// TreeMap class implements SortedMap interface - it serves to sort the keys
		SortedMap<String, String> map = new TreeMap<>();
		for (WebElement each : listOfLanguages) {
			// we have to click on each language
			each.click();
			// we have to use a time between iterations
			Thread.sleep(400);
			// we have to inspect the result field of translated words, capturing and adding our new word to our translatedWord list
			translatedWord.add(driver.findElement(By.id("result_box")).getText());
			// we have to click on a dropdown arrow
			driver.findElement(By.xpath("//div[@id='gt-tl-gms']")).click();
		}

		// we have to use for loop because we need an index to loop through each languages
		for (int i = 0; i < listOfLanguages.size(); i++) {
			
			// we are adding languages and translated words to our map
			map.put(listOfLanguages.get(i).getText(), translatedWord.get(i));
		}

		// in order to iterate our map's keys and values we are using Entry interface and foreach loop
		Set<Entry<String, String>> entries = map.entrySet();
		for (Entry<String, String> entry : entries) {
			// unfortunately Eclipse doesn't support all the letters in different languages
			// therefore you will see question marks for several of them in console
			System.out.println(word + " in " + entry.getKey() + " language is " + entry.getValue());
		}
		// this line displays in alphabetical order because we are using SortedMap
		System.out.println(map);
	}

	@AfterClass
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.close();
	}
}
