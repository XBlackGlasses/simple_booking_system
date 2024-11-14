package com.booking.servicebookingsys.entity;

import com.booking.servicebookingsys.dto.AdDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ads")
@Data
/*
service廣告資料
 */
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String description;

    private Double price;

    // save image
    @Lob    // can store large amount of data
    @Column(columnDefinition = "Longblob")
    private byte[] img;

    // create relation of this ad and user
    @ManyToOne(fetch = FetchType.LAZY, optional = false)      // one user can create many ads
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public AdDTO getAdDTO() {
        AdDTO adDTO = new AdDTO();
        adDTO.setId(id);
        adDTO.setServiceName(serviceName);
        adDTO.setDescription(description);
        adDTO.setPrice(price);
        adDTO.setCompanyName(user.getName());
        adDTO.setReturnedImg(img);

        return adDTO;
    }
}
