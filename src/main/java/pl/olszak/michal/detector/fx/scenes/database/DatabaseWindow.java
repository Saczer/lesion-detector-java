package pl.olszak.michal.detector.fx.scenes.database;

import com.jfoenix.controls.JFXTextField;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import pl.olszak.michal.detector.controller.creator.MapCreator;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.util.Optional;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
public class DatabaseWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(DatabaseWindow.class);

    @FXML
    private JFXTextField maskResourcesText;
    @FXML
    private JFXTextField imageResourcesText;

    @Autowired
    private MapCreator probabilityMapCreator;
    @Autowired
    private DatabaseWindowContext model;

    public DatabaseWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }


    @FXML
    public void initialize() {
        maskResourcesText.setText(model.getMaskFolder());
        imageResourcesText.setText(model.getImageResourcesFolder());
    }


    @FXML
    public void onOpenTrainResourcesFolder() {
        logger.info("Open training resources folder");
        Optional<File> imageResources = DialogUtils.openFolder("Training Resources Folder", screensConfiguration.getStage());
        if (imageResources.isPresent()) {
            File file = imageResources.get();
            model.setImageResourcesFolder(file.getAbsolutePath());
            imageResourcesText.setText(file.getAbsolutePath());
        }

    }

    @FXML
    public void onOpenMaskFolder() {
        logger.info("Open mask folder");
        Optional<File> maskResources = DialogUtils.openFolder("Mask Resources", screensConfiguration.getStage());
        if (maskResources.isPresent()) {
            File file = maskResources.get();
            model.setMaskFolder(file.getAbsolutePath());
            maskResourcesText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void onProcessTraining() {
        //todo move the multithreading logic to controller
        // TODO: 28.06.2017 dorobić możliwość redukcji kanałów
        if (StringUtils.isEmpty(model.getImageResourcesFolder()) ||
                StringUtils.isEmpty(model.getMaskFolder())) {
            logger.error("Could not process set, the folders are not chosen");
            return;
        }
        logger.info("Process training");
        model.setLoading(true);
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe((action) -> {
                    probabilityMapCreator.process(ColorReduce.BINS_PER_CHANNEL_256, model);
                    model.setLoading(false);
                });
    }
}