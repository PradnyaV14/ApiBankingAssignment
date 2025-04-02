package com.productManagement;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test file for performing unit test cases
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductResourceTest {

    private static Long productId;

    /**
     * To retrieve all products.
     */
    @Test
    @Order(1)
    public void testGetAllProducts() {
        given()
          .when().get("/products")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON);
    }

    /**
     * To add a new product.
     */
    @Test
    @Order(2)
    public void testCreateProduct() {
        String productJson = """
            {
                "name": "Laptop",
                "description": "High-end gaming laptop",
                "price": 1499.99,
                "quantity": 5
            }
            """;

        Response response = given()
          .contentType(ContentType.JSON)
          .body(productJson)
          .when().post("/products")
          .then()
             .statusCode(201)
             .contentType(ContentType.JSON)
             .body("name", equalTo("Laptop"))
             .body("price", hasToString("1499.99")) // Handles floating point precision
             .extract().response();

        productId = response.jsonPath().getLong("id"); // Ensure proper ID retrieval
        System.out.println("Added Product with id: " + productId + "\nDetails:\n" + response.asPrettyString());

        Assertions.assertNotNull(productId, "Product ID should not be null");
    }

    /**
     * To fetch a specific product by ID.
     */
    @Test
    @Order(3)
    public void testGetProductById() {
        Assertions.assertNotNull(productId, "Product ID should not be null");

        given()
          .pathParam("id", productId)
          .when().get("/products/{id}")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("id", is(productId.intValue())) // Ensure correct ID
             .body("name", equalTo("Laptop"));
    }

    /**
     * To update an existing product's details.
     */
    @Test
    @Order(4)
    public void testUpdateProduct() {
        Assertions.assertNotNull(productId, "Product ID should not be null");

        String updatedProductJson = """
            {
                "name": "Gaming Laptop",
                "description": "Updated high-end gaming laptop",
                "price": 1599.99,
                "quantity": 3
            }
            """;

        given()
          .pathParam("id", productId)
          .contentType(ContentType.JSON)
          .body(updatedProductJson)
          .when().put("/products/{id}")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("name", equalTo("Gaming Laptop"))
             .body("price", hasToString("1599.99")); // Fixes floating point issue
    }

    /**
     * To delete a product based on ID.
     */
    @Test
    @Order(5)
    public void testDeleteProduct() {
        Assertions.assertNotNull(productId, "Product ID should not be null");

        given()
          .pathParam("id", productId)
          .when().delete("/products/{id}")
          .then()
             .statusCode(204); // 204 No Content

        System.out.println("DELETEED PRODUCT ID: " + productId);
    }

    /**
     * To fetch a deleted product details (should return 404).
     */
    @Test
    @Order(6)
    public void testGetDeletedProduct() {
        Assertions.assertNotNull(productId, "Product ID should not be null");

        given()
          .pathParam("id", productId)
          .when().get("/products/{id}")
          .then()
             .statusCode(404);
    }
}
