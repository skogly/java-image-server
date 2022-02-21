package com.example.imageserver.requests;

import org.springframework.web.multipart.MultipartFile;

public class ImageRequest {
    private String FilePath;
    private MultipartFile ImageFile;

    public ImageRequest(){

    }

    public ImageRequest(final String path, final MultipartFile imageFile)
    {
        this.FilePath = path;
        this.ImageFile = imageFile;
    }

    public String getFilePath() {
        return this.FilePath;
    }
    public MultipartFile getImageFile() {
        return this.ImageFile;
    }
}
