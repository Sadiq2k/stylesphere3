package com.Mini.Mini.service;

import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Util.EmailUtil;
import com.Mini.Mini.Util.OtpUtil;
import com.Mini.Mini.repository.RoleRepository;
import com.Mini.Mini.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;

  private final UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    public void removeUserById(Long id){
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email){
      return  userRepository.findUserByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);

    }

    public User findUserByfirstname(String firstname) {
    return  userRepository.findUserByfirstname(firstname);
    }

    public UserService(UserRepository userRepository){

        this.userRepository = userRepository;
    }


    public User getPasswordByEmail(String email) {
        return userRepository.findUserPasswordByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }


    public Boolean isValidReferralCode(String referralCode) {
        return userRepository.existsByReferralCode(referralCode);
    }

    public User getUserByReferralCode(String referralCode) {
        return userRepository.findUserByReferralCode(referralCode);
    }


}
