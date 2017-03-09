package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementService.class)
public class UserServiceTest {

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private AchievementRepository repository;

    @Test
    public void getUserAchievementsDetails() throws Exception {

    }

    @Test
    public void getAllUsersWithPointSum() throws Exception {

    }
}