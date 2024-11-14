package com.booking.servicebookingsys.entity;

import com.booking.servicebookingsys.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Review {       // 顧客評論
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date reviewDate;

    private String review;

    private Long rating;    // 評分1~5

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ad_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ad ad;

    public ReviewDTO getDto(){
        ReviewDTO dto = new ReviewDTO();
        dto.setId(id);
        dto.setReview(review);
        dto.setRating(rating);
        dto.setReviewDate(reviewDate);

        dto.setUserId(user.getId());
        dto.setClientName(user.getName());

        dto.setAdId(ad.getId());
        dto.setServiceName(ad.getServiceName());

        return dto;
    }
}
