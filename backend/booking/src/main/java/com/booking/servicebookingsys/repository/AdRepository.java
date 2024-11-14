package com.booking.servicebookingsys.repository;

import com.booking.servicebookingsys.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByUserId(Long userId);

    // containing is a key word.
    List<Ad> findAllByServiceNameContaining(String name);
}
