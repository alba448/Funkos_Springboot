package example.demo.storage.controllers;

import example.demo.storage.exceptions.StorageNotFound;
import example.demo.storage.services.StorageService;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class StorageControllerTest {

    @MockBean
    private StorageService storageService;

    @Autowired
    private MockMvc mvc;

    private final String endpoint = "/funkos/files";

    @MockBean
    private Resource resource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testServeFile_FileNotFound() throws Exception {
        String filename = "nonexistentfile.txt";

        when(storageService.loadAsResource(anyString())).thenThrow(new StorageNotFound("File not found"));
        MockHttpServletResponse response = mvc.perform(
                        get(endpoint + "/" + filename)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        verify(storageService, times(1)).loadAsResource(filename);
    }

   /* @Test
    void serveFile() throws Exception {
        String filename = "test-image3.png";

        InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("test-image3.png");

        assertNotNull(mockInputStream);

        Resource mockResource = mock(Resource.class);

        when(mockResource.getInputStream()).thenReturn(mockInputStream);
        when(mockResource.exists()).thenReturn(true);

        when(storageService.loadAsResource(filename)).thenReturn(mockResource);

        MockHttpServletResponse response = mvc.perform(
                        get(endpoint + "/" + filename)
                                .accept(MediaType.IMAGE_PNG))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentLength() > 0)
        );

        verify(storageService, times(1)).loadAsResource(filename);
    }

    @Test
    void serveFileThrowsException() throws Exception {
        String filename = "test-image3.png";

        Resource resource = mock(Resource.class);

        when(resource.getInputStream()).thenThrow(new IOException("No se puede determinar el tipo de fichero"));

        when(storageService.loadAsResource(filename)).thenReturn(resource);

        MockHttpServletResponse response = mvc.perform(
                        get(endpoint + "/" + filename)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("No se puede determinar el tipo de fichero"))
        );

        verify(storageService, times(1)).loadAsResource(filename);
    }*/




}
