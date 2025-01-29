package top.kerpoz.ecom_proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.kerpoz.ecom_proj.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}