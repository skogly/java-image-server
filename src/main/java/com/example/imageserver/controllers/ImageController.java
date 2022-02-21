package com.example.imageserver.controllers;

import com.example.imageserver.requests.JsonRequest;
import com.example.imageserver.services.ImageService;
import com.example.imageserver.websocket.WebSocketServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
public class ImageController {

    private WebSocketServer webSocketServer;
    private ImageService imageService;

    public ImageController(ImageService imageService, WebSocketServer webSocketServer) throws IOException {
        this.webSocketServer = webSocketServer;
        this.imageService = imageService;
        this.imageService.FindAllImages();
    }

    @GetMapping("/images")
    @CrossOrigin
    public List<String> getImageFilesAsList() {
        return Arrays.stream(imageService.GetImageFiles()).toList();
    }

    @PostMapping(path = "/thumbnail")
    @CrossOrigin
    @ResponseBody
    ResponseEntity<byte[]> getThumbnailImage(@RequestBody JsonRequest jsonRequest) throws Exception {
        InputStream in = imageService.GetThumbnailImageAsStream(jsonRequest.getPath());
        if (in == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND
            );
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(in.readAllBytes(), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/mobile")
    @CrossOrigin
    @ResponseBody
    ResponseEntity<byte[]> getMobileImage(@RequestBody JsonRequest jsonRequest) throws Exception {
        InputStream in = imageService.GetMobileImageAsStream(jsonRequest.getPath());
        if (in == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND
            );
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(in.readAllBytes(), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/image")
    @CrossOrigin
    @ResponseBody
    ResponseEntity<byte[]> getImage(@RequestBody JsonRequest jsonRequest) throws Exception {
        InputStream in = imageService.GetImageAsStream(jsonRequest.getPath());
        if (in == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND
            );
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(in.readAllBytes(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/upload")
    @CrossOrigin
    public void UploadFile(@RequestPart String FilePath, @RequestPart MultipartFile ImageFile) throws Exception {
        imageService.SaveImage(FilePath, ImageFile);
        webSocketServer.NotifyClients("uploadedImages");
    }

    @DeleteMapping(value = "/delete")
    @CrossOrigin
    public void DeleteFile(@RequestBody JsonRequest jsonRequest) throws Exception {
        imageService.DeleteImage(jsonRequest.getPath());
        webSocketServer.NotifyClients("deletedImages");
    }
}
