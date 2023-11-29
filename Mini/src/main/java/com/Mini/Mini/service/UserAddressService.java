package com.Mini.Mini.service;

import com.Mini.Mini.Entity.UserAddress;
import com.Mini.Mini.dto.UserAddressDto;
import com.Mini.Mini.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAddressService {


  private final UserAddressRepository userAddressRepository;



    @Autowired
    public UserAddressService(UserAddressRepository userAddressRepository){
        this.userAddressRepository = userAddressRepository;
    }

    public void saveUserAddress(UserAddress userAddress){
        userAddressRepository.save(userAddress);
    }

    public List<UserAddress> getAllAddresses() {
        return userAddressRepository.findAll();
    }

    public List<UserAddress> getUserAddressesByUserId(Long userId) {
        return userAddressRepository.findByUserId(userId);
    }

    public Optional<UserAddress> findById(Long id) {
        return userAddressRepository.findById(id);
    }

    public void removeUserAddressById(Long id) {
        userAddressRepository.deleteById(id);
    }

    public Optional<UserAddress> getUserAddressById(Long id) {
        return userAddressRepository.findById(id);
    }



}
