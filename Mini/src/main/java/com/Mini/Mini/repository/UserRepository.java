package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.User;

import com.Mini.Mini.Entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);

    User findUserByfirstname(String firstname);
    Optional<User> findByEmail(String email);

    User findUserPasswordByEmail(String email);

//    User findReferralCode(String referralCode);

    User findUserByReferralCode(String referralCode);

    Boolean existsByReferralCode(String referralCode);

}
