package com.rideshare.rideshareapi.comman.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass  // don't crete table for BaseClass class
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @CreatedDate    // hibernate
    @Temporal(TemporalType.TIMESTAMP) //JPA
    @Column(updatable = false)
    @CreatedDate
    private Date createdAt;

    //    @LastModifiedDate   // hibernate
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

//    @PrePersist  //create time - whenever id is assigned
//    @PreUpdate   // whenever a change is pushed to the DB
//    void updateTimeStamp(){}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  //same object
        //check the class of both
        if (o == null || getClass() != o.getClass()) return false;
        //cast the object
        BaseEntity that = (BaseEntity) o;
        //compare the ids
        if(id==null || that.id==null)
            return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id==null?0:Objects.hashCode(id);
    }
}
