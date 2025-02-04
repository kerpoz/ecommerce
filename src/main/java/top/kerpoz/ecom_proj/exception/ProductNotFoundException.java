package top.kerpoz.ecom_proj.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(int prodId) {
        super(String.format("Product with ID %d not found.", prodId));
    }
}