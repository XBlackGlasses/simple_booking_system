package com.booking.servicebookingsys.repository;

import com.booking.servicebookingsys.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByAdId(Long adId);

}
