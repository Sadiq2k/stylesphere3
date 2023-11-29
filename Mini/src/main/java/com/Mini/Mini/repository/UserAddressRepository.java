package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {

    List<UserAddress> findByUserId(Long userId);

}
