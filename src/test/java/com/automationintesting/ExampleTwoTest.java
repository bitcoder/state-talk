package com.automationintesting;

import com.automationintesting.e2databuilder.builder.BookingBuilder;
import com.automationintesting.e2databuilder.creators.BookingCreator;
import com.automationintesting.e2databuilder.model.BookingRequestModel;
import com.automationintesting.pageobjects.LoginPage;
import com.automationintesting.pageobjects.RoomListingPage;
import com.automationintesting.pageobjects.RoomPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ExampleTwoTest {

    WebDriver driver;

    @Before
    public void SetUp(){
        driver = new DriverFactory().create();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @Test
    public void dataBuilderPatternApproach() throws InterruptedException {

        BookingRequestModel booking = new BookingBuilder().BuildOneWeekBooking();
        BookingCreator.createBooking(booking);

        Thread.sleep(1000); // Added for demo purposes

        driver.navigate().to("http://localhost:8080/#/admin");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.populateUsername("admin");
        loginPage.populatePassword("password");
        loginPage.clickLogin();

        Thread.sleep(1000); // Added for demo purposes

        RoomListingPage roomListingPage = new RoomListingPage(driver);
        roomListingPage.clickFirstRoom();

        RoomPage roomPage = new RoomPage(driver);
        WebElement latestBooking = roomPage.getLatestBooking();

        Thread.sleep(1000); // Added for demo purposes

        assertEquals("Mark\nWinteringham\n700\ntrue\n2019-09-09\n2019-09-16", latestBooking.getText());
    }

    @After
    public void TearDown(){
        driver.quit();
    }

}
