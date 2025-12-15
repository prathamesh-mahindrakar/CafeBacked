package com.cafe.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/dashboard")
public interface DashboardRest {
	
	@GetMapping("details")
	public ResponseEntity<Map<String, Object>> getCount();
}
