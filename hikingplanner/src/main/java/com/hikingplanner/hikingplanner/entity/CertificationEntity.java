package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="certification")
@Table(name="certification")

public class CertificationEntity {
    
    @Id
    private String userId;
    private String email;
    private String certificationNumber;
    private boolean isCertified; // 인증 여부 플래그 추가

    // 새로운 생성자 추가
    public CertificationEntity(String userId, String email, String certificationNumber) {
        this.userId = userId;
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.isCertified = false; // 기본값으로 인증되지 않은 상태
    }

    public void markAsCertified() {
        this.isCertified = true;
    }
}
