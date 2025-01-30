package top.kerpoz.ecom_proj.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kerpoz.ecom_proj.exception.ProductNotFoundException;
import top.kerpoz.ecom_proj.model.Product;
import top.kerpoz.ecom_proj.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(int prodId) {
        return productRepository.findById(prodId);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageDate(imageFile.getBytes());
        return productRepository.save(product);
    }

    public Product updateProduct(int prodId, Product product, MultipartFile imageFile) throws IOException {
        product.setImageDate(imageFile.getBytes());
        product.setImageType(imageFile.getContentType());
        product.setImageType(imageFile.getContentType());
        return productRepository.save(product);
    }

    public void deleteProduct(int prodId) {
        Optional<Product> product = productRepository.findById(prodId);
        if (!product.isPresent()) {
            throw new ProductNotFoundException("Product with ID " + prodId + " not found.");
        }
        productRepository.delete(product.get());
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
}
