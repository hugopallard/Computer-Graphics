package org.example.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MyHistogram {

    private int[] data = new int[256];
    private ImageIcon histogramImageIcon;
    private int totalPixels;

    public MyHistogram(byte[] pixels) {

        for (byte pixelValue : pixels) {
            int unsignedValue = pixelValue & 0xFF;
            data[unsignedValue]++;
        }
    }

    public int[] linearNormalization() {
        // Find the minimum and maximum values in the histogram
        int minValue = Arrays.stream(this.data).min().orElse(0);
        int maxValue = Arrays.stream(this.data).max().orElse(0);
        // Compute the scaling factors for contrast stretching
        double scaleFactor = 255.0 / (maxValue - minValue);
        // Apply linear normalization using contrast stretching
        int[] normalizedHistogram = new int[this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            normalizedHistogram[i] = (int) Math.round((this.data[i] - minValue) * scaleFactor);
        }
        this.data = normalizedHistogram;
        return this.data;
    }

    public int[] equalizeHistogram() {
        // Calculate the cumulative distribution function (CDF)
        int[] cdf = new int[256];
        cdf[0] = this.data[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + this.data[i];
        }

        // Normalize the CDF to the range [0, 255]
        int[] cdfNormalized = new int[256];
        for (int i = 0; i < 256; i++) {
            cdfNormalized[i] = (int) (255.0 * (cdf[i] - cdf[0]) / (this.data.length - 1));
        }

        // Interpolate the values in the original histogram using the normalized CDF
        int[] equalizedHistogram = new int[this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            equalizedHistogram[i] = cdfNormalized[this.data[i] & 0xFF];
        }
        this.data = equalizedHistogram;
        return equalizedHistogram;
    }

    public ImageIcon toImageIcon(int width, int height) {
        BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = histogramImage.createGraphics();
        // Find the maximum value in the histogram for scaling the y-axis
        int maxValue = Arrays.stream(this.data).max().orElse(1);

        // Draw axes
        g.setColor(Color.BLACK);
        g.drawLine(0, height - 1, width - 1, height - 1); // x-axis
        g.drawLine(0, 0, 0, height - 1); // y-axis

        // Draw X-axis graduations
        int numGraduations = 5; // Adjust the number of graduations
        int graduationSpacing = width / numGraduations;

        for (int i = 0; i <= numGraduations; i++) {
            int x = i * graduationSpacing;
            g.drawLine(x, height - 1, x, height - 6);
            g.drawString(String.valueOf(i * (255 / numGraduations)), x - 10, height - 10);
        }

        // Draw bars
        int barWidth = width / 256; // 256 intensity values from 0 to 255

        for (int i = 0; i < 256; i++) {
            int barHeight = (int) (((double) this.data[i] / maxValue) * height);

            // Define three colors for the gradient
            Color[] colors = {Color.BLUE, Color.GREEN, Color.RED};

            // Define fractions for the colors (0.0 to 1.0)
            float[] fractions = {0.0f, 0.5f, 1.0f};

            // Create a three-color gradient paint
            LinearGradientPaint gradientPaint = new LinearGradientPaint(
                    new Point2D.Float(0, 0), new Point2D.Float(width, 0),
                    fractions, colors
            );

            // Set the paint to the Graphics2D object
            g.setPaint(gradientPaint);
            g.fillRect(i * barWidth, height - barHeight - 1, barWidth, barHeight);
        }
        g.dispose();

        BufferedImage scaledImage = new BufferedImage(width - 50, height - 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gScale = scaledImage.createGraphics();

        // Draw the original image with scaling
        gScale.drawImage(histogramImage, 0, 0, width - 50, height - 50, null);

        gScale.dispose();

        this.histogramImageIcon = new ImageIcon(scaledImage);
        return this.histogramImageIcon;
    }

    public int getTotalPixels() {
        // Count total number of pixels from the histogram
        for (int count : data) {
            totalPixels += count;
        }
        return totalPixels;
    }

    public int[] getData() {
        return data;
    }
}
