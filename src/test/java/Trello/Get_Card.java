package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Get_Card {

    @BeforeClass
    public Response GetCard() {



        if(Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        if (Create_List.list_id == null || Create_List.list_id.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }

        System.out.println("Using Card ID to get Card: " + Create_Card.card_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/cards/" + Create_Card.card_id )
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        GetCard().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime =GetCard().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Card_is_Created_successfully() {
        String str1 = GetCard().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_Card.card_id);
        String str2 = GetCard().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_Card.card_name);
    }

    @Test
    public void Validate_That_List_is_Created_successfully_on_specific_list() {
        String str = GetCard().jsonPath().getString("idList");
        Assert.assertEquals(str,Create_List.list_id);
    }

    @Test
    public void Validate_That_List_is_Created_successfully_on_specific_board() {
        String str = GetCard().jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_Board.board_id);
    }

}
