package org.example;

import org.example.utils.MyHistogram;
import org.example.utils.MyImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ImagePanel extends JPanel implements ActionListener {
    private final static String IMAGEPANEL = "Panel for displaying images";
    private final JButton openFileButton;
    private final JButton showDefaultButton;
    private final JButton negativeButton;
    private final JButton additiveButton;
    private final JButton subscractButton;
    private final JButton multiplicationButton;
    private final JButton divisionButton;
    private final JButton brightnessButton;
    private final JButton gammaGrayScaleButton;
    private final JButton lumaGrayScaleButton;
    private final JButton saveImageButton;
    private final JButton drawHistogramButton;
    private final JButton linearNormButton;
    private final JButton equalisationButton;
    private final JButton binPercentBlackButton;
    private final JButton binMeanIterativeButton;
    private final JButton binEntropyButton;
    private final JButton binMinErrorButton;
    private final JButton binFuzzyMinErrorButton;
    private final JLabel picLabel;
    private final JLabel picIndicator;
    private File file;
    private MyImage myImage;
    private MyHistogram myHistogram;

    public ImagePanel() {
        // Command panel
        openFileButton = new JButton("Choose file");
        openFileButton.addActionListener(this);

        showDefaultButton = new JButton("Show default");
        showDefaultButton.addActionListener(this);
        showDefaultButton.setEnabled(false);

        negativeButton = new JButton("Make image negative");
        negativeButton.addActionListener(this);
        negativeButton.setEnabled(false);

        additiveButton = new JButton("Add colors");
        additiveButton.addActionListener(this);
        additiveButton.setEnabled(false);

        subscractButton = new JButton("Substract colors");
        subscractButton.addActionListener(this);
        subscractButton.setEnabled(false);

        multiplicationButton = new JButton("Multiply colors");
        multiplicationButton.addActionListener(this);
        multiplicationButton.setEnabled(false);

        divisionButton = new JButton("Divide colors");
        divisionButton.addActionListener(this);
        divisionButton.setEnabled(false);

        brightnessButton = new JButton("Change brightness");
        brightnessButton.addActionListener(this);
        brightnessButton.setEnabled(false);

        gammaGrayScaleButton = new JButton("Gamma Grayscale transformation");
        gammaGrayScaleButton.addActionListener(this);
        gammaGrayScaleButton.setEnabled(false);

        lumaGrayScaleButton = new JButton("Luma Grayscale transformation");
        lumaGrayScaleButton.addActionListener(this);
        lumaGrayScaleButton.setEnabled(false);

        drawHistogramButton = new JButton("Draw histogram");
        drawHistogramButton.addActionListener(this);
        drawHistogramButton.setEnabled(false);

        linearNormButton = new JButton("Histogram linear normalisation");
        linearNormButton.addActionListener(this);
        linearNormButton.setEnabled(false);

        equalisationButton = new JButton("Histogram equalisation");
        equalisationButton.addActionListener(this);
        equalisationButton.setEnabled(false);

        binPercentBlackButton = new JButton("Binarize Percent Black");
        binPercentBlackButton.addActionListener(this);
        binPercentBlackButton.setEnabled(false);

        binMeanIterativeButton = new JButton("Binarize Mean Iterative");
        binMeanIterativeButton.addActionListener(this);
        binMeanIterativeButton.setEnabled(false);

        binEntropyButton = new JButton("Binarize Entropy Selection");
        binEntropyButton.addActionListener(this);
        binEntropyButton.setEnabled(false);

        binMinErrorButton = new JButton("Binarize Minimum Error");
        binMinErrorButton.addActionListener(this);
        binMinErrorButton.setEnabled(false);

        binFuzzyMinErrorButton = new JButton("Binarize Fuzzy minimum Error");
        binFuzzyMinErrorButton.addActionListener(this);
        binFuzzyMinErrorButton.setEnabled(false);

        saveImageButton = new JButton("Save image");
        saveImageButton.addActionListener(this);
        saveImageButton.setEnabled(false);

        JPanel commandPanel = new JPanel();
        commandPanel.setPreferredSize(new Dimension(250, MyFrame.getHEIGHT()));
        commandPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        commandPanel.add(openFileButton);
        commandPanel.add(showDefaultButton);
        commandPanel.add(negativeButton);
        commandPanel.add(additiveButton);
        commandPanel.add(subscractButton);
        commandPanel.add(multiplicationButton);
        commandPanel.add(divisionButton);
        commandPanel.add(brightnessButton);
        commandPanel.add(gammaGrayScaleButton);
        commandPanel.add(lumaGrayScaleButton);
        commandPanel.add(drawHistogramButton);
        commandPanel.add(linearNormButton);
        commandPanel.add(equalisationButton);
        commandPanel.add(binPercentBlackButton);
        commandPanel.add(binMeanIterativeButton);
        commandPanel.add(binEntropyButton);
        commandPanel.add(binMinErrorButton);
        commandPanel.add(binFuzzyMinErrorButton);
        commandPanel.add(saveImageButton);

        // Label which will display image information
        picIndicator = new JLabel();
        picIndicator.setPreferredSize(new Dimension(MyFrame.getWIDTH() - 550, MyFrame.getHEIGHT() - 650));
        picIndicator.setVerticalAlignment(JLabel.CENTER);
        picIndicator.setHorizontalAlignment(JLabel.CENTER);
        picIndicator.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Label which will hold image preview
        picLabel = new JLabel();
        picLabel.setVerticalAlignment(JLabel.CENTER);
        picLabel.setHorizontalAlignment(JLabel.CENTER);

        // Panel holding image preview and image information Labels
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new BorderLayout());
        picPanel.add(picLabel, BorderLayout.CENTER);
        picPanel.add(picIndicator, BorderLayout.NORTH);
        picPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add components to global panel
        this.setLayout(new BorderLayout());
        this.add(commandPanel, BorderLayout.WEST);
        this.add(picPanel, BorderLayout.CENTER);
    }

    public void addImage() {
        String fileName = file.getName();
        this.showDefaultButton.setEnabled(true);
        this.negativeButton.setEnabled(true);
        this.additiveButton.setEnabled(true);
        this.subscractButton.setEnabled(true);
        this.multiplicationButton.setEnabled(true);
        this.divisionButton.setEnabled(true);
        this.brightnessButton.setEnabled(true);
        this.gammaGrayScaleButton.setEnabled(true);
        this.lumaGrayScaleButton.setEnabled(true);
        this.drawHistogramButton.setEnabled(true);
        this.saveImageButton.setEnabled(true);
        if (fileName.endsWith(".ppm")) {
            // Build a new MyImage object from the absolute path of a file
            myImage = new MyImage(file.getAbsolutePath(), "ppm");
            // Read rgbData
            switch (myImage.getMagicString()) {
                case "P3" -> myImage.readP3_PPMData(file.getAbsolutePath());
                case "P6" -> myImage.readP6_PPMData(file.getAbsolutePath());
                default ->
                        throw new UnsupportedOperationException("Unsupported PPM format: " + myImage.getMagicString());
            }
            // Convert the byte array of the ppmImage to an ImageIcon. The byte by default is RGB.
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.getPixels()));

        } else if (fileName.endsWith("jpg") || fileName.endsWith("jpeg") ||
                fileName.endsWith("png") || fileName.endsWith("gif") ||
                fileName.endsWith("bmp") || fileName.endsWith("webp")) {
            // Reset the variable as we change the image.
            // Create a custom myImage object from the absolute path of a file with some properties and method.
            myImage = new MyImage(file.getAbsolutePath());
            // Convert the byte array of an image to an ImageIcon. The byte by default is RGB.
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.getPixels()));

        } else {
            picIndicator.setText("Error reading your file: it isn't an image");
        }
        // Quick information about the displayed image.
        picIndicator.setText("Image mode: RGB " + "| Image type: " + file.getName().split("\\.")[1] + " | Dimension: " + picLabel.getIcon().getIconWidth() + "x" + picLabel.getIcon().getIconHeight() + " | Size: " + file.length() / 1024 + " KB");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.linearNormButton.setEnabled(false);
        this.equalisationButton.setEnabled(false);
        if (e.getSource() == this.openFileButton) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setCurrentDirectory(new File("."));
            int response = jFileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                this.file = jFileChooser.getSelectedFile();
                addImage();
                this.negativeButton.setEnabled(true);
                this.additiveButton.setEnabled(true);
                this.subscractButton.setEnabled(true);
                this.multiplicationButton.setEnabled(true);
                this.divisionButton.setEnabled(true);
                this.brightnessButton.setEnabled(true);
                this.gammaGrayScaleButton.setEnabled(true);
                this.lumaGrayScaleButton.setEnabled(true);
                this.binPercentBlackButton.setEnabled(false);
                this.binMeanIterativeButton.setEnabled(false);
                this.binEntropyButton.setEnabled(false);
                this.binMinErrorButton.setEnabled(false);
                this.binFuzzyMinErrorButton.setEnabled(false);
            }
        } else if (e.getSource() == this.showDefaultButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.makeDefault()));
            this.negativeButton.setEnabled(true);
            this.additiveButton.setEnabled(true);
            this.subscractButton.setEnabled(true);
            this.multiplicationButton.setEnabled(true);
            this.divisionButton.setEnabled(true);
            this.brightnessButton.setEnabled(true);
            this.gammaGrayScaleButton.setEnabled(true);
            this.lumaGrayScaleButton.setEnabled(true);
            this.binPercentBlackButton.setEnabled(false);
            this.binMeanIterativeButton.setEnabled(false);
            this.binEntropyButton.setEnabled(false);
            this.binMinErrorButton.setEnabled(false);
            this.binFuzzyMinErrorButton.setEnabled(false);
        } else if (e.getSource() == this.negativeButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.makeNegative()));
        } else if (e.getSource() == this.additiveButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.modifyColors(100, 40, 20, "add")));
        } else if (e.getSource() == this.subscractButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.modifyColors(100, 40, 20, "subtract")));
        } else if (e.getSource() == this.multiplicationButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.modifyColors(2, 4, 6, "multiply")));
        } else if (e.getSource() == this.divisionButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.modifyColors(2, 4, 6, "divide")));
        } else if (e.getSource() == this.brightnessButton) {
            picLabel.setIcon(myImage.fromRGBtoImageIcon(myImage.modifyBrightness(50)));
        } else if (e.getSource() == this.gammaGrayScaleButton) {
            this.showDefaultButton.setEnabled(true);
            this.negativeButton.setEnabled(false);
            this.additiveButton.setEnabled(false);
            this.subscractButton.setEnabled(false);
            this.multiplicationButton.setEnabled(false);
            this.divisionButton.setEnabled(false);
            this.brightnessButton.setEnabled(false);
            this.gammaGrayScaleButton.setEnabled(false);
            this.lumaGrayScaleButton.setEnabled(false);
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.toGrayScale(0.4, "gamma")));
        } else if (e.getSource() == this.lumaGrayScaleButton) {
            this.showDefaultButton.setEnabled(true);
            this.negativeButton.setEnabled(false);
            this.additiveButton.setEnabled(false);
            this.subscractButton.setEnabled(false);
            this.multiplicationButton.setEnabled(false);
            this.divisionButton.setEnabled(false);
            this.brightnessButton.setEnabled(false);
            this.gammaGrayScaleButton.setEnabled(false);
            this.lumaGrayScaleButton.setEnabled(false);
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.toGrayScale(0.9, "lumma")));
        } else if (e.getSource() == this.drawHistogramButton) {
            myHistogram = new MyHistogram(myImage.getPixels());
            picLabel.setIcon(myHistogram.toImageIcon(picLabel.getWidth(), picLabel.getHeight()));
            this.showDefaultButton.setEnabled(true);
            this.linearNormButton.setEnabled(true);
            this.equalisationButton.setEnabled(true);
            this.negativeButton.setEnabled(false);
            this.additiveButton.setEnabled(false);
            this.subscractButton.setEnabled(false);
            this.multiplicationButton.setEnabled(false);
            this.divisionButton.setEnabled(false);
            this.brightnessButton.setEnabled(false);
            this.gammaGrayScaleButton.setEnabled(false);
            this.lumaGrayScaleButton.setEnabled(false);
            if (myImage.isGrayScale()) {
                this.binPercentBlackButton.setEnabled(true);
                this.binMeanIterativeButton.setEnabled(true);
                this.binEntropyButton.setEnabled(true);
                this.binMinErrorButton.setEnabled(true);
                this.binFuzzyMinErrorButton.setEnabled(true);
            }
        } else if (e.getSource() == this.linearNormButton) {
            myHistogram.linearNormalization();
            picLabel.setIcon(myHistogram.toImageIcon(picLabel.getWidth(), picLabel.getHeight()));
        } else if (e.getSource() == this.equalisationButton) {
            myHistogram.equalizeHistogram();
            picLabel.setIcon(myHistogram.toImageIcon(picLabel.getWidth(), picLabel.getHeight()));
        } else if (e.getSource() == this.binPercentBlackButton) {
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.binarizePercentBlack(0.5, myHistogram.getData())));
        } else if (e.getSource() == this.binMeanIterativeButton) {
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.binarizeMeanIterative()));
        } else if (e.getSource() == this.binEntropyButton) {
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.binarizeEntropySelection(myHistogram.getData())));
        } else if (e.getSource() == this.binMinErrorButton) {
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.binarizeMinimumError(myHistogram.getData())));
        } else if (e.getSource() == this.binFuzzyMinErrorButton) {
            picLabel.setIcon(myImage.fromGrayscaleToImageIcon(myImage.binarizeFuzzyMinimumError(myHistogram.getData())));
        } else if (e.getSource() == this.saveImageButton) {
            if (file.getName().contains(".ppm")) {
                myImage.savePPMToDisk();
            } else {
                myImage.saveToDisk(file.getName());
            }
        }
    }

    public void resetButtons(boolean[] buttons) {
        this.openFileButton.setEnabled(buttons[0]);
        this.showDefaultButton.setEnabled(buttons[1]);
        this.negativeButton.setEnabled(buttons[2]);
        this.additiveButton.setEnabled(buttons[3]);
        this.subscractButton.setEnabled(buttons[4]);
        this.multiplicationButton.setEnabled(buttons[5]);
        this.divisionButton.setEnabled(buttons[6]);
        this.brightnessButton.setEnabled(buttons[7]);
        this.gammaGrayScaleButton.setEnabled(buttons[8]);
        this.lumaGrayScaleButton.setEnabled(buttons[9]);
        this.drawHistogramButton.setEnabled(buttons[10]);
        this.linearNormButton.setEnabled(buttons[11]);
        this.equalisationButton.setEnabled(buttons[12]);
        this.binPercentBlackButton.setEnabled(buttons[13]);
        this.binMeanIterativeButton.setEnabled(buttons[14]);
        this.saveImageButton.setEnabled(buttons[15]);
    }

    public static String getIMAGEPANEL() {
        return IMAGEPANEL;
    }
}
