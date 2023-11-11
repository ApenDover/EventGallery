package gui.gallery.runnable;

import gui.gallery.utils.LoadAllFiles;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class LoadProcessor implements Runnable {

    @Override
    public void run() {
        log.info(LocalTime.now() + " - start");
        LoadAllFiles.load();
        log.info(LocalTime.now() + " - end");
    }

}
