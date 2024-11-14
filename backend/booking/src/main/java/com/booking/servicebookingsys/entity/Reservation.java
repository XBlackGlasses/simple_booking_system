package com.booking.servicebookingsys.entity;

import com.booking.servicebookingsys.dto.ReservationDTO;
import com.booking.servicebookingsys.enums.ReservationStates;
import com.booking.servicebookingsys.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // enum
    private ReservationStates reservationStates;    // 預約狀態
    // enum
    private ReviewStatus reviewStatus;      // 審核狀態

    private Date bookDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)      // one user can make many reservations
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)      // if user got deleted, the reservation of the user should get deleted
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)      // one user can make many reservations
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)      // one user can make many reservations
    @JoinColumn(name = "ad_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ad ad;

    public ReservationDTO getReservationDTO() {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(id);
        dto.setServiceName(ad.getServiceName());
        dto.setBookDate(bookDate);
        dto.setReservationState(reservationStates);
        dto.setReviewStatus(reviewStatus);

        dto.setAdId(ad.getId());
        dto.setCompanyId(company.getId());
        dto.setUserName(user.getName());

        return dto;
    }
}
