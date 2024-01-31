package fileServer.FileServer.service.impl;

import fileServer.FileServer.model.File;
import fileServer.FileServer.repository.FileRepository;
import fileServer.FileServer.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            // create uploads directory at project root
            Files.createDirectories(root);
            log.info("FileService initialized!");
        } catch (IOException  e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            
            // Save file metadata in the database
            File fileEntity = new File();
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setUrl(this.root.resolve(file.getOriginalFilename()).toString());
            fileRepository.save(fileEntity);
            log.info("File saved!");
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            log.info("File loaded!");

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not load, file does not exist!");
            } else {
                log.info("Loaded file:" + resource.getFilename());
                return resource;
            }
        } catch (MalformedURLException  e) {
            throw new RuntimeException("Could not load the file. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            log.info("All files loaded!");
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException  e) {
            throw new RuntimeException("Could not load all files!", e);
        }
    }
}
// FileServiceImpl
