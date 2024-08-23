package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(nullable = false,unique = true,columnDefinition = "VARCHAR(225)")
    @NotEmpty
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private List<User> users;



}
