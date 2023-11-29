package com.Mini.Mini.dto;

import com.Mini.Mini.Entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressDto {
    private Long id;

    private String city;
    private String state;
    private String landmark;
    private String pincode;
    private String phoneNo;
    private String username;
    private String address;
    private String country;
    private String addressType;


}
