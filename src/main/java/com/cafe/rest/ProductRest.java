package com.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.wrapper.ProductWrapper;

@CrossOrigin(origins = "*")
@RequestMapping("/product")
public interface ProductRest {
	
	@PostMapping("/add")
	public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);
	
	@GetMapping("/get")
	public ResponseEntity<List<ProductWrapper>> getAllProduct();
	
	@PutMapping("/update")
	public ResponseEntity<String> updateProduct(@RequestBody Map<String, String> requestMap);
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id);

	@PutMapping("/updateStatus")
	public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);
	
	@GetMapping("/getByCategory/{id}")
	public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable int id);
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<ProductWrapper> getProductById(@PathVariable int id);

}

