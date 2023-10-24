package GUI.Gallery.configuration;

import javafx.stage.FileChooser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public FileChooser fileChooser() {
        return new FileChooser();
    }

}
