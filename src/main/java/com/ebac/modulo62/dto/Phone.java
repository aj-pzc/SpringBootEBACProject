package com.ebac.modulo62.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "telefonos")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPhone;

    private String phoneType;
    private int lada;
    private String number;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @JsonBackReference
    private User user;
}
