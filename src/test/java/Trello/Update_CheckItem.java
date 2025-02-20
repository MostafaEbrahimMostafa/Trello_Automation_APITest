package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Update_CheckItem {


    public Response update_checkItem() {
        // Access the static board_id directly

        if (Create_CheckItem.checkitem_id == null ||Create_CheckItem.checkitem_id.isEmpty()) {
            throw new IllegalStateException("CheckItem ID is not initialized. Please ensure Create_CheckItem is executed first.");
        }

        System.out.println("Using CheckItem ID to Update CheckItem: " + Create_CheckItem.checkitem_id);
        System.out.println("Using CheckItem List o Update CheckItem: " + Create_CheckItem.checkitem_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Validate logout working correctly")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/cards/" +Create_Card.card_id+"/checkItem/"+Create_CheckItem.checkitem_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        update_checkItem().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = update_checkItem().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Checklist_is_Updated_successfully() {
        String str2 = update_checkItem().jsonPath().getString("name");
        Assert.assertNotEquals(str2,Create_CheckItem.checkitem_name);
    }
}
