package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Create_Checklist {

    //curl --request POST \
    //  --url 'https://api.trello.com/1/checklists?idCard=5abbe4b7ddc1b351ef961414&key=APIKey&token=APIToken'


    public static String checklist_id = " "; // Make it static
    public static String checklist_name = " ";
    private Response response;
    @BeforeClass
    public void CreateChecklist() {

        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        System.out.println("Using Card ID to Create new Checklist: " + Create_Card.card_id);


        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Login")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idCard", Create_Card.card_id)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/checklists")
                .then()
                .log().all()
                .extract()
                .response();
        checklist_id= response.jsonPath().getString("id");
        checklist_name = response.jsonPath().getString("name");

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
    public void Validate_That_checklist_is_created_successfully_on_card()
    {
        String str= response.jsonPath().getString("idCard");
        Assert.assertEquals(str,Create_Card.card_id);
    }

    @Test
    public void Validate_That_card_is_created_successfully_on_board()
    {
        String str = response.jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_Board.board_id);
    }


}
