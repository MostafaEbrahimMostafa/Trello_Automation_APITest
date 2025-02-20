package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

@Test(priority = 1)
public class Create_Board {

    public static String APIKey = "02b3ed9affd044752b0143db18279eb7";
    public static String token = "ATTA4a119ccc642eee90628adacd5432b8f7f65f437a3519ea6d3ddf8096d16f1c2e80A7BDDD";
    public static String board_id = " "; // Make it static
    public static String board_name = " ";

    private Response response; // To store the response of board creation

    @BeforeClass
    public void createBoard() {
        // Create the board once before all tests
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name", "API Testing")
                .queryParam("key", APIKey)
                .queryParam("token", token)
                .queryParam("prefs_background","orange")
                .contentType(ContentType.JSON)
                .when()
                .post("/1/boards/")
                .then()
                .log().all()
                .extract()
                .response();

        // Extract and store the board ID
        board_id = response.jsonPath().getString("id");
        board_name = response.jsonPath().getString("name");
        System.out.println("Created Board ID: " + board_id);


    }

    @Test
    public void TestStatusCode() {
        // Validate the status code of the board creation
        response.then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        // Validate the response time of the board creation
        long responseTime = response.getTime();
        assertTrue(responseTime < 3000, "Response time is greater than 3000ms");
    }

}
