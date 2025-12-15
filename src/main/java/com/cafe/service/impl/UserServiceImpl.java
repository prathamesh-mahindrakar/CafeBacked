package com.cafe.service.impl;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.JWT.CustomerUserDetailsService;
import com.cafe.JWT.JWTUtil;
import com.cafe.JWT.JwtFilter;
import com.cafe.POJO.User;
import com.cafe.contants.CafeContants;
import com.cafe.dao.UserDao;
import com.cafe.service.UserService;
import com.cafe.utils.CafeUtils;
import com.cafe.utils.EmailUtils;
import com.cafe.wrapper.UserWrapper;
import com.google.common.base.Strings;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;

	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
				&& requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false"); // hardcoded value
		user.setRole("user"); // hardcoded value
		return user;
	}

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {

			if (validateSignUpMap(requestMap)) {
				User user = userDao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					userDao.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("SuccessFully Register", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("Email Already Exits .", HttpStatus.BAD_REQUEST);
				}

			} else {
				return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					String token = jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
							customerUserDetailsService.getUserDetail().getRole());
					return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
				} else {
					return new ResponseEntity<>("{\"message\":\"Wait for Admin Approval.\"}", HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if (jwtFilter.isAdmin()) {

				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);

			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
					return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User id Doesn't Exists", HttpStatus.OK);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeContants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

		allAdmin.remove(jwtFilter.getCurrentUser());

		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved",
					"USER:- " + user + " \n is approved by \nADMIN:-", allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled",
					"USER:- " + user + " \n is disabled by \nADMIN:-", allAdmin);

		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User user = userDao.findByEmail(jwtFilter.getCurrentUser());
			if (!user.equals(null)) {
				if (user.getPassword().equals(requestMap.get("oldPassword"))) {
					user.setPassword(requestMap.get("newPassword"));
					userDao.save(user);
					return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgetpassword(Map<String, String> requestMap) {
		try {
			User user = userDao.findByEmail(requestMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
				emailUtils.forgetEmail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
			}
			return CafeUtils.getResponseEntity("Check your Email For Credentials", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
