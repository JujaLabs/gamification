package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.TeamRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.entity.WelcomeRequest;

import java.util.List;

/*
 * @author Benjamin Novikov
 */
public interface AchievementService {
    List<String> addDaily(DailyRequest request);
    List<String> addThanks(ThanksRequest request);
    List<String> addCodenjoy(CodenjoyRequest request);
    List<String> addInterview(InterviewRequest request);
    List<String> addThanksKeeper();
    List<String> addWelcome(WelcomeRequest request);
    List<String> addTeam(TeamRequest request);
}