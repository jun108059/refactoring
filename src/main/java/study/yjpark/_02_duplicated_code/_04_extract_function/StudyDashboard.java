package study.yjpark._02_duplicated_code._04_extract_function;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 중복 코드 : 함수 추출하기(Extract Function)
 */
public class StudyDashboard {

    private void printParticipants(int eventId) throws IOException {
        GHIssue issue = getGhIssue(eventId);
        Set<String> participants = getUsernames(issue);
        print(participants);
    }

    private static void print(Set<String> participants) {
        participants.forEach(System.out::println);
    }

    private static Set<String> getUsernames(GHIssue issue) throws IOException {
        Set<String> usernames = new HashSet<>();
        issue.getComments().forEach(c -> usernames.add(c.getUserName()));
        return usernames;
    }

    private static GHIssue getGhIssue(int eventId) throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(eventId);
        return issue;
    }

    private void printReviewers() throws IOException {
        GHIssue issue = getGhIssue(30);
        Set<String> reviewers = getUsernames(issue);
        print(reviewers);
    }

    public static void main(String[] args) throws IOException {
        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.printReviewers();
        studyDashboard.printParticipants(15);
    }

}
