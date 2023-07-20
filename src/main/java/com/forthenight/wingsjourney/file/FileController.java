package com.forthenight.wingsjourney.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class FileController {
    
    @GetMapping("/images/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get("images/" + imageName);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); 

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}