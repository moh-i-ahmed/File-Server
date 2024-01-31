package fileserver.FileServer.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileService {
    void init();
    void save(MultipartFile file);
    UrlResource load(String filename);
    Stream<Path> loadAll();  
}
// FileService
