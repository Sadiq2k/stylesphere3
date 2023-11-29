package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {
    List<WalletTransaction> findWalletTranscationByWalletId(Long walletId);
}
