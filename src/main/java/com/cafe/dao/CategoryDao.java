package com.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafe.POJO.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer>{

	List<Category> getAllCategories();
}
