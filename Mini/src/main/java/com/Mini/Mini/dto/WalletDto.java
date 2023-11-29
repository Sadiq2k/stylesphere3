package com.Mini.Mini.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalletDto {

    private Long id;
    private double walletAmount;
}
