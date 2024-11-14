package com.booking.servicebookingsys.services.company;

import com.booking.servicebookingsys.dto.AdDTO;
import com.booking.servicebookingsys.dto.ReservationDTO;
import com.booking.servicebookingsys.entity.Ad;
import com.booking.servicebookingsys.entity.Reservation;
import com.booking.servicebookingsys.entity.User;
import com.booking.servicebookingsys.enums.ReservationStates;
import com.booking.servicebookingsys.repository.AdRepository;
import com.booking.servicebookingsys.repository.ReservationRepository;
import com.booking.servicebookingsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public boolean postAd(Long userId, AdDTO adDTO) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {      // user exists in database
            Ad ad = new Ad();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setImg(adDTO.getImg().getBytes());       // getBytes 方法，需要在method後添加異常處理。(throws IOException)
            ad.setPrice(adDTO.getPrice());
            ad.setUser(optionalUser.get());

            adRepository.save(ad);

            return true;
        }

        return false;
    }

    // all ads in this company.
    public List<AdDTO> getAllAds(Long userId) {
        return adRepository.findAllByUserId(userId).stream().map(Ad::getAdDTO).collect(Collectors.toList());
    }

    public AdDTO getAdById(Long adId) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        return optionalAd.map(Ad::getAdDTO).orElse(null);

//        if(optionalAd.isPresent()) {
//            return optionalAd.get().getAdDTO();
//        }
//        return null;
    }

    public boolean updateAd(Long adId, AdDTO adDTO) throws IOException {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if(optionalAd.isPresent()) {
            Ad ad = optionalAd.get();
            ad.setServiceName(adDTO.getServiceName());
            ad.setDescription(adDTO.getDescription());
            ad.setPrice(adDTO.getPrice());

            if(adDTO.getImg() != null) {
                ad.setImg(adDTO.getImg().getBytes());
            }
            adRepository.save(ad);
            return true;
        }

        return false;
    }

    public boolean deleteAd(Long adId) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if(optionalAd.isPresent()) {
            Ad ad = optionalAd.get();
            adRepository.delete(ad);
            return true;
        }
        return false;
    }

    public List<ReservationDTO> getAllAdBookings(Long companyId){
        return reservationRepository.findAllByCompanyId(companyId)
                .stream().map(Reservation::getReservationDTO).collect(Collectors.toList());
    }

    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(bookingId);
        if(optionalReservation.isPresent()) {
            Reservation existingReservation = optionalReservation.get();
            if(Objects.equals(status, "Approve")){
                existingReservation.setReservationStates(ReservationStates.APPROVED);
            }else{
                existingReservation.setReservationStates(ReservationStates.REJECTED);
            }

            reservationRepository.save(existingReservation);
            return true;
        }
        return false;
    }

}
