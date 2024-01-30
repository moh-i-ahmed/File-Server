package fileserver.FileServer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fileserver.FileServer.model.File;
import fileserver.FileServer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/")
    public void getAllFiles() {
        // TODO: process GET request
    }

    @GetMapping("/filename")
    public void getAllFiles(@RequestParam String filenameString) {
        // TODO: process GET request
    }
    
    @PostMapping("/upload")
    public void uploadFile(@RequestBody File entity) {
        //TODO: process POST request
    }
    
}
// FileController
