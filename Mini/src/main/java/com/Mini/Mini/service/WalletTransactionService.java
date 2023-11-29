package com.Mini.Mini.service;

import com.Mini.Mini.Entity.WalletTransaction;
import com.Mini.Mini.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletTransactionService {
    @Autowired
    WalletTransactionRepository walletTransactionRepository;

    public List<WalletTransaction> getTransactionsByWallet(Long walletId) {
       return walletTransactionRepository.findWalletTranscationByWalletId(walletId);
    }


}
