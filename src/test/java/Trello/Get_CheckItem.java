package Trello;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Get_CheckItem {


    @BeforeClass
    public Response GetCheckItem() {

        if(Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        if(Create_CheckItem.checkitem_id == null || Create_CheckItem.checkitem_id.isEmpty()) {
            throw new IllegalStateException("CheckItem ID is not initialized. Please ensure Create_CheckItem is executed first.");
        }


        System.out.println("Using Checklist ID to get CheckItems: " + Create_Checklist.checklist_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Scenario")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/checklists/" + Create_Checklist.checklist_id+"/checkItems/"+ Create_CheckItem.checkitem_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Test
    public void TestStatusCode() {
        GetCheckItem().then().statusCode(200);
    }

    @Test
    public void TestResponseTime() {
        long responseTime =GetCheckItem().getTime();
        assert responseTime < 3000;
    }

    @Test
    public void Validate_That_CheckItem_is_Created_successfully() {
        String str1 = GetCheckItem().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_CheckItem.checkitem_id);
        String str2 = GetCheckItem().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_CheckItem.checkitem_name);
    }

    @Test
    public void Validate_That_CheckItem_is_Created_successfully_on_specific_Checklist() {
        String str = GetCheckItem().jsonPath().getString("idChecklist");
        Assert.assertEquals(str,Create_Checklist.checklist_id);
    }


    @Test
    public void Validate_That_checklist_is_created_successfully_on_specific_Card()
    {
        Get_Card card = new Get_Card();
        Response r =   card.GetCard();
        String str = GetCheckItem().jsonPath().getString("idChecklist");
        String str1 = r.jsonPath().getString("idChecklists[0]");
        Assert.assertEquals(str,str1);

    }

}
