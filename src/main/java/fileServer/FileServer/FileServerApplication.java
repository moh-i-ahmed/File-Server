package fileServer.FileServer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import fileServer.FileServer.service.FileService;

@SpringBootApplication
public class FileServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileServerApplication.class, args);
	}

	@Bean
    CommandLineRunner init(FileService fileService, ResourceLoader resourceLoader) {
        return (args) -> {
            fileService.init();

            // Load initial test files from 'src/main/resources/initialFiles'
            Resource initialFilesResource = resourceLoader.getResource("classpath:initialFiles");
            File initialFilesDir = initialFilesResource.getFile();

            // Copy initial files to the upload folder
            try (Stream<Path> paths = Files.walk(initialFilesDir.toPath())) {
                paths.filter(Files::isRegularFile).forEach(path -> {
                    File file = path.toFile();
					MultipartFile multipartFile = createMultipartFileFromFile(file);
                	fileService.save(multipartFile);
                });
            }
        };
    }

	private MultipartFile createMultipartFileFromFile(File file) {
        try {
            Path path = Paths.get(file.getAbsolutePath());
            String name = file.getName();
            String originalFileName = file.getName();
            String contentType = Files.probeContentType(path);
            byte[] content = Files.readAllBytes(path);
            return new MockMultipartFile(name, originalFileName, contentType, content);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the file. " + e.getMessage(), e);
        }
    }
}
// FileServerApplication
