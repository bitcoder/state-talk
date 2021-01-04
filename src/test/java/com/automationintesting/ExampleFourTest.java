package com.automationintesting;

import com.automationintesting.pageobjects.LoginPage;
import com.automationintesting.pageobjects.RoomListingPage;
import com.automationintesting.pageobjects.RoomPage;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

public class ExampleFourTest {

    WebDriver driver;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(3000));

    @Before
    public void SetUp(){
        driver = new DriverFactory().create();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @Test
    public void mockingApproach() throws InterruptedException {

        wireMockRule.stubFor(get(urlEqualTo("/booking/?roomid=1"))
                .willReturn(aResponse()
                    .withBody("{\"bookings\": [{\"bookingid\": 2,\"roomid\": 1,\"firstname\": \"Mark\",\"lastname\": \"Winteringham\",\"depositpaid\": true,\"bookingdates\": {\"checkin\": \"2019-09-10\",\"checkout\": \"2019-09-17\"}}]}")
                ));

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

        assertEquals("Mark\nWinteringham\n700\ntrue\n2019-09-10\n2019-09-17", latestBooking.getText());
    }

    @After
    public void TearDown(){
        driver.quit();
    }

}
