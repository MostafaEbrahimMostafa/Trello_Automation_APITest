package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Delete_Board {

    private Response response; // Store the response to reuse in tests

    @BeforeClass
    public void deleteBoard() {
        // Access the static board_id from Create_Board
        String boardId = Create_Board.board_id;

        // Ensure board_id is initialized
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID for Deletion: " + boardId);

        // Perform the DELETE request to delete the board
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // Access APIKey directly
                .queryParam("token", Create_Board.token) // Access token directly
                .contentType(ContentType.JSON)
                .when()
                .delete("/1/boards/" + boardId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    public void testStatusCode() {
        // Validate the status code of the DELETE request
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Test
    public void testResponseTime() {
        // Validate the response time of the DELETE request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms: " + responseTime);
    }

    @Test
    public void Validate_That_Board_is_Deleted_successfully() {
        Get_Board getBoard = new Get_Board();
        Response getListResponse = getBoard.getBoard();

        // Verify that the list is deleted by checking the status code
        int getStatusCode = getListResponse.getStatusCode();
        System.out.println("Get Status Code: " + getStatusCode);

        // If list is deleted, the response should return 404 (not found)
        assertEquals(getStatusCode, 404, "Expected status code 404 but got: " + getStatusCode);
    }
}
