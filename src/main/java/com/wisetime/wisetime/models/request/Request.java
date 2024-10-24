package com.wisetime.wisetime.models.request;

import java.util.List;

import com.wisetime.wisetime.models.certificate.Certificate;
import com.wisetime.wisetime.models.organization.Organization;
import com.wisetime.wisetime.models.punch.TemporaryPunch;
import com.wisetime.wisetime.models.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestTypeEnum requestType;

    @Column(name = "justification")
    private String justification;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;

    @OneToMany(mappedBy = "request")
    private List<TemporaryPunch> temporaryPunches;
    
    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    private Certificate certificate;

}

