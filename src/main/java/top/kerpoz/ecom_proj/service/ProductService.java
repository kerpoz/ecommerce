package top.kerpoz.ecom_proj.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kerpoz.ecom_proj.exception.ProductNotFoundException;
import top.kerpoz.ecom_proj.model.Product;
import top.kerpoz.ecom_proj.repository.ProductRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(int prodId) {
        return productRepository.findById(prodId)
                .orElseThrow(() -> new ProductNotFoundException(prodId));
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        // If an image is provided, save the image fields
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
        }
        return productRepository.save(product);
    }

    public Product updateProduct(int prodId, Product updatedProduct, MultipartFile imageFile) throws IOException {
        // Fetch existing product or throw an exception if not found
        Product existingProduct = productRepository.findById(prodId)
                .orElseThrow(() -> new ProductNotFoundException(prodId));

        // Update fields if new values are provided
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        // If an image is provided, update the image fields
        if (imageFile != null && !imageFile.isEmpty()) {
            existingProduct.setImageData(imageFile.getBytes());
            existingProduct.setImageType(imageFile.getContentType());
        }

        // Save and return the updated product
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(int prodId) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new ProductNotFoundException(prodId));
        productRepository.delete(product);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
}
