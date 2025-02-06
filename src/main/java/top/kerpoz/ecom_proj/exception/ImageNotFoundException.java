package top.kerpoz.ecom_proj.exception;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(int prodId) {
        super(String.format("Image not found for product with ID: " + prodId));
    }
}