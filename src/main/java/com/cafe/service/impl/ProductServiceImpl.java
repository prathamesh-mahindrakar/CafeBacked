package com.cafe.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.cafe.JWT.JwtFilter;
import com.cafe.POJO.Category;
import com.cafe.POJO.Product;
import com.cafe.contants.CafeContants;
import com.cafe.dao.ProductDao;
import com.cafe.service.ProductService;
import com.cafe.utils.CafeUtils;
import com.cafe.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, false)) {
					productDao.save(getProductFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}

			return CafeUtils.getResponseEntity(CafeContants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
	    Category category = new Category();
	    // ✅ Correct field: "categoryId" instead of "id"
	    category.setId(Integer.parseInt(requestMap.get("categoryId")));

	    Product product = new Product();
	    if (isAdd) {
	        product.setId(Integer.parseInt(requestMap.get("id")));
	    } else {
	        product.setStatus("true");
	    }
	    product.setCategory(category);
	    product.setName(requestMap.get("name"));
	    product.setDescription(requestMap.get("description"));
	    product.setPrice(Integer.parseInt(requestMap.get("price")));
	    return product;
	}


	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> gettAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
	    try {
	        if (!jwtFilter.isAdmin()) {
	            return CafeUtils.getResponseEntity(CafeContants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
	        }

	        if (!validateProductMap(requestMap, true)) {
	            return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
	        }

	        int productId = Integer.parseInt(requestMap.get("id"));
	        Optional<Product> optionalProduct = productDao.findById(productId);

	        if (optionalProduct.isEmpty()) {
	            return CafeUtils.getResponseEntity("Product ID doesn't exist", HttpStatus.NOT_FOUND);
	        }

	        Product existingProduct = optionalProduct.get();
	        Product updatedProduct = getProductFromMap(requestMap, true);
	        updatedProduct.setStatus(existingProduct.getStatus());

	        productDao.save(updatedProduct);

	        return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@Override
	public ResponseEntity<String> deleteProduct(int id) {
		try {
			// ✅ Only admin can delete
			if (!jwtFilter.isAdmin()) {
				return CafeUtils.getResponseEntity(CafeContants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

			// ✅ Check if product exists
			Optional<Product> optionalProduct = productDao.findById(id);

			if (optionalProduct.isEmpty()) {
				return CafeUtils.getResponseEntity("Product ID doesn't exist", HttpStatus.NOT_FOUND);
			}

			// ✅ Delete product
			productDao.deleteById(id);
			return CafeUtils.getResponseEntity("Product Deleted Successfully!", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
	    try {
	        if (!jwtFilter.isAdmin()) {
	            return CafeUtils.getResponseEntity(CafeContants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
	        }

	        int productId = Integer.parseInt(requestMap.get("id"));
	        String status = requestMap.get("status");

	        Optional<Product> optionalProduct = productDao.findById(productId);
	        if (optionalProduct.isEmpty()) {
	            return CafeUtils.getResponseEntity("Product ID doesn't exist", HttpStatus.NOT_FOUND);
	        }

	        int updated = productDao.updateProductStatus(status, productId);

	        if (updated > 0) {
	            return CafeUtils.getResponseEntity("Product Status Updated Successfully", HttpStatus.OK);
	        } else {
	            return CafeUtils.getResponseEntity("Failed to update product status", HttpStatus.BAD_REQUEST);
	        }

	    } catch (Exception e) {
	        System.out.println("🔥 Error in updateStatus: " + e.getMessage());
	        e.printStackTrace();
	        return CafeUtils.getResponseEntity("Exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(int id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(int id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
