package top.kerpoz.ecom_proj.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import top.kerpoz.ecom_proj.exception.ProductNotFoundException;
import top.kerpoz.ecom_proj.model.entity.Product;
import top.kerpoz.ecom_proj.repository.ProductRepository;
import top.kerpoz.ecom_proj.service.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository mockProductRepository;

    private ProductService productServiceUnderTest;

    @BeforeEach
    void setUp() {
        productServiceUnderTest = new ProductService(mockProductRepository);
    }

    private Product createSampleProduct(int id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Sample Product");
        product.setBrand("Brand");
        product.setCategory("Category");
        product.setDescription("Sample description");
        product.setImageName("imageName");
        product.setImageType("imageType");
        product.setImageData("content".getBytes());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductAvailable(true);
        product.setStockQuantity(10);
        return product;
    }

    @Test
    @DisplayName("Should return a list of products when repository has data")
    void shouldReturnListOfProducts() {
        // Arrange
        Product product = createSampleProduct(1);
        when(mockProductRepository.findAll()).thenReturn(List.of(product));

        // Act
        List<Product> result = productServiceUnderTest.getProducts();

        // Assert
        verify(mockProductRepository).findAll();
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getName()).isEqualTo("Sample Product");
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProductsExist() {
        // Arrange
        when(mockProductRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Product> result = productServiceUnderTest.getProducts();

        // Assert
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("Should return a product when it exists")
    void shouldReturnProductWhenExists() {
        // Arrange
        Product product = createSampleProduct(1);
        when(mockProductRepository.findById(1)).thenReturn(Optional.of(product));

        // Act
        Product result = productServiceUnderTest.getProduct(1);

        // Assert
        verify(mockProductRepository).findById(eq(1));
        assertThat(Optional.of(result)).isPresent().contains(product);
    }

    @Test
    @DisplayName("Should throw exception when trying to get non-existent product")
    void shouldThrowExceptionWhenGettingNonExistentProduct() {
        // Arrange
        when(mockProductRepository.findById(1)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> productServiceUnderTest.getProduct(1))
                .isInstanceOf(ProductNotFoundException.class);

        verify(mockProductRepository).findById(eq(1));
    }

    @Test
    @DisplayName("Should add a new product successfully")
    void shouldAddNewProduct() throws IOException {
        // Arrange
        Product inputProduct = createSampleProduct(0);

        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "dummy content".getBytes());

        when(mockProductRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productServiceUnderTest.addProduct(inputProduct, imageFile);

        // Assert
        verify(mockProductRepository).save(any(Product.class));
        assertThat(result.getName()).isEqualTo(inputProduct.getName());
    }

    @Test
    @DisplayName("Should update an existing product successfully")
    void shouldUpdateExistingProduct() throws IOException {
        // Arrange: Mock existing product in repository
        Product existingProduct = createSampleProduct(1);

        when(mockProductRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        Product updatedProduct = createSampleProduct(1);
        updatedProduct.setName("Updated Name");

        MultipartFile imageFile = new MockMultipartFile("image", "updated-image.jpg", "image/jpeg", "updated content".getBytes());

        when(mockProductRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Call update method with updated details and file
        Product result = productServiceUnderTest.updateProduct(1, updatedProduct, imageFile);

        // Assert: Verify repository interactions and updated values
        verify(mockProductRepository).findById(eq(1));
        verify(mockProductRepository).save(any(Product.class));
        assertThat(result.getName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("Should delete a product successfully")
    void shouldDeleteExistingProduct() {
        // Arrange: Mock existing product in repository
        Product existingProduct = createSampleProduct(1);
        when(mockProductRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        // Act: Call delete method
        productServiceUnderTest.deleteProduct(1);

        // Assert: Verify that the delete method was called with the correct parameters
        verify(mockProductRepository).delete(existingProduct);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete non-existent product")
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // Arrange: Mock repository to return empty for a non-existent ID
        when(mockProductRepository.findById(1)).thenReturn(Optional.empty());

        // Act and Assert: Expect a ProductNotFoundException to be thrown
        assertThatThrownBy(() -> productServiceUnderTest.deleteProduct(1))
                .isInstanceOf(ProductNotFoundException.class);

        verify(mockProductRepository).findById(eq(1));
    }

    @Test
    @DisplayName("Should return empty list when searching for products with no matches")
    void shouldReturnEmptyListWhenSearchingForNoMatches() {
        // Arrange: Mock search to return empty list for keyword search.
        when(mockProductRepository.searchProducts(any(String.class))).thenReturn(Collections.emptyList());

        // Act: Perform search.
        List<Product> result = productServiceUnderTest.searchProducts("nonexistent");

        // Assert: Verify results.
        verify(mockProductRepository).searchProducts(eq("nonexistent"));
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when searching products and an error occurs")
    void shouldThrowExceptionWhenSearchingAndErrorOccurs() {
        // Arrange: Mock search to throw exception.
        when(mockProductRepository.searchProducts(any(String.class)))
                .thenThrow(new ProductNotFoundException(123));

        // Act and Assert: Expect exception.
        assertThatThrownBy(() -> productServiceUnderTest.searchProducts("keyword"))
                .isInstanceOf(ProductNotFoundException.class);

        verify(mockProductRepository).searchProducts(eq("keyword"));
    }

    @Test
    @DisplayName("Should update an existing product without changing the image")
    void shouldUpdateExistingProductWithoutChangingImage() throws IOException {
        // Arrange: Mock existing product in repository
        Product existingProduct = createSampleProduct(1);
        when(mockProductRepository.findById(1)).thenReturn(Optional.of(existingProduct));

        Product updatedProduct = createSampleProduct(1);
        updatedProduct.setName("Updated Name");

        // Ensure save() returns the updated product
        when(mockProductRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Call update method without an image file
        Product result = productServiceUnderTest.updateProduct(1, updatedProduct, new MockMultipartFile("noImageDataBecauseOf0", new byte[0]));

        // Assert: Verify repository interactions and updated values
        verify(mockProductRepository).findById(eq(1));
        verify(mockProductRepository).save(any(Product.class));
        assertThat(result).isNotNull(); // Ensure result is not null
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getImageData()).isEqualTo(existingProduct.getImageData());
    }

    @Test
    @DisplayName("Should successfully add a new product when image-related fields are null")
    void shouldAddNewProductWithNullImage() throws IOException {
        // Arrange
        Product inputProduct = createSampleProduct(0);
        inputProduct.setImageData(null);
        inputProduct.setImageType(null);
        inputProduct.setImageName(null);

        when(mockProductRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product savedProduct = invocation.getArgument(0);
                    // Verify that image-related fields remain null during save
                    assertThat(savedProduct.getImageData()).isNull();
                    assertThat(savedProduct.getImageType()).isNull();
                    assertThat(savedProduct.getImageName()).isNull();
                    return savedProduct;
                });

        // Act
        Product result = productServiceUnderTest.addProduct(inputProduct, null);

        // Assert
        verify(mockProductRepository).save(argThat(product -> {
            return product.getImageData() == null &&
                    product.getImageType() == null &&
                    product.getImageName() == null;
        }));

        // Verify other product fields are set correctly
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(inputProduct.getName());
        assertThat(result.getImageData()).isNull();
        assertThat(result.getImageType()).isNull();
        assertThat(result.getImageName()).isNull();
    }
}