package com.booking.servicebookingsys.services.client;

import com.booking.servicebookingsys.dto.AdDTO;
import com.booking.servicebookingsys.dto.AdDetailsForClientDTO;
import com.booking.servicebookingsys.dto.ReservationDTO;
import com.booking.servicebookingsys.dto.ReviewDTO;

import java.util.List;

public interface ClientService {

    List<AdDTO> getAllAds();

    List<AdDTO> searchAdByName(String name);

    boolean boolService(ReservationDTO reservationDTO);

    AdDetailsForClientDTO getAdDetailsByAdId(Long adId);

    List<ReservationDTO> getAllReservationsByUserId(Long userId);

    Boolean giveReview(ReviewDTO reviewDTO);
}
