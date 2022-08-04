package cn.tuyucheng.taketoday.extensions.tempdir;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SharedTemporaryDirectoryUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedTemporaryDirectoryUnitTest.class);
    @TempDir
    static Path sharedTempDir;

    @Test
    @Order(1)
    @DisplayName("givenFieldWithSharedTempDirectoryPath_whenWriteToFile_thenContentIsCorrect")
    void givenFieldWithSharedTempDirectoryPath_whenWriteToFile_thenContentIsCorrect() throws IOException {
        LOGGER.info(sharedTempDir.toString());
        Path numbers = sharedTempDir.resolve("numbers.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(numbers, lines);
        assertAll(
                () -> assertTrue(Files.exists(numbers), "File should exist"),
                () -> assertLinesMatch(lines, Files.readAllLines(numbers))
        );
        Files.createTempDirectory("bpb");
    }

    @Test
    @Order(2)
    @DisplayName("givenAlreadyWrittenToSharedFile_whenCheckContents_thenContentsIsCorrect")
    void givenAlreadyWrittenToSharedFile_whenCheckContents_thenContentsIsCorrect() throws IOException {
        Path numbers = sharedTempDir.resolve("numbers.txt");
        assertLinesMatch(Arrays.asList("1", "2", "3"), Files.readAllLines(numbers));
    }
}