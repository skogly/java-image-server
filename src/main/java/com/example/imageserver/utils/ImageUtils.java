package com.example.imageserver.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    public static void CreateAndSaveResizedImage(String path, String imageFolder, String thumbnailFolder, String mobileFolder) throws IOException {
        String imagePath = Paths.get(imageFolder, path).toString();
        String thumbnailSavePath = Paths.get(thumbnailFolder, path).toString();
        String mobileSavePath = Paths.get(mobileFolder, path).toString();
        TryResizeAndSaveImage(thumbnailSavePath, 200, imagePath);
        TryResizeAndSaveImage(mobileSavePath, 1000, imagePath);
    }

    private static void TryResizeAndSaveImage(String path, int maxPixels, String imagePath) throws IOException {
        if (!isExistingPath(path)) {
            BufferedImage image = ImageIO.read(new File(imagePath));
            BufferedImage resizedImage = resizeImage(image, maxPixels);
            try {
                File file = new File(path);
                file.getParentFile().mkdirs();
                ImageIO.write(resizedImage, path.substring(path.length() - 3), file);
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private static boolean isExistingPath(String path) {
        return Files.exists(Paths.get(path));
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int maxPixels) {
        Map<String, Integer> resized = getWidthAndHeight(originalImage, maxPixels);
        Image resultingImage = originalImage.getScaledInstance(resized.get("width"), resized.get("height"), Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(resized.get("width"), resized.get("height"), BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private static Map<String, Integer> getWidthAndHeight(BufferedImage image, int maxPixels) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int width = 0;
        int height = 0;

        if (imageWidth > imageHeight)
        {
            var factor = (double)maxPixels / imageWidth;
            width = (int)(imageWidth * factor);
            height = (int)(imageHeight * factor);
        }
        else
        {
            var factor = (double)maxPixels / imageHeight;
            width = (int)(imageWidth * factor);
            height = (int)(imageHeight * factor);
        }
        Map<String, Integer> result = new HashMap<>();

        result.put("width", width);
        result.put("height", height);

        return result;
    }
}
