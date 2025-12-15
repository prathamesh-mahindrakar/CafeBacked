package com.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.wrapper.ProductWrapper;

public interface ProductService {

	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

	ResponseEntity<List<ProductWrapper>> gettAllProduct();

	ResponseEntity<String> updateProduct(Map<String, String> requestMap);

	ResponseEntity<String> deleteProduct(int id);

	ResponseEntity<String> updateStatus(Map<String, String> requestMap);

	ResponseEntity<List<ProductWrapper>> getByCategory(int id);

	ResponseEntity<ProductWrapper> getProductById(int id);

}
