package com.cafe.rest;

import java.util.List;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.POJO.Bill;

@RequestMapping("/bill")
@CrossOrigin(origins = "*")
public interface BillRest {
	
	@PostMapping("/generateReport")
	public ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);
	
	@GetMapping("/getBills")
	public ResponseEntity<List<Bill>> getBill();
	
	@PostMapping("/getPdf")
	public ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteBill(@PathVariable int id);
	
}



