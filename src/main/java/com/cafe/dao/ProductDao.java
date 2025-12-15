package com.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cafe.POJO.Product;
import com.cafe.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

	List<ProductWrapper> getAllProduct();

	@Transactional
	@Modifying
	@Query("UPDATE Product p SET p.status = :status WHERE p.id = :id")
	int updateProductStatus(@Param("status") String status, @Param("id") int id);

	List<ProductWrapper> getProductByCategory(@Param("id") int id);

	@Query("SELECT new com.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price) "
			+ "FROM Product p WHERE p.id = :id")
	ProductWrapper getProductById(@Param("id") int id);

}
