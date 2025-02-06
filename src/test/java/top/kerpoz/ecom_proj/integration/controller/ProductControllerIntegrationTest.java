package top.kerpoz.ecom_proj.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("Should add product with image")
    void shouldAddProductWithImage() throws Exception {
        // JSON representation of the product
        String productJson = "{\"name\":\"Test Product\",\"description\":\"Test Description\",\"price\":19.99," +
                "\"category\":\"Test Category\",\"brand\":\"Test Brand\",\"releaseDate\":\"2025-02-06\"," +
                "\"productAvailable\":true,\"stockQuantity\":100}";

        // Create a MockMultipartFile for the 'product' part (JSON)
        MockMultipartFile productPart = new MockMultipartFile("product", "product.json", "application/json", productJson.getBytes());

        // Create a MockMultipartFile for the 'imageFile' part
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", new byte[0]);

        // Perform the multipart request
        mockMvc.perform(multipart("/api/product")
                        .file(productPart)  // Send the product data as JSON
                        .file(imageFile)    // Send the image file
                        .contentType(MediaType.MULTIPART_FORM_DATA))  // Set the content type
                .andExpect(status().isCreated());  // Expect a 201 Created response
    }

    @Test
    @WithMockUser
    @DisplayName("Should add product without image")
    void shouldAddProductWithoutImage() throws Exception {
        // JSON representation of the product
        String productJson = "{\"name\":\"Test Product\",\"description\":\"Test Description\",\"price\":19.99," +
                "\"category\":\"Test Category\",\"brand\":\"Test Brand\",\"releaseDate\":\"2025-02-06\"," +
                "\"productAvailable\":true,\"stockQuantity\":100}";

        // Create a MockMultipartFile for the 'product' part (JSON)
        MockMultipartFile productPart = new MockMultipartFile("product", "product.json", "application/json", productJson.getBytes());

        // Perform the multipart request
        mockMvc.perform(multipart("/api/product")
                        .file(productPart)  // Send the product data as JSON
                        .contentType(MediaType.MULTIPART_FORM_DATA))  // Set the content type
                .andExpect(status().isCreated());  // Expect a 201 Created response
    }

    @Test
    @WithMockUser
    @DisplayName("Should throw exception when adding product with image but no product data")
    void shouldThrowExceptionWhenAddingImageWithoutProductData() throws Exception {
        // Create a MockMultipartFile for the 'imageFile' part
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", new byte[0]);

        // Perform the multipart request
        mockMvc.perform(multipart("/api/product")
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))  // Set the content type
                .andExpect(status().isBadRequest());  // Expect a 201 Created response
    }
}