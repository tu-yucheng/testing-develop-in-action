package cn.tuyucheng.taketoday.resourcedirectory;

import cn.tuyucheng.taketoday.migration.junit5.extensions.TraceUnitExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReadResourceDirectoryUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceUnitExtension.class);

    @Test
    @DisplayName("givenResourcePath_whenReadAbsolutePathWithFile_thenAbsolutePathEndsWithDirectory")
    void givenResourcePath_whenReadAbsolutePathWithFile_thenAbsolutePathEndsWithDirectory() {
        String path = "src/test/resources";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        LOGGER.info(absolutePath);
        assertTrue(absolutePath.endsWith("src" + File.separator + "test" + File.separator + "resources"));
    }

    @Test
    @DisplayName("givenResourcePath_whenReadAbsolutePathWithPaths_thenAbsolutePathEndsWithDirectory")
    void givenResourcePath_whenReadAbsolutePathWithPaths_thenAbsolutePathEndsWithDirectory() {
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        LOGGER.info(absolutePath);
        assertTrue(absolutePath.endsWith("src" + File.separator + "test" + File.separator + "resources"));
    }

    @Test
    @DisplayName("giveResourceFile_whenReadResourceWithClassLoader_thenPathEndsWithFilename")
    void giveResourceFile_whenReadResourceWithClassLoader_thenPathEndsWithFilename() {
        String resourceNamae = "example_resource.txt";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceNamae)).getFile());
        String absolutePath = file.getAbsolutePath();
        LOGGER.info(absolutePath);
        assertTrue(absolutePath.endsWith(File.separator + "example_resource.txt"));
    }
}