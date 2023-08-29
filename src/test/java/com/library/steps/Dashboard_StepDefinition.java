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
    JsonPath jsonPath;
    String token;
    int id = 0;
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
        System.out.println("id = " + id);

        //verifying if id is being provided or if we are retrieving all data;
        if(id==0) {
            response = given().accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .header("x-library-token", token)
                    .when()
                    .get(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek()
                    .then()
                    .extract().response();
        }

        else
        {
            response = given().accept(ContentType.JSON)
                    .pathParam("id", id)
                    .contentType(ContentType.JSON)
                    .header("x-library-token", token)
                    .when()
                    .get(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek()
                    .then()
                    .extract().response();
        }


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
       jsonPath = response.jsonPath();
        //gets all data
        List<Map<String, String>> dataList = jsonPath.getList("");

        //verify the key is not null
        for (Map<String, String> eachMap : dataList)
        {
            Assert.assertTrue(eachMap.get(key)!=null);

        }


    }

    @Given("Path param is {string}")
    public void path_param_is(String expectedId)
    {
        //converting the string value to integer
        id = Integer.valueOf(expectedId);
        System.out.println("id = " + id);


    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String id)
    {
        jsonPath = response.jsonPath();
        //converting expected id to integer
        int expectedId = jsonPath.getInt("id");
        Assert.assertTrue(expectedId == this.id);

    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> keys)
    {
        Map<String, String> dataMap= jsonPath.getMap("");

        //verify the key is not null
        for (String eachKey : keys)
        {
            Assert.assertTrue(dataMap.get(eachKey)!=null);
        }






    }

}
