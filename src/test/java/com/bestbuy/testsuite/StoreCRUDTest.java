package com.bestbuy.testsuite;

import com.bestbuy.bestbuyinfo.StoreSteps;
import com.bestbuy.testbase.TestBaseStores;
import com.bestbuy.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StoreCRUDTest extends TestBaseStores {
    static String name = "Zara" + TestUtils.getRandomValue();
    static String type = "SmallBox" + TestUtils.getRandomValue();
    static String address = TestUtils.getRandomValue();
    static String address2 =  "";
    static String city = TestUtils.getRandomValue();
    static String state = TestUtils.getRandomValue();
    static String zip = TestUtils.getRandomValue();
    static double lat = TestUtils.randomDouble();
    static double lng = TestUtils.randomDouble();
    static String hours = "Mon: 10-9; Tue: 10-9; Wed: 10-9; Thurs: 10-9; Fri: 10-9; Sat: 10-9; Sun: 10-8";

    static int storeID;
    @Steps
    StoreSteps storeSteps;

    @WithTags({
            @WithTag("storefeature:SANITY"),
            @WithTag("storefeature:SMOKE"),
            @WithTag("storefeature:REGRESSION")
    })
    @Title("This will create new store")
    @Test
    public void test001() {
        ValidatableResponse response = storeSteps.createStore(name, type, address, address2, city, state, zip, lat, lng, hours);
        response.log().all().statusCode(201);
        storeID = response.log().all().extract().path("id");
    }

    @WithTags({
            @WithTag("storefeature:SANITY"),
            @WithTag("storefeature:REGRESSION")
    })
    @Title("Verify if the store has been added to application")
    @Test
    public void test002() {
        HashMap<String, Object> storeMap = storeSteps.getStoreInfoByStoreID(storeID);
        Assert.assertThat(storeMap, hasValue(type));

    }

    @WithTags({
            @WithTag("storefeature:SMOKE"),
            @WithTag("storefeature:REGRESSION")
    })
    @Title("This will update store information")
    @Test
    public void test003() {
        name = name + "_Updated";

        storeSteps.updateStore(storeID, name, type, address, address2, city, state, zip, lat, lng, hours);

        HashMap<String, Object> storeMap = storeSteps.getStoreInfoByStoreID(storeID);
        Assert.assertThat(storeMap, hasValue(city));
    }

    @WithTags({
            @WithTag("storefeature:REGRESSION")
    })
    @Title("This will delete Store")
    @Test
    public void test004() {

        storeSteps.deleteStoreByID(storeID).statusCode(200);
        storeSteps.getStoreById(storeID).statusCode(404);
    }

}
