package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Boolean existsByUserId(Long id);

    Wallet findWalletByUserId(Long id);


    Wallet getWalletByUserId(Long id);
}
