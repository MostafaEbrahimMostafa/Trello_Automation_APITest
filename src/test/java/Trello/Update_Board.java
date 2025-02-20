package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Test(priority = 3)
public class Update_Board {



    public Response update_board() {
        // Access the static board_id directly

        String boardId = Create_Board.board_id;
        String boardName = Create_Board.board_name;
        Create_Board c = new Create_Board();
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID to Update board: " + boardId);
        System.out.println("Using Board Name to Update board: " + boardName);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","RestAssured")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/boards/" + boardId)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        update_board().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = update_board().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Board_is_Updated_successfully() {
        String str2 = update_board().jsonPath().getString("name");
        Assert.assertNotEquals(str2,Create_Board.board_name);
    }


}

