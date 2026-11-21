package org.enset.ebankservice.entities;


import jakarta.persistence.*;
import lombok.*;
import org.enset.ebankservice.model.Customer;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {


    @Id
    private String id;
    private Date createdAt;
    private String type;
    private Long customerId;
    private Double balance;

    @Transient
    private Customer customer;
}
