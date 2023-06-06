package com.bestbuy.testsuite;

import com.bestbuy.bestbuyinfo.ProductSteps;
import com.bestbuy.testbase.TestBaseProducts;
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
public class ProductCRUDTest extends TestBaseProducts {
    private String name = "Lara" + TestUtils.getRandomValue();
    private String type = "Lakme" + TestUtils.getRandomValue();
    private Double price = 6.98;
    private String upc = TestUtils.getRandomValue();
    private int shipping = TestUtils.getRandomNumber();
    private String description = TestUtils.getRandomValue();
    private String manufacturer = TestUtils.getRandomValue();
    private String  model  = "MN" + TestUtils.getRandomValue();
    private String  url = "http://" + TestUtils.randomString();
    private String image = "http://" +TestUtils.randomString();
    static int productID;

    @Steps
    ProductSteps productSteps;

    @WithTags({
            @WithTag("productfeature:SANITY"),
            @WithTag("productfeature:SMOKE"),
            @WithTag("productfeature:REGRESSION")
    })
    @Title("This will create a new product")
    @Test
    public void test001(){
        ValidatableResponse response = productSteps.createProduct(name,type, price, shipping, description, upc, manufacturer, model, url, image);
        response.log().all().statusCode(201);
        productID = response.log().all().extract().path("id");
    }

    @WithTags({
            @WithTag("productfeature:SANITY"),
            @WithTag("productfeature:REGRESSION")
    })
    @Title("Verify if the product was added to application")
    @Test
    public void test002() {

        HashMap<String, Object> productMap = productSteps.getProductInfoByProductName(productID);
        Assert.assertThat(productMap, hasValue(productID));
    }

    @WithTags({
            @WithTag("productfeature:SMOKE"),
            @WithTag("productfeature:REGRESSION")
    })
    @Title("This will update product")
    @Test
    public void test003() {
        name = name + "_Updated";

        productSteps.updateProduct(productID, name, type, price, shipping, upc, description, manufacturer, model, url, image);

        HashMap<String, Object> productMap = productSteps.getProductInfoByProductName(productID);
        Assert.assertThat(productMap, hasValue(name));
    }

    @WithTags({
            @WithTag("productfeature:REGRESSION")
    })
    @Title("This will delete product")
    @Test
    public void test004() {

        productSteps.deleteProductByID(productID).statusCode(200);
        productSteps.getProductById(productID).statusCode(404);
    }


}

