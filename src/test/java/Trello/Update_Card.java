package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Update_Card {

    public Response update_card() {
        // Access the static board_id directly

        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        System.out.println("Using Card ID to Update Card: " + Create_Card.card_id);
        System.out.println("Using Card List o Update Card: " + Create_Card.card_name);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Logout")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/cards/" + Create_Card.card_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        update_card().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = update_card().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_Card_is_Updated_successfully() {
        String str2 = update_card().jsonPath().getString("name");
        Assert.assertNotEquals(str2,Create_Card.card_name);
    }

}
