package example.demo.storage.services;

import example.demo.storage.exceptions.StorageBadRequest;
import example.demo.storage.exceptions.StorageInternal;
import example.demo.storage.exceptions.StorageNotFound;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class StorageServiceImplTest {

    private final String rootLocationPath = "upload-dir";
    private Path rootLocation;

    @Mock
    private Path mockPath;

    @InjectMocks
    private StorageServiceImpl storageService;

    @BeforeEach
    void setUp() {
        rootLocation = Paths.get(rootLocationPath);
        storageService = new StorageServiceImpl(rootLocationPath);
    }

    // Casos correctos
    @Test
    void testStore() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        String storedFilename = storageService.store(file);

        assertNotNull(storedFilename);
        assertTrue(storedFilename.endsWith("_test.txt"));
    }

    @Test
    void testLoadAll() throws IOException {
        Files.createDirectories(rootLocation);
        Files.createFile(rootLocation.resolve("test.txt"));

        Stream<Path> files = storageService.loadAll();

        assertNotNull(files);
        assertTrue(files.anyMatch(path -> path.equals(Paths.get("test.txt"))));
    }

    @Test
    void testLoad() {
        String filename = "test.txt";
        Path path = storageService.load(filename);
        assertEquals(rootLocation.resolve(filename), path);
    }

    @Test
    void testLoadAsResource() throws IOException {
        String filename = "test.txt";
        Files.createFile(rootLocation.resolve(filename));

        Resource resource = storageService.loadAsResource(filename);

        assertNotNull(resource);
        assertEquals(filename, resource.getFilename());
    }

    @Test
    void testDelete() throws IOException {
        String filename = "test.txt";
        Files.createFile(rootLocation.resolve(filename));

        storageService.delete(filename);

        assertFalse(Files.exists(rootLocation.resolve(filename)));
    }

    @Test
    void testInit() throws IOException {
        storageService.init();

        assertTrue(Files.exists(rootLocation));
    }

    @Test
    void testDeleteAll() throws IOException {
        Files.createDirectories(rootLocation);
        Files.createFile(rootLocation.resolve("test.txt"));

        storageService.deleteAll();

        assertFalse(Files.exists(rootLocation.resolve("test.txt")));
    }

    @Test
    void testGetUrl() {
        String filename = "test.txt";
        String url = storageService.getUrl(filename);

        assertNotNull(url);
        assertTrue(url.contains("/v1/storage/test.txt"));
    }

    // Casos incorrectos
    @Test
    void testStore_EmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        Exception exception = assertThrows(StorageBadRequest.class, () -> storageService.store(file));

        assertTrue(exception.getMessage().contains("Fichero vacío"));
    }

    @Test
    void testStore_FilenameWithRelativePath() {
        MockMultipartFile file = new MockMultipartFile("file", "../test.txt", "text/plain", "Test content".getBytes());

        Exception exception = assertThrows(StorageBadRequest.class, () -> storageService.store(file));

        assertTrue(exception.getMessage().contains("No se puede almacenar un fichero con una ruta relativa fuera del directorio actual"));
    }

    @Test
    void testLoadAsResource_FileNotFound() {
        String filename = "non_existent.txt";

        Exception exception = assertThrows(StorageNotFound.class, () -> storageService.loadAsResource(filename));

        assertTrue(exception.getMessage().contains("No se puede leer fichero"));
    }

    @Test
    void testDelete_FileNotFound() {
        String filename = "non_existent.txt";

        Exception exception = assertThrows(StorageInternal.class, () -> storageService.delete(filename));

        assertTrue(exception.getMessage().contains("No se puede eliminar el fichero"));
    }
}
