package com.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.wrapper.UserWrapper;

@CrossOrigin(origins = "*")
@RequestMapping("/user")
public interface UserRest {
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody(required = true) Map<String, String> requestMap);

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);
	
	@GetMapping("/get")
	public ResponseEntity<List<UserWrapper>> getAllUser();
	
	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap);
	
	@GetMapping("/checkToken")
	public ResponseEntity<String> checkToken();
	
	@PostMapping("/changepassword")
	public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);
	
	@PostMapping("/forgetpassword")
	public ResponseEntity<String> forgetpassword(@RequestBody Map<String, String> requestMap);

}

