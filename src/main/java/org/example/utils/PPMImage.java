/*
package org.example.utils;

import java.io.*;
import java.util.Arrays;

public class PPMImage extends MyImage {
    private String magicString;
    private int maxColorValue;

    // Read a give PPM file
    public PPMImage(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read magic number
            this.magicString = br.readLine().trim();

            // Read width, height, and max color value and set it to the parent.
            String[] dimensions = br.readLine().trim().split("\\s+");
            setHeight(Integer.parseInt(dimensions[0]));
            setWidth(Integer.parseInt(dimensions[1]));

            // Read max color value
            this.maxColorValue = Integer.parseInt(br.readLine().trim());

            // Read rgbData
            if ("P3".equals(magicString)) {
                setRgbData(getRGB_P3_PPM(filePath));
            } else if ("P6".equals(magicString)) {
                setRgbData(getRGB_P6_PPM(filePath));
            } else {
                throw new UnsupportedOperationException("Unsupported PPM format: " + magicString);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    // Write a new PPMFile
    public PPMImage(String magicString, int width, int height, int maxColorValue, String filePath) {

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


    private byte[] getRGB_P3_PPM(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

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

            return imageData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to read P6 PPM file (binary format)
    private byte[] getRGB_P6_PPM(String filePath) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath))) {
            // Read pixel data
            byte[] pixelDataArray = new byte[getWidth() * getHeight() * 3];
            int bytesRead = 0;
            try {
                while (bytesRead < pixelDataArray.length) {
                    pixelDataArray[bytesRead++] = dataInputStream.readByte();
                }
            } catch (EOFException e) {
                // Handle the EOFException as needed
                System.err.println("Error: Unexpected end of file. Insufficient data in the PPM file.");
            }
            return pixelDataArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
*/
