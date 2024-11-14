package com.booking.servicebookingsys;

import com.booking.servicebookingsys.entity.Ad;
import com.booking.servicebookingsys.entity.User;
import com.booking.servicebookingsys.repository.AdRepository;
import com.booking.servicebookingsys.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import org.slf4j.Logger;


@SpringBootTest
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AdRepository adRepository;

    // logging example
    Logger logger = LoggerFactory.getLogger(RepositoryTest.class);

    @Transactional
    @Test
    public void testFindByEmail(){
        User user = new User();
        user.setEmail("test@email.com");
        user.setName("test");
        userRepository.save(user);

        Assertions.assertEquals(user, userRepository.findFirstByEmail("test@email.com"));
        //System.out.println(userRepository.findFirstByEmail("test@email.com"));

        /*
            logger 有不同level： trace < debug < info < warn < error
            spring boot 預設 info 以上才會 show 出來，
            可以在application.properties修改預設
         */
        logger.debug("DataBase Test in debug level : {}", userRepository.findFirstByEmail("test@email.com").toString());
        logger.info("DataBase Test in info level : {}", userRepository.findFirstByEmail("test@email.com").toString());
        logger.warn("DataBase Test in warn level : {}", userRepository.findFirstByEmail("test@email.com").toString());

    }

    @Transactional
    @Test
    public void TestAdRepository(){
        List<Ad> ads = adRepository.findAllByUserId(19L);
        System.out.println(ads.get(0).getServiceName());
    }
}
