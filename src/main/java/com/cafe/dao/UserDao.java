package com.cafe.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cafe.POJO.User;
import com.cafe.wrapper.UserWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmailId(String email);

   
    List<UserWrapper> getAllUser();


    @Transactional
    @Modifying
	Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

    List<String> getAllAdmin();
    
    User findByEmail(String email);
}
