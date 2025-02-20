package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Update_Checklist {

    public Response update_checklist() {
        // Access the static board_id directly

        if (Create_Checklist.checklist_id == null ||Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        System.out.println("Using Checklist ID to Update Checklist: " + Create_Checklist.checklist_id);
        System.out.println("Using Checklist List o Update Checklist: " + Create_Checklist.checklist_name);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Valid Test Cases")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/checklists/" +Create_Checklist.checklist_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        update_checklist().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = update_checklist().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Checklist_is_Updated_successfully() {
        String str2 = update_checklist().jsonPath().getString("name");
        Assert.assertNotEquals(str2,Create_Checklist.checklist_name);
    }
}
