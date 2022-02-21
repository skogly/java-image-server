package com.example.imageserver.repositories;

import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FileRepository {

    private String[] exts = new String[] {"jpg", "JPG", "png"};

    public String[] GetFilesInDir(String path, String[] fileExtensions) throws IOException {
        List<Path> files;
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            files = walk.filter(file -> validExtension(file.toString())).collect(Collectors.toList());
        }
        return files.stream().map(f -> f.toString().replace(Paths.get(path)+ File.separator, "")).toArray(String[]::new);
    }

    public InputStream GetFileAsStream(String path) throws FileNotFoundException {
        return new FileInputStream(path);
    }

    public void DeleteFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    private boolean validExtension(String name) {
        for (String ext : exts ) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
