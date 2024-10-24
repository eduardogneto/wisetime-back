package com.wisetime.wisetime.models.certificate;

import java.time.LocalDate;

import com.wisetime.wisetime.models.request.Request;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    private String justification;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    @Enumerated(EnumType.STRING)
    private CertificateStatusEnum status;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
