package com.wisetime.wisetime.models.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(length = 100)
    private String street;

    @Column(length = 20)
    private String number;

    @Column(length = 50)
    private String complement;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 20)
    private String zipCode;
}
