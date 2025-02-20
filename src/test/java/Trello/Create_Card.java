package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Create_Card {
    public static String card_id = " "; // Make it static
    public static String card_name = " ";
    private Response response;
    @BeforeClass
    public void CreateCard() {
        String li = Create_List.list_id;

        if (Create_List.list_id == null || Create_List.list_id.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Card ID to Create new card: " + li);


        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Login")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idList", Create_List.list_id)
                .contentType(ContentType.JSON)  
                .when()
                .post("/1/cards")
                .then()
                .log().all()
                .extract()
                .response();
        card_id = response.jsonPath().getString("id");
        card_name = response.jsonPath().getString("name");

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
    public void Validate_That_card_is_created_successfully_on_board()
    {
        String str= response.jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_Board.board_id);
    }

    @Test
    public void Validate_That_card_is_created_successfully_on_list()
    {
        String str = response.jsonPath().getString("idList");
        Assert.assertEquals(str,Create_List.list_id);
    }


}
