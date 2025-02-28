package com.rideshare.rideshareapi.booking.Review;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
@Getter @Setter
public class Review extends BaseEntity {
    private Integer ratingOutOfFive;
    private String note;
}
