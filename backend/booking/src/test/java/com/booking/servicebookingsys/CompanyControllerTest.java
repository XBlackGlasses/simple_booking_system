package com.booking.servicebookingsys;

import com.booking.servicebookingsys.dto.AdDTO;
import com.booking.servicebookingsys.services.company.CompanyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)       // 不然驗證的filter會擋掉他
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;
    @Test
    public void testPostAd() throws Exception {
        Long userId = 1L;
        AdDTO adDTO = new AdDTO();
        Mockito.when(companyService.postAd(userId, adDTO)).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/company/ad/{userId}", userId)  // just tyr 1 as the pathValue
                .flashAttr("ad", adDTO);    // for set @ModelAttribute


        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
