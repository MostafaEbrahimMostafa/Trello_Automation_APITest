package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Get_List {

    @BeforeClass
    public Response GetList() {
        String ListId = Create_List.list_id;
        String ListName = Create_List.list_name;
        if (ListId == null || ListId.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using List ID to get List: " + ListId);
        System.out.println("Using List Name to get List: " + ListName);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/lists/" + Create_List.list_id )
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        GetList().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime = GetList().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_List_is_Created_successfully() {
        String str1 = GetList().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_List.list_id);
        String str2 = GetList().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_List.list_name);
    }

    @Test
    public void Validate_That_List_is_Created_successfully_on_specific_board() {
        String str = GetList().jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_List.BID);
    }


}
