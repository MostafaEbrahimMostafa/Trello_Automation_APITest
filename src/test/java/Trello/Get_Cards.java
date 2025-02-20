package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Get_Cards {

    private Response response;
    @BeforeClass
    public void fetchCards() {

        // Validate if the board_id is initialized
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using List ID to fetch lists: " + Create_List.list_id);

        // Fetch all lists on the board
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // API Key
                .queryParam("token", Create_Board.token) // API Token
                .contentType(ContentType.JSON)
                .when()
                .get("1/lists/" + Create_List.list_id+"/cards") // Correct endpoint
                .then()
                .log().all()
                .extract()
                .response();
    }
    @Test
    public void testStatusCode() {
        // Verify the status code of the response
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Test
    public void testResponseTime() {
        // Validate the response time of the request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms");
    }

    @Test
    public void Validate_that_cards_is_retrieved_successfully() {
        // Verify that lists were retrieved successfully
        String jsonResponse = response.getBody().asString();
        System.out.println("Cards Response Body: " + jsonResponse);

        // Assert that the response contains at least one list
        int cardCount = response.jsonPath().getList("$").size(); // Count the number of items in the response array
        System.out.println("Number of Cardss: " + cardCount);
        assertTrue(cardCount > 0, "No lists were retrieved from the board.");
    }


}
