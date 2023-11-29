package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @Id
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @GeneratedValue(generator = "uuid2")
//    @Column(name = "id", unique = true, nullable = false)
//    private UUID id;

    @Column(name = "city")
    @NotBlank(message = "City is required")
    private String city;


    @Column(name = "State")
    @NotBlank(message = "State is required")
    private String state;

    @Column(name = "Landmark")
    @NotBlank(message = "Landmark is required")
    private String landmark;

    @Column(name = "Pincode")
    @Size(min = 6, max = 6)
    @Pattern(regexp = "\\d{6}", message = "PinCode must be a 6-digit number")
    private String pincode;


    @Column(name = "phone number")
    @Size(min = 1, message = "is required")
    private String phoneNo;

    @Column(name = "username")
    @Size(min = 1, message = "is required")
    private String username;

    @Column(name = "country")
    @Size(min = 1, message = "is required")
    private String country;

    @Column(name = "addressType")
    @NotBlank(message = "Select addressType")
    private String addressType;

    @Column(name = "Address")
    @NotBlank(message = "Address is required")
    private String address;


    @ManyToOne
    private User user;

    public UserAddress(){

    }
    @Override
    public String toString() {
        return
                "City: " + city +  "\n" +
                "State: " + state +  "\n" +
                "Landmark: " + landmark +  "\n" +
                "Pincode: " + pincode +  "\n" +
                "PhoneNo: " + phoneNo +  "\n" +
                "Country: " + country +  "\n" +
                "AddressType: " + addressType + "\n" +
                "Address: " + address;
    }

}
