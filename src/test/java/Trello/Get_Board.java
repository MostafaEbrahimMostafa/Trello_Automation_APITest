package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Test(priority = 2)
public class Get_Board {


    public Response getBoard() {
        // Access the static board_id directly

        String boardId = Create_Board.board_id;
        String boardName = Create_Board.board_name;
        //Create_Board c = new Create_Board();
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID to get board: " + boardId);
        System.out.println("Using Board Name to get board: " + boardName);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key",Create_Board.APIKey)
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/boards/" + boardId)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
       getBoard().then().statusCode(200);

    }

    @Test
    public void TestResponseTime() {
        long responseTime = getBoard().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Board_is_Created_successfully() {
        String str1 = getBoard().jsonPath().getString("id");
        Assert.assertEquals(str1,Create_Board.board_id);
        String str2 = getBoard().jsonPath().getString("name");
        Assert.assertEquals(str2,Create_Board.board_name);
    }

}
