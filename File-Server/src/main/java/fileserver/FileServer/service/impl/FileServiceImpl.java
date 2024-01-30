package fileserver.FileServer.service.impl;

import fileserver.FileServer.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public void init() {
        // TODO
        try {
            System.out.println("FileService initialized!");
        } catch (Exception e) {
            System.err.println("Could not initialize FileService!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        // TODO
        try {
            System.out.println("File saved!");
        } catch (Exception e) {
            System.err.println("Could not save file!");
        }
    }

    @Override
    public Resource load(String filename) {
        // TODO
        try {
            System.out.println("File loaded!");
        } catch (Exception e) {
            System.err.println("Could not load file!");
        }
    }

    @Override
    public Stream<Path> loadAll() {
        // TODO
        try {
            System.out.println("All files loaded!");
        } catch (Exception e) {
            System.err.println("Could not load all files!");
        }
    }
}
// FileServiceImpl
