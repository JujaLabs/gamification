package juja.microservices.gamification.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RequestValidatorTest {

    private RequestValidator validator;

    @Mock
    private Appender mockAppender;
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingMessage;

    @Before
    public void setUp() {
        validator = new RequestValidator();
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @Test
    public void dailyRequestTest() {
        //given
        DailyRequest request = new DailyRequest("Max", "daily request");

        //when
        validator.checkDaily(request);

        //then
        verify(mockAppender).doAppend(captorLoggingMessage.capture());
        final LoggingEvent event = captorLoggingMessage.getValue();
        assertThat(event.getLevel(), is(Level.INFO));
        assertThat(event.getFormattedMessage(), is("Daily request successfully checked"));
    }

    @Test (expected = UnsupportedAchievementException.class)
    public void nullRequestTest() {
        //when
        validator.checkDaily(null);
        fail();
    }

    @Test (expected = UnsupportedAchievementException.class)
    public void emptyFromTest() {
        //given
        DailyRequest request = new DailyRequest("","daily description");

        //when
        validator.checkDaily(request);
        fail();
    }

    @Test (expected = UnsupportedAchievementException.class)
    public void emptyDescriptionTest() {
        //given
        DailyRequest request = new DailyRequest("Max","");

        //when
        validator.checkDaily(request);
        fail();
    }

    @Test
    public void thanksRequestTest() {
        //given
        ThanksRequest request = new ThanksRequest("Max", "Bob", "Thanks for good job!");

        //when
        validator.checkThanks(request);

        //then
        verify(mockAppender).doAppend(captorLoggingMessage.capture());
        final LoggingEvent event = captorLoggingMessage.getValue();
        assertThat(event.getLevel(), is(Level.INFO));
        assertThat(event.getFormattedMessage(), is("Thanks request successfully checked"));
    }

    @Test (expected = UnsupportedAchievementException.class)
    public void emptyToTest() {
        //given
        ThanksRequest request = new ThanksRequest("Max", "", "Thanks for good job!");

        //when
        validator.checkThanks(request);
        fail();
    }

    @Test (expected = UnsupportedAchievementException.class)
    public void thanksYourselfTest() {
        //given
        ThanksRequest request = new ThanksRequest("Max", "Max", "Thanks for good job!");

        //when
        validator.checkThanks(request);
        fail();
    }
}