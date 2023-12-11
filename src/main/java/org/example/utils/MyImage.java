package org.example.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class MyImage {

    private int width;
    private int height;
    private String magicString;
    private int maxColorValue;
    private byte[] pixels;
    private byte[] defaultPixels;
    private BufferedImage image;
    private BufferedImage defaultImage;
    private boolean isGrayScale;


    // Create a default MyImage object (for all files except .ppm)
    public MyImage(String filePath) {
        try {
            this.image = ImageIO.read(new File(filePath));
            this.defaultImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();

        byte[] imageData = new byte[width * height * 3]; // 3 bytes per pixel (RGB)

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                imageData[index++] = (byte) ((pixel >> 16) & 0xFF); // Red
                imageData[index++] = (byte) ((pixel >> 8) & 0xFF);  // Green
                imageData[index++] = (byte) (pixel & 0xFF);         // Blue
            }
        }
        this.pixels = imageData;
        this.defaultPixels = imageData;

    }


    // Create a MyImage PPM object
    public MyImage(String filePath, String type) {
        if (!Objects.equals(type, "ppm")) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            // Read magic number
            this.magicString = br.readLine().trim();
            // Read width, height, and max color value and set it to the parent.
            String[] dimensions = br.readLine().trim().split("\\s+");
            this.height = Integer.parseInt(dimensions[0]);
            this.width = Integer.parseInt(dimensions[1]);
            // Read max color value
            this.maxColorValue = Integer.parseInt(br.readLine().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readP3_PPMData(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            // Skip the header lines
            reader.readLine(); // Read and skip the magic number
            reader.readLine(); // Read and skip the dimensions line
            reader.readLine(); // Skip the max color value line

            // Read ASCII pixel data
            StringBuilder pixelData = new StringBuilder();
            while (reader.ready()) {
                pixelData.append(reader.readLine()).append(" ");
            }

            // Scale pixelData based on maxColorValue
            String[] values = pixelData.toString().split("\\s+");
            byte[] imageData = new byte[values.length];
            for (int i = 0; i < values.length; i++) {
                imageData[i] = (byte) ((Integer.parseInt(values[i]) * 255) / maxColorValue);
            }
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            image.getRaster().setDataElements(0, 0, width, height, imageData);
            // Create a default image
            this.defaultImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            defaultImage.getRaster().setDataElements(0, 0, width, height, imageData);
            this.pixels = imageData;
            // Create a default array of pixels
            this.defaultPixels = imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readP6_PPMData(String filePath) {
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath));
            // Read pixel data
            byte[] imageData = new byte[getWidth() * getHeight() * 3];
            int bytesRead = 0;
            while (bytesRead < imageData.length) {
                imageData[bytesRead++] = dataInputStream.readByte();
            }
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            image.getRaster().setDataElements(0, 0, width, height, imageData);
            // Create a default image
            this.defaultImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            defaultImage.getRaster().setDataElements(0, 0, width, height, imageData);
            // Create a default array of pixels
            this.defaultPixels = imageData;
            this.pixels = imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToDisk(String fileName) {
        // Get the format of the image (e.g., "jpg", "png", etc.)
        String format = fileName.split("\\.")[1];
        String outputPath = "./output/" + Math.random() + "." + format;
        BufferedImage toWriteImage = new BufferedImage(width, height, image.getType());
        toWriteImage.getRaster().setDataElements(0, 0, width, height, pixels);
        try {
            ImageIO.write(toWriteImage, format, new File(outputPath));
            System.out.println("Wrote image successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePPMToDisk() {
        String outputPath = "./output/" + Math.random() + ".ppm";
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputPath))) {
            // Write PPM header
            dos.writeBytes(magicString + "\n");
            dos.writeBytes(width + " " + height + "\n");
            dos.writeBytes("255\n");
            // Write pixel data
            dos.write(pixels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] makeDefault() {
        System.arraycopy(defaultPixels, 0, pixels, 0, defaultPixels.length);
        // Create a new BufferedImage with the same type as the source image
        this.image = new BufferedImage(width, height, defaultImage.getType());
        // Get a sub-image of the source image and draw it onto the new image
        image.getGraphics().drawImage(defaultImage, 0, 0, null);
        this.isGrayScale = false;
        return pixels;
    }

    public byte[] makeNegative() {

        byte[] invertedPixels = new byte[width * height * 3]; // Assuming RGB format
        int index = 0; // Index to keep track of the position in the array

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB value of the original pixel
                int pixel = image.getRGB(x, y);

                // Extract individual color components
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Perform negative transformation by subtracting each color component from 255
                int invertedRed = 255 - red;
                int invertedGreen = 255 - green;
                int invertedBlue = 255 - blue;

                // Store the inverted RGB values in the byte array
                invertedPixels[index++] = (byte) invertedRed;
                invertedPixels[index++] = (byte) invertedGreen;
                invertedPixels[index++] = (byte) invertedBlue;
            }
        }
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, invertedPixels);
        this.pixels = invertedPixels;
        return invertedPixels;
    }

    public byte[] modifyColors(int newRed, int newGreen, int newBlue, String mode) {
        // Create a byte array to store the multiplied pixels values
        byte[] modifiedPixels = new byte[width * height * 3]; // Assuming RGB format
        int index = 0;
        // Iterate through each pixel and perform the additive transformation
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB value of the original pixel
                int pixel = image.getRGB(x, y);

                // Extract individual color components
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                int transformedRed;
                int transformedGreen;
                int transformedBlue;

                switch (mode) {
                    case "add" -> {
                        // Perform additive transformation by adding the specified values
                        transformedRed = Math.max(0, Math.min(255, red + newRed));
                        transformedGreen = Math.max(0, Math.min(255, green + newGreen));
                        transformedBlue = Math.max(0, Math.min(255, blue + newBlue));
                    }
                    case "subtract" -> {
                        // Perform additive transformation by adding the specified values
                        transformedRed = Math.max(0, Math.min(255, red - newRed));
                        transformedGreen = Math.max(0, Math.min(255, green - newGreen));
                        transformedBlue = Math.max(0, Math.min(255, blue - newBlue));
                    }
                    case "multiply" -> { // Perform additive transformation by adding the specified values
                        transformedRed = Math.max(0, Math.min(255, red * newRed));
                        transformedGreen = Math.max(0, Math.min(255, green * newGreen));
                        transformedBlue = Math.max(0, Math.min(255, blue * newBlue));
                    }
                    case "divide" -> {
                        // Perform additive transformation by adding the specified values
                        transformedRed = Math.max(0, Math.min(255, red / newRed));
                        transformedGreen = Math.max(0, Math.min(255, green / newGreen));
                        transformedBlue = Math.max(0, Math.min(255, blue / newBlue));
                    }
                    default ->
                            throw new IllegalArgumentException("Given mode is not supported: " + mode + " [add, subtract, multiply, divide]")
                                    ;
                }

                // Store the transformed RGB values in the byte array
                modifiedPixels[index++] = (byte) transformedRed;
                modifiedPixels[index++] = (byte) transformedGreen;
                modifiedPixels[index++] = (byte) transformedBlue;
            }
        }
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, modifiedPixels);
        this.pixels = modifiedPixels;
        return modifiedPixels;
    }

    public byte[] modifyBrightness(double brightness) {
        // Create a byte array to store the modified brightness pixels values
        byte[] modifiedBrightnessPixels = new byte[width * height * 3]; // Assuming RGB format
        int index = 0;
        // Iterate through each pixel and perform the additive transformation
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB value of the original pixel
                int pixel = image.getRGB(x, y);

                // Extract individual color components
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Perform additive transformation by adding the specified values
                int transformedRed = (int) Math.max(0, Math.min(255, red + brightness));
                int transformedGreen = (int) Math.max(0, Math.min(255, green + brightness));
                int transformedBlue = (int) Math.max(0, Math.min(255, blue + brightness));

                // Store the transformed RGB values in the byte array
                modifiedBrightnessPixels[index++] = (byte) transformedRed;
                modifiedBrightnessPixels[index++] = (byte) transformedGreen;
                modifiedBrightnessPixels[index++] = (byte) transformedBlue;
            }
        }
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, modifiedBrightnessPixels);
        this.pixels = modifiedBrightnessPixels;
        return modifiedBrightnessPixels;
    }

    public byte[] toGrayScale(double gamma, String mode) {
        // Create a byte array to store the gamma grayscale pixels values
        byte[] gammaGrayScalePixels = new byte[width * height]; // Assuming RGB format
        // Iterate through each pixel and perform the additive transformation
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                int gray;

                switch (mode) {
                    case "gamma" -> gray = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
                    case "lumma" -> gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                    default ->
                            throw new IllegalArgumentException("Given mode is not supported: " + mode + " [gamma, lumma]");
                }

                // Apply gamma expansion
                int expandedGray = (int) (Math.pow(gray / 255.0, gamma) * 255.0);

                // Store the result in the byte array
                gammaGrayScalePixels[y * width + x] = (byte) expandedGray;
            }
        }
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setDataElements(0, 0, width, height, gammaGrayScalePixels);
        this.pixels = gammaGrayScalePixels;
        this.isGrayScale = true;
        return gammaGrayScalePixels;
    }

    public byte[] binarizePercentBlack(double percentBlackThreshold, int[] histogram) {

        int totalPixels = Arrays.stream(histogram).sum();

        // Calculate threshold based on percentBlack
        int blackThreshold = (int) (totalPixels * percentBlackThreshold);
        int cumulativeBlackPixels = 0;
        int threshold = 0;

        // Calculate the threshold
        for (int i = 0; i < 256; i++) {
            cumulativeBlackPixels += histogram[i];
            if (cumulativeBlackPixels > blackThreshold) {
                threshold = i;
                break;
            }
        }
        // Binarize the image
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) ((pixels[i] & 0xFF) <= threshold ? 0 : 255);
        }

        return pixels;
    }

    public byte[] binarizeMeanIterative() {
        // Calculate initial threshold using mean of pixel values
        int sum = 0;
        for (int pixel : pixels) {
            sum += pixel & 0xFF;
        }
        int threshold = sum / pixels.length;

        int newThreshold = -1;

        // Iteratively update threshold until convergence
        while (newThreshold != threshold) {
            int sum1 = 0, sum2 = 0;
            int count1 = 0, count2 = 0;

            // Calculate sums and counts for both sides of the threshold
            for (int pixel : pixels) {
                int pixelValue = pixel & 0xFF;

                if (pixelValue <= threshold) {
                    sum1 += pixelValue;
                    count1++;
                } else {
                    sum2 += pixelValue;
                    count2++;
                }
            }

            // Calculate means for both sides
            int mean1 = (count1 > 0) ? sum1 / count1 : 0;
            int mean2 = (count2 > 0) ? sum2 / count2 : 0;

            // Update thresholds
            newThreshold = threshold;
            threshold = (mean1 + mean2) / 2;
        }

        // Binarize the image
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) ((pixels[i] & 0xFF) <= threshold ? 0 : 255);
        }

        return pixels;
    }

    public byte[] binarizeEntropySelection(int[] histogram) {

        double maxEntropy = 0.0;
        int bestThreshold = 0;

        int totalPixels = Arrays.stream(histogram).sum();

        // Iterate through possible thresholds and find the one that maximizes entropy
        for (int threshold = 0; threshold < 256; threshold++) {
            int countBackground = 0;
            int countForeground = 0;

            // Calculate counts for background and foreground
            for (int i = 0; i < threshold; i++) {
                countBackground += histogram[i];
            }

            for (int i = threshold; i < 256; i++) {
                countForeground += histogram[i];
            }

            // Calculate entropy
            if (countBackground > 0 && countForeground > 0) {
                double probabilityBackground = (double) countBackground / totalPixels;
                double probabilityForeground = (double) countForeground / totalPixels;

                double entropyBackground = -probabilityBackground * Math.log(probabilityBackground);
                double entropyForeground = -probabilityForeground * Math.log(probabilityForeground);

                double entropy = entropyBackground + entropyForeground;

                // Update max entropy and best threshold if needed
                if (entropy > maxEntropy) {
                    maxEntropy = entropy;
                    bestThreshold = threshold;
                }
            }
        }

        // Binarize the image
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) ((pixels[i] & 0xFF) <= bestThreshold ? 0 : 255);
        }
        return pixels;
    }

    public byte[] binarizeMinimumError(int[] histogram) {

        double minError = Double.MAX_VALUE;
        int bestThreshold = 0;

        // Iterate through possible thresholds and find the one that minimizes error
        for (int threshold = 0; threshold < histogram.length; threshold++) {
            int countBackground = 0;
            int countForeground = 0;
            int sumBackground = 0;
            int sumForeground = 0;

            // Calculate sums and counts for both sides of the threshold
            for (int i = 0; i < threshold; i++) {
                countBackground += histogram[i];
                sumBackground += i * histogram[i];
            }

            for (int i = threshold; i < histogram.length; i++) {
                countForeground += histogram[i];
                sumForeground += i * histogram[i];
            }

            // Calculate means for both sides
            double meanBackground = (countBackground > 0) ? sumBackground / (double) countBackground : 0;
            double meanForeground = (countForeground > 0) ? sumForeground / (double) countForeground : 0;

            // Calculate error
            double error = countBackground * Math.pow(meanBackground - meanForeground, 2)
                    + countForeground * Math.pow(meanForeground - meanBackground, 2);

            // Update minError and bestThreshold if needed
            if (error < minError) {
                minError = error;
                bestThreshold = threshold;
            }
        }

        // Binarize the image
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) ((pixels[i] & 0xFF) <= bestThreshold ? 0 : 255);
        }

        return pixels;
    }

    public byte[] binarizeFuzzyMinimumError(int[] histogram) {
        double minError = Double.MAX_VALUE;
        int bestThreshold = 0;

        // Iterate through possible thresholds and find the one that minimizes error
        for (int threshold = 0; threshold < histogram.length; threshold++) {
            double countBackground = 0;
            double countForeground = 0;
            double sumBackground = 0;
            double sumForeground = 0;

            // Calculate sums and counts for both sides of the threshold
            for (int i = 0; i < threshold; i++) {
                countBackground += histogram[i];
                sumBackground += i * histogram[i];
            }

            for (int i = threshold; i < histogram.length; i++) {
                countForeground += histogram[i];
                sumForeground += i * histogram[i];
            }

            // Calculate means for both sides
            double meanBackground = (countBackground > 0) ? sumBackground / countBackground : 0;
            double meanForeground = (countForeground > 0) ? sumForeground / countForeground : 0;

            // Calculate error
            double error = countBackground * Math.pow(meanBackground - meanForeground, 2)
                    + countForeground * Math.pow(meanForeground - meanBackground, 2);

            // Update minError and bestThreshold if needed
            if (error < minError) {
                minError = error;
                bestThreshold = threshold;
            }
        }

        // Binarize the image
        for (int i = 0; i < pixels.length; i++) {
            int pixelValue = pixels[i] & 0xFF;

            // Use fuzzy logic to determine pixel value directly inside binarization
            double membership = 1.0 - Math.abs((pixelValue - bestThreshold) / (double) bestThreshold);
            double fuzzyValue = Math.max(0, Math.min(1, membership));

            pixels[i] = (byte) ((fuzzyValue <= 0.5) ? 0 : 255);
        }

        return pixels;
    }

    public ImageIcon fromRGBtoImageIcon(byte[] rgbPixels) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Fill the BufferedImage with RGB data
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = rgbPixels[index++] & 0xFF;
                int green = rgbPixels[index++] & 0xFF;
                int blue = rgbPixels[index++] & 0xFF;
                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }
        return new ImageIcon(image);
    }

    public ImageIcon fromGrayscaleToImageIcon(byte[] grayscalePixels) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = grayscalePixels[y * width + x] & 0xFF; // Ensure positive value by masking with 0xFF
                int rgb = (value << 16) | (value << 8) | value; // Grayscale values for red, green, and blue
                image.setRGB(x, y, rgb);
            }
        }

        return new ImageIcon(image);
    }

    public byte[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getMagicString() {
        return magicString;
    }

    public boolean isGrayScale() {
        return isGrayScale;
    }

    // Legacy
    private void writePPM_File(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            String header = magicString + "\n" + width + " " + height + "\n" + maxColorValue + "\n";
            fos.write(header.getBytes());

            switch (magicString) {
                case "P3" -> {
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width * 3; j++) {
                            // Replace the next line with your logic to obtain or generate pixel values
                            int pixelValue = (int) (Math.random() * maxColorValue);
                            fos.write(String.format("%d ", pixelValue).getBytes());
                        }
                        fos.write("\n".getBytes());
                    }
                }
                case "P6" -> {
                    for (int y = 0; y < width; y++) {
                        for (int x = 0; x < height; x++) {
                            // Assuming imageData is a 1D array of RGB values (byte[] imageData)
                            fos.write((int) (Math.random() * maxColorValue + 1));
                            fos.write((int) (Math.random() * maxColorValue + 1));
                            fos.write((int) (Math.random() * maxColorValue + 1));
                        }
                    }
                }
            }
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] convertRGBtoCMYK(byte[] rgbData) {
        byte[] cmykData = new byte[width * height * 4];

        int index = 0;
        for (int i = 0; i < rgbData.length; i += 3) {
            int red = rgbData[i] & 0xFF;
            int green = rgbData[i + 1] & 0xFF;
            int blue = rgbData[i + 2] & 0xFF;

            // Invert RGB values
            red = 255 - red;
            green = 255 - green;
            blue = 255 - blue;

            // Normalize RGB values to the range [0, 1]
            double r = red / 255.0;
            double g = green / 255.0;
            double b = blue / 255.0;

            // CMYK conversion
            double k = 1 - Math.max(Math.max(r, g), b);
            double c = (1 - r - k) / (1 - k);
            double m = (1 - g - k) / (1 - k);
            double y = (1 - b - k) / (1 - k);

            // Scale to byte values (0-255)
            cmykData[index++] = (byte) (c * 255); // Cyan
            cmykData[index++] = (byte) (m * 255); // Magenta
            cmykData[index++] = (byte) (y * 255); // Yellow
            cmykData[index++] = (byte) (k * 255); // Black
        }

        return cmykData;
    }

    public BufferedImage buildCMYKImage(byte[] cmykData) {
        BufferedImage cmykImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int cyan = cmykData[index++];
                int magenta = cmykData[index++];
                int yellow = cmykData[index++];
                int black = cmykData[index++];

                int cmyk = (255 - cyan << 24) | (255 - magenta << 16) | (255 - yellow << 8) | (255 - black);

                cmykImage.setRGB(x, y, cmyk);
            }
        }

        return cmykImage;
    }
}
