package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Create_CheckItem {
    public static String checkitem_id = " "; // Make it static
    public static String checkitem_name = " ";
    private Response response;
    @BeforeClass
    public void CreateCheckitem() {

        if (Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        System.out.println("Using Checklist ID to Create new CheckItem: " + Create_Checklist.checklist_id);


        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Logout redirection")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idCard", Create_Card.card_id)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/checklists/" + Create_Checklist.checklist_id+"/checkItems" )
                .then()
                .log().all()
                .extract()
                .response();
        checkitem_id= response.jsonPath().getString("id");
        checkitem_name = response.jsonPath().getString("name");

    }

    @Test
    public void TestStatusCode() {
        response.then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = response.getTime();
        assert responseTime < 3000;
    }


    @Test
    public void Validate_That_checkItem_is_created_successfully_on_Checklist()
    {
        String str= response.jsonPath().getString("idChecklist");
        Assert.assertEquals(str,Create_Checklist.checklist_id);
    }
}
