package study.yjpark._03_long_function._10_replace_function_with_command;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyDashboard {

    private final int totalNumberOfEvents;

    public StudyDashboard(int totalNumberOfEvents) {
        this.totalNumberOfEvents = totalNumberOfEvents;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        StudyDashboard studyDashboard = new StudyDashboard(15);
        studyDashboard.print();
    }

    private void print() throws IOException, InterruptedException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        List<Participant> participants = new CopyOnWriteArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(totalNumberOfEvents);

        // 15개의 이슈에 댓글이 있는지 확인
        for (int index = 1; index <= totalNumberOfEvents; index++) {
            int eventId = index;
            service.execute(() -> {
                try {
                    GHIssue issue = repository.getIssue(eventId);
                    List<GHIssueComment> comments = issue.getComments();

                    for (GHIssueComment comment : comments) {
                        Participant participant = findParticipant(participants, comment.getUserName());
                        participant.setHomeworkDone(eventId);
                    }

                    latch.countDown();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            });
        }

        latch.await();
        service.shutdown();

        // Markdown 파일로 출력
        new StudyPrinter(this.totalNumberOfEvents, participants).execute();
    }

    private static Participant findParticipant(List<Participant> participants, String username) {
        boolean isNewUser = participants.stream().noneMatch(p -> p.username().equals(username));
        Participant participant;
        if (isNewUser) {
            participant = new Participant(username);
            participants.add(participant);
        } else {
            participant = participants.stream().filter(p -> p.username().equals(username)).findFirst().orElseThrow();
        }
        return participant;
    }
}
