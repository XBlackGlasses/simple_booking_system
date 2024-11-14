package com.booking.servicebookingsys.services.client;

import com.booking.servicebookingsys.dto.AdDTO;
import com.booking.servicebookingsys.dto.AdDetailsForClientDTO;
import com.booking.servicebookingsys.dto.ReservationDTO;
import com.booking.servicebookingsys.dto.ReviewDTO;
import com.booking.servicebookingsys.entity.Ad;
import com.booking.servicebookingsys.entity.Reservation;
import com.booking.servicebookingsys.entity.Review;
import com.booking.servicebookingsys.entity.User;
import com.booking.servicebookingsys.enums.ReservationStates;
import com.booking.servicebookingsys.enums.ReviewStatus;
import com.booking.servicebookingsys.repository.AdRepository;
import com.booking.servicebookingsys.repository.ReservationRepository;
import com.booking.servicebookingsys.repository.ReviewRepository;
import com.booking.servicebookingsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    public List<AdDTO> getAllAds(){
        return adRepository.findAll().stream().map(Ad::getAdDTO).collect(Collectors.toList());
    }

    public List<AdDTO> searchAdByName(String name){
        return adRepository.findAllByServiceNameContaining(name).stream().map(Ad::getAdDTO).collect(Collectors.toList());
    }

    // client books a service
    public boolean boolService(ReservationDTO reservationDTO){
        Optional<Ad> optionalAd = adRepository.findById(reservationDTO.getAdId());
        Optional<User> optionalUser = userRepository.findById(reservationDTO.getUserId());
        if(optionalAd.isPresent() && optionalUser.isPresent()){
            Reservation reservation = new Reservation();
            reservation.setBookDate(reservationDTO.getBookDate());
            reservation.setReservationStates(ReservationStates.PENDING);    // 代辦
            reservation.setUser(optionalUser.get());

            reservation.setAd(optionalAd.get());
            reservation.setCompany(optionalAd.get().getUser());     // user company saved in ad entity.
            reservation.setReviewStatus(ReviewStatus.FALSE);    // 未審核

            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }

    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId){
        Optional<Ad> optionalAd = adRepository.findById(adId);
        AdDetailsForClientDTO adDetailsForClientDTO = new AdDetailsForClientDTO();
        if(optionalAd.isPresent()){
            adDetailsForClientDTO.setAdDTO(optionalAd.get().getAdDTO());

            List<Review> reviewList = reviewRepository.findAllByAdId(adId);
            // 顧客評論 about the ad service
            adDetailsForClientDTO.setReviewDTOList(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));
        }

        return adDetailsForClientDTO;
    }

    public List<ReservationDTO> getAllReservationsByUserId(Long userId){
        return reservationRepository.findAllByUserId(userId).stream().map(Reservation::getReservationDTO).collect(Collectors.toList());
    }

    public Boolean giveReview(ReviewDTO reviewDTO){
        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());
        Optional<Reservation> optionalBooking = reservationRepository.findById(reviewDTO.getBookId());

        if(optionalUser.isPresent() && optionalBooking.isPresent()){
            Review review = new Review();
            review.setReviewDate(new Date());
            review.setReview(reviewDTO.getReview());
            review.setRating(reviewDTO.getRating());
            review.setUser(optionalUser.get());
            review.setAd(optionalBooking.get().getAd());

            reviewRepository.save(review);

            Reservation booking = optionalBooking.get();
            booking.setReviewStatus(ReviewStatus.TRUE);
            reservationRepository.save(booking);

            return true;
        }
        return false;
    }
}
