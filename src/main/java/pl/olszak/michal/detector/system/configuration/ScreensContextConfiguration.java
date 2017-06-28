package pl.olszak.michal.detector.system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.olszak.michal.detector.fx.scenes.database.DatabaseWindowContext;
import pl.olszak.michal.detector.fx.scenes.segmentation.SegmentationWindowContext;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
@Configuration
@Import(ScreensConfiguration.class)
public class ScreensContextConfiguration {

    @Bean
    DatabaseWindowContext databaseWindowContext(){
        return new DatabaseWindowContext();
    }

    @Bean
    SegmentationWindowContext segmentationWindowContext() {
        return new SegmentationWindowContext();
    }
}
