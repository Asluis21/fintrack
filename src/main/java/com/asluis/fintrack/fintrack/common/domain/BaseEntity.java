package com.asluis.fintrack.fintrack.common.domain;

import java.time.OffsetDateTime;

import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

      
    @Column(
        name        = "created_at", 
        nullable    = false, 
        updatable   = false
    )
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist(){

        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }


    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        Class<?> thisEffectiveClass = Hibernate.getClass(this);
        Class<?> otherEffectiveClass = Hibernate.getClass(obj);

        if (thisEffectiveClass != otherEffectiveClass) return false;

        BaseEntity other = (BaseEntity) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
    
}
