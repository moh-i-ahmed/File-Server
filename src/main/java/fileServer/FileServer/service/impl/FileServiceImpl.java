@ -1,87 +0,0 @@
package fileserver.FileServer.service.impl;

import fileserver.FileServer.model.File;
import fileserver.FileServer.repository.FileRepository;
import fileserver.FileServer.service.FileService;
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
            Files.createDirectories(root);
            System.out.println("FileService initialized!");
        } catch (IOException  e) {
            System.err.println("Could not initialize FileService!");
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
            System.out.println("File saved!");
        } catch (Exception e) {
            log.error("Could not store the file. Error: ", e);
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public UrlResource load(String filename) {
        Boolean success = false;
        try {
            Path file = root.resolve(filename);
            UrlResource resource = new UrlResource(file.toUri());
            System.out.println("File loaded!");
            return resource;

            // if (!resource.exists() || !resource.isReadable()) {
            //     log.error("Could not load, file does not exist!");
            //     throw new RuntimeException("Could not load, file does not exist!");
            // } else {
            //     success = true;
            // }
            // System.out.println("File loaded!");
            // return resource;
        } catch (MalformedURLException  e) {
            System.err.println("Could not load file!");
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            System.out.println("All files loaded!");
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException  e) {
            log.error("Could not load all files!", e);
            throw new RuntimeException("Could not load the files!", e);
        }
    }
}
// FileServiceImpl