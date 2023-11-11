package gui.gallery.singleton;

import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ExecutorServiceSingleton {

    private static ExecutorServiceSingleton instance;

    @Getter
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private ExecutorServiceSingleton() {
    }

    public static ExecutorServiceSingleton getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ExecutorServiceSingleton();
        }
        return instance;
    }

    public void stop() {
        executorService.shutdown();
    }


}
