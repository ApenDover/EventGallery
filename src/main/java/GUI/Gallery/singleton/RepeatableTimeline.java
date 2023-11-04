package GUI.Gallery.singleton;

import GUI.Gallery.GalleryController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.Objects;

public class RepeatableTimeline {

    private static RepeatableTimeline instance;

    private Timeline fiveSecondsWonder;

    private RepeatableTimeline() {
    }

    public RepeatableTimeline(EventHandler<ActionEvent> eventHandler) {
        fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1), eventHandler));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    public static RepeatableTimeline getInstance() {
        if (Objects.isNull(instance)) {
            throw new RuntimeException("first call have to be with eventHandler");
        }
        return instance;
    }

    public static RepeatableTimeline getInstance(EventHandler<ActionEvent> eventHandler) {
        if (Objects.isNull(instance)) {
            if (Objects.isNull(eventHandler)) {
                throw new RuntimeException("eventHandler Can't be null");
            }
            instance = new RepeatableTimeline(eventHandler);
        }
        return instance;
    }

    public void start() {
        System.out.println("fiveSecondsWonder is start");
        fiveSecondsWonder.play();
    }

    public void stop() {
        System.out.println("fiveSecondsWonder is stop");
        fiveSecondsWonder.stop();
    }


}
