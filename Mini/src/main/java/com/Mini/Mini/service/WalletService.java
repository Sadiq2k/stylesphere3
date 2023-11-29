package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Wallet;
import com.Mini.Mini.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
    public Boolean findUserByWallet(Long id){
      return   walletRepository.existsByUserId(id);
    }

}
