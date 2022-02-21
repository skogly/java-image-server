package com.example.imageserver.services;

import com.example.imageserver.repositories.FileRepository;
import com.example.imageserver.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ImageService {

    @Value("${imageFolder}")
    private String imageFolder;
    @Value("${thumbnailFolder}")
    private String thumbnailFolder;
    @Value("${mobileFolder}")
    private String mobileFolder;
    private static String[] imageFiles;
    private static final Logger logger = Logger.getLogger("imageService");
    private static final FileRepository repository = new FileRepository();
    private static final String[] FileExtensions = new String[]{"jpg", "JPG", "png"};
    private static FileRepository fileRepository;

    public ImageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String[] GetImageFiles() {
        return this.imageFiles;
    }

    public void FindAllImages() throws IOException {
        logger.info("Going through images in directory ");

        imageFiles = null;

        String[] imgFiles = repository.GetFilesInDir(imageFolder, FileExtensions);

        logger.info("%s images found".formatted(imgFiles.length));

        imageFiles = imgFiles;

        Arrays.stream(imgFiles).toList().parallelStream().forEach(filePath ->
        {
            try {
                ImageUtils.CreateAndSaveResizedImage(filePath, imageFolder, thumbnailFolder, mobileFolder);
            } catch (IOException e) {
                logger.warning("Unable to process %s".formatted(filePath));
                logger.warning(e.getMessage());
            }
        });

        logger.info("Finished processing images");
    }

    public InputStream GetThumbnailImageAsStream(String path) throws FileNotFoundException {
        if (!isInImageFileArray(path)) {
            logger.warning("%s does not exist in the image file array".formatted(path));
            return null;
        }
        return fileRepository.GetFileAsStream(Paths.get(thumbnailFolder, path).toString());
    }

    public InputStream GetMobileImageAsStream(String path) throws FileNotFoundException {
        if (!isInImageFileArray(path)) {
            logger.warning("%s does not exist in the image file array".formatted(path));
            return null;
        }
        return fileRepository.GetFileAsStream(Paths.get(mobileFolder, path).toString());
    }

    public InputStream GetImageAsStream(String path) throws FileNotFoundException {
        if (!isInImageFileArray(path)) {
            logger.warning("%s does not exist in the image file array".formatted(path));
            return null;
        }
        return fileRepository.GetFileAsStream(Paths.get(imageFolder, path).toString());
    }

    public void SaveImage(String fileName, MultipartFile imageFile) throws Exception {
        logger.info("Requested to save %s".formatted(fileName));
        try {
            imageFile.transferTo(new File(Paths.get(imageFolder + File.separator, fileName).toString()));
            FindAllImages();
        } catch (Exception e) {
            logger.warning("Not able to save %s".formatted(fileName));
            logger.warning(e.getMessage());
        }
    }

    public void DeleteImage(String path) throws Exception {
        if (!isInImageFileArray(path)) {
            logger.warning("%s does not exist in the image file array".formatted(path));
            return;
        }
        List<String> directories = Arrays.asList(imageFolder, thumbnailFolder, mobileFolder);
        logger.info("Requested to delete %s".formatted(path));
        for (Iterator<String> i = directories.iterator(); i.hasNext();) {
            try {
                String directory = i.next();
                fileRepository.DeleteFile(Paths.get(directory, path).toString());
            } catch (FileSystemException e) {
                logger.warning("Was not able to delete %s".formatted(path));
                logger.warning(e.getMessage());
            }
        }
        FindAllImages();
    }

    private boolean isInImageFileArray(String path){
        return Arrays.asList(imageFiles).contains(path);
    }
}