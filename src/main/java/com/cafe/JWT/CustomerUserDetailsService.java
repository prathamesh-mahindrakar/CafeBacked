package com.cafe.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cafe.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	private com.cafe.POJO.User userDetail;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		userDetail = userDao.findByEmailId(email);
		if (!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User Not Found");
		}
	}
	
	public com.cafe.POJO.User getUserDetail(){
		return userDetail;
	}
	
	

}
