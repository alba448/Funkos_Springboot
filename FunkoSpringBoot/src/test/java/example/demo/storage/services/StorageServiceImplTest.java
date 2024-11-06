package example.demo.storage.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;



import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@TestPropertySource(properties = "upload.root-location=upload-dir")
public class StorageServiceImplTest {

    @Value("${upload.root-location}")
    private String rootLocationPath;

    @Mock
    private Path rootLocation;

    @InjectMocks
    private StorageServiceImpl storageService;

    @BeforeEach
    public void setUp() {
        rootLocation = Paths.get(rootLocationPath);
        storageService = new StorageServiceImpl(rootLocationPath);
    }

    @Test
    public void testStore() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        String storedFilename = storageService.store(file);

        assertNotNull(storedFilename);
        assertTrue(storedFilename.endsWith("_test.txt"));
    }

    @Test
    public void testLoadAll() throws IOException {
        Path testPath = rootLocation.resolve("test.txt");
        Files.createDirectories(rootLocation);
        Files.createFile(testPath);

        Stream<Path> files = storageService.loadAll();

        assertNotNull(files);
        assertTrue(files.anyMatch(path -> path.equals(testPath.getFileName())));

        Files.deleteIfExists(testPath);
    }

    @Test
    public void testLoad() {
        String filename = "test.txt";
        Path path = storageService.load(filename);
        assertEquals(rootLocation.resolve(filename), path);
    }

    @Test
    public void testLoadAsResource() throws MalformedURLException {
        String filename = "test.txt";
        Path testPath = rootLocation.resolve(filename);
        Resource resource = new ByteArrayResource("Test content".getBytes());

        when(rootLocation.resolve(filename)).thenReturn(testPath);

        Resource loadedResource = storageService.loadAsResource(filename);

        assertNotNull(loadedResource);
        assertEquals(resource.getFilename(), loadedResource.getFilename());
    }

    @Test
    public void testDeleteAll() throws IOException {
        File rootFile = mock(File.class);
        when(rootLocation.toFile()).thenReturn(rootFile);

        doNothing().when(rootFile).delete();
        storageService.deleteAll();

        verify(rootFile, times(1)).delete();
    }

    @Test
    public void testInit() throws IOException {
        doNothing().when(Files.class);
        storageService.init();

        verify(Files.class);
    }

    @Test
    public void testDelete() throws IOException {
        String filename = "test.txt";
        Path path = storageService.load(filename);
        Files.createFile(path);

        storageService.delete(filename);

        assertFalse(Files.exists(path));
    }

    @Test
    public void testGetUrl() {
        String filename = "test.txt";
        String url = storageService.getUrl(filename);

        assertNotNull(url);
        assertTrue(url.contains("/v1/storage/test.txt"));
    }
}
