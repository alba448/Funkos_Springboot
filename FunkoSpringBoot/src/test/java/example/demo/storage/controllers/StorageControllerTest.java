package example.demo.storage.controllers;



import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import example.demo.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;


@WebMvcTest(StorageController.class)
@ExtendWith(MockitoExtension.class)
public class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private StorageController storageController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(storageController).build();
    }

    @Test
    public void testServeFile() throws Exception {

        Resource mockResource = new ByteArrayResource("caca".getBytes());
        when(storageService.loadAsResource(anyString())).thenReturn(mockResource);


        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getServletContext().getMimeType(anyString())).thenReturn("text/plain");


        mockMvc.perform(get("/v1/storage/testfile.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("caca"));
    }
}
