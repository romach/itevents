package org.itevents.service.crawler.integration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itevents.service.exception.IntegrationException;

/**
 * Created by vaa25 on 20.03.2016.
 */
public final class StringLoader {
    private static final Logger LOGGER = LogManager.getLogger();

    public String load(final String relative) {
        try {
            return new String(
                Files.readAllBytes(this.getPath(relative)), "UTF-8");
        } catch (final IOException exception) {
            final String message = String.format("Can't load file %s", relative);
            StringLoader.LOGGER.error(message);
            throw new IntegrationException(message, exception);
        }
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private Path getPath(final String relative) {
        try {
            return Paths
                .get(ClassLoader.getSystemResource(relative).toURI())
                .toFile()
                .toPath();
        } catch (final URISyntaxException exception) {
            final String message = String.format("Wrong filename %s", relative);
            StringLoader.LOGGER.error(message);
            throw new IntegrationException(message, exception);
        }
    }
}
