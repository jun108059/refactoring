package study.yjpark._02_duplicated_code._06_pull_up_method;

import java.io.IOException;

/**
 * 중복 코드 : 하위 클래스에 동일 코드의 메소드 올리기(Pull Up Method)
 */
public class Dashboard {

    public static void main(String[] args) throws IOException {
        ReviewerDashboard reviewerDashboard = new ReviewerDashboard();
        reviewerDashboard.printReviewers();

        ParticipantDashboard participantDashboard = new ParticipantDashboard();
        participantDashboard.printParticipants(15);
    }
}