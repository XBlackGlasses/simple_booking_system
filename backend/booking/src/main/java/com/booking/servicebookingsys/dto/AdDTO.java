package com.booking.servicebookingsys.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdDTO {
    private Long id;
    private String serviceName;
    private String description;
    private Double price;
    private byte[] returnedImg; // we return the image als byte Array. return to front-end.
    //instead of the User we take his id
    private Long uerId;

    private String companyName;
    private MultipartFile img;  // in the post api call we will get the image in the "MultipartFile" from the Frontend application
}
