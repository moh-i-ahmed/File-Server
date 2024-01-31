package fileServer.FileServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fileServer.FileServer.controller.FileController;
import fileServer.FileServer.dto.FileInfo;
import fileServer.FileServer.model.File;
import fileServer.FileServer.repository.FileRepository;
import fileServer.FileServer.service.FileService;
import jakarta.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringJUnitConfig
@WebMvcTest(FileController.class)
public class FileServerEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

	@MockBean
    private FileRepository fileRepository;

	@BeforeEach
	void setUp() {
		// mock HttpServletRequest
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}	

    @Test
    void testGetFile() throws Exception {
		// mock an exisiting resource
        String filename = "some_test_file.txt";
        Resource mockResource = Mockito.mock(Resource.class);

		// mock loading file using FileService
        given(mockResource.getFilename()).willReturn(filename);
        given(fileService.load(filename)).willReturn(mockResource);

		// verify file is present as an attachment
        mockMvc.perform(get("/files/" + filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + filename + "\""));
    }

	@Test
    void testGetListFiles() throws Exception {
		// mock creating & uploading file
		List<File> filesInRepo = new ArrayList<>();
		File mockFile = new File();
		mockFile.setName("list_files.txt");
		mockFile.setUrl("uploads/list_files.txt");
		filesInRepo.add(mockFile);
		given(fileRepository.findAll()).willReturn(filesInRepo);

		FileInfo fileInfo = new FileInfo(mockFile.getName(), MvcUriComponentsBuilder.fromMethodName(FileController.class,
				"getFile", mockFile.getName()).build().toString());

		// expect the loadAll method to return a mock file stream
		given(fileService.loadAll()).willReturn(filesInRepo.stream().map(file -> Paths.get(file.getUrl())));

		// verify file name & url are present in endpoint response
		mockMvc.perform(get("/files/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value(fileInfo.getName()))
				.andExpect(jsonPath("$[0].url").value(fileInfo.getUrl()));
    }

	@Test
    void testUploadFile() throws Exception {
		// mock creating & uploading file
		String filename = "some_test_upload.txt";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                filename,
                MediaType.TEXT_PLAIN_VALUE,
                "This project was fun! This test should pass.".getBytes()
        );
        doNothing().when(fileService).save(any(MockMultipartFile.class));

		// verify 'ok' response is received after upload
        mockMvc.perform(multipart("/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully: " + filename));
    }

}
// FileServerEndpointTests
