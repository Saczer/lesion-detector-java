package pl.olszak.michal.detector.core.converter;

import io.reactivex.Single;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.model.file.AbstractImageFile;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFileFactory;

import java.util.List;

/**
 * @author molszak
 * created on 27.03.2017.
 */
public class ConvertedContainerCreator {

    private final Logger logger = LoggerFactory.getLogger(ConvertedContainer.class);

    private final ImageConverter converter;

    public ConvertedContainerCreator(ImageConverter converter) {
        this.converter = converter;
    }

    public Single<ConvertedContainer> createColoredContainer(final List<AbstractImageFile> files) {
        return Single.create(e -> {
            final ConvertedContainer container = new ConvertedContainer();
            files.forEach(image -> {
                ConvertedFile converted = ConvertedFileFactory.createColored(image, converter);
                container.put(FilenameUtils.getBaseName(image.getFileName()), converted);
            });

            logger.info("Created colored converted container");
            e.onSuccess(container);
        });
    }

    public Single<ConvertedContainer> createThresholds(final List<AbstractImageFile> files, final int threshold, final boolean constantThreshold) {
        return Single.create(e -> {
            final ConvertedContainer container = new ConvertedContainer();
            files.forEach(image -> {
                ConvertedFile converted = ConvertedFileFactory.createThreshold(image, converter, threshold, constantThreshold);
                container.put(FilenameUtils.getBaseName(image.getFileName()), converted);
            });
            logger.info("Created threshold converted container");
            e.onSuccess(container);
        });
    }
}

