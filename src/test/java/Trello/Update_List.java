package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Update_List {

    public Response update_list() {
        // Access the static board_id directly

        String ListId = Create_List.list_id;
        String ListName = Create_List.list_name;
        if (ListId == null || ListId.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using List ID to Update List: " + ListId);
        System.out.println("Using Board List o Update List: " + ListName);

        return given()
                .baseUri("https://api.trello.com")
                .queryParams("name","In Progress")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/lists/" + Create_List.list_id)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    public void TestStatusCode() {
       update_list().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = update_list().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_List_is_Updated_successfully() {
        String str2 = update_list().jsonPath().getString("name");
        Assert.assertNotEquals(str2,Create_List.list_name);
    }
}

