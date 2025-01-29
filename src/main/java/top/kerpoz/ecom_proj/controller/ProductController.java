package top.kerpoz.ecom_proj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kerpoz.ecom_proj.model.Product;
import top.kerpoz.ecom_proj.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping({"products", "products/"})
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = service.getProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProduct(@PathVariable int prodId) {
        Optional<Product> product = service.getProduct(prodId);
        return service.getProduct(prodId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try{
            Product newProduct = service.addProduct(product, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int prodId) {
        Optional<Product> product = service.getProduct(prodId);
        return product.map(value -> ResponseEntity.ok(value.getImageDate()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}