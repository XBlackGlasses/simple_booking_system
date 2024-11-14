package com.booking.servicebookingsys.dto;

import com.booking.servicebookingsys.enums.ReservationStates;
import com.booking.servicebookingsys.enums.ReviewStatus;
import lombok.Data;

import java.util.Date;

@Data       // get getter and setter
public class ReservationDTO {
    private Long id;

    private Date bookDate;

    private String serviceName;

    private ReservationStates reservationState;

    private ReviewStatus reviewStatus;

    private Long userId;

    private String userName;

    private Long companyId;

    private Long adId;
}
