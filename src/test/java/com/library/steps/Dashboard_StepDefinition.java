package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Dashboard_StepDefinition
{
    Response response;
    String token;
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role)
    {
        token = LibraryAPI_Util.getToken(role);

        System.out.println("token = " + token);

    }
    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeaderContentType)
    {
        given().accept(acceptHeaderContentType);


    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endPoint)
    {
        response = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("x-library-token", token)
                .when()
                .get(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek()
                .then()
                .extract().response();


    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode)
    {
        Integer actualStatusCode = response.statusCode();
        Assert.assertEquals(statusCode, actualStatusCode);

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String responseHeaderContentType)
    {
       String actualResponseContentType = response.contentType();

       Assert.assertEquals(responseHeaderContentType, actualResponseContentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String key)
    {
        JsonPath jsonPath = response.jsonPath();
        //gets all data
        List<Map<String, String>> dataList = jsonPath.getList("");

        //verify the key is not null
        for (Map<String, String> eachMap : dataList)
        {
            Assert.assertTrue(eachMap.get(key)!=null);

        }


    }
}
