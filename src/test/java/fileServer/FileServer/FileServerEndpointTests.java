package fileServer.FileServer;

import org.junit.jupiter.api.Test;
import fileServer.FileServer.controller.FileController;
import fileServer.FileServer.service.FileService;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@WebMvcTest(FileController.class)
public class FileServerEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void testGetFile() throws Exception {
        String filename = "some_test_text.txt";
        Resource mockResource = Mockito.mock(Resource.class);
        given(mockResource.getFilename()).willReturn(filename);
        given(fileService.load(filename)).willReturn(mockResource);

        mockMvc.perform(get("/files/" + filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + filename + "\""));
    }

	@Test
    void testUpload() throws Exception {
		String filename = "some_test_text.txt";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                filename,
                MediaType.TEXT_PLAIN_VALUE,
                "This project was fun! This test should pass.".getBytes()
        );

        doNothing().when(fileService).save(any(MockMultipartFile.class));

        mockMvc.perform(multipart("/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully: " + filename));
    }

}
// FileServerEndpointTests
