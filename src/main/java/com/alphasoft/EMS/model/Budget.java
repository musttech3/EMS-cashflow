package com.alphasoft.EMS.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "category_id")
    private long categoryId;


    @Column(name = "amount")
    private double amount;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;


}
