package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SliderPanel extends JPanel implements ChangeListener, ActionListener {

    private JSlider redSlider;
    private JLabel redSliderLabel;
    private JSlider blueSlider;
    private JLabel blueSliderLabel;
    private JSlider greenSlider;
    private JLabel greenSliderLabel;
    private JSlider cyanSlider;
    private JLabel cyanSliderLabel;
    private JSlider magentaSlider;
    private JLabel magentaSliderLabel;
    private JSlider yellowSlider;
    private JLabel yellowSliderLabel;
    private JSlider blackSlider;
    private JLabel blackSliderLabel;

    private JPanel rgbPanel;
    private JPanel cmykPanel;

    private JPanel modePanel;

    private JRadioButton cmykToRgbButton;
    private JRadioButton rgbToCmykButton;

    private final static String SLIDERPANEL = "Panel for displaying sliders";

    public SliderPanel() {
        this.redSlider = new JSlider(0, 255, 0);
        this.greenSlider = new JSlider(0, 255, 0);
        this.blueSlider = new JSlider(0, 255, 0);
        this.cyanSlider = new JSlider(0, 100, 0);
        this.magentaSlider = new JSlider(0, 100, 0);
        this.yellowSlider = new JSlider(0, 100, 0);
        this.blackSlider = new JSlider(0, 100, 100);

        this.redSliderLabel = new JLabel("R: " + this.redSlider.getValue());
        this.greenSliderLabel = new JLabel("G: " + this.greenSlider.getValue());
        this.blueSliderLabel = new JLabel("B: " + this.blueSlider.getValue());
        this.cyanSliderLabel = new JLabel("C: " + this.cyanSlider.getValue());
        this.magentaSliderLabel = new JLabel("M: " + this.magentaSlider.getValue());
        this.yellowSliderLabel = new JLabel("Y: " + this.yellowSlider.getValue());
        this.blackSliderLabel = new JLabel("K: " + this.blackSlider.getValue());


        this.redSlider.setMajorTickSpacing(255);
        this.redSlider.setPaintLabels(true);
        this.greenSlider.setMajorTickSpacing(255);
        this.greenSlider.setPaintLabels(true);
        this.blueSlider.setMajorTickSpacing(255);
        this.blueSlider.setPaintLabels(true);


        this.cyanSlider.setMajorTickSpacing(100);
        this.cyanSlider.setPaintLabels(true);
        this.magentaSlider.setMajorTickSpacing(100);
        this.magentaSlider.setPaintLabels(true);
        this.yellowSlider.setMajorTickSpacing(100);
        this.yellowSlider.setPaintLabels(true);
        this.blackSlider.setMajorTickSpacing(100);
        this.blackSlider.setPaintLabels(true);

        this.redSlider.addChangeListener(this);
        this.greenSlider.addChangeListener(this);
        this.blueSlider.addChangeListener(this);
        this.cyanSlider.addChangeListener(this);
        this.magentaSlider.addChangeListener(this);
        this.yellowSlider.addChangeListener(this);
        this.blackSlider.addChangeListener(this);

        this.cyanSlider.setEnabled(false);
        this.magentaSlider.setEnabled(false);
        this.yellowSlider.setEnabled(false);
        this.blackSlider.setEnabled(false);

        this.rgbPanel = new JPanel(new GridLayout(6, 1));
        this.rgbPanel.setPreferredSize(new Dimension((MyFrame.getWIDTH() / 2) - 250, MyFrame.getHEIGHT() - 500));
        this.rgbPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        this.cmykPanel = new JPanel(new GridLayout(8, 1));
        this.cmykPanel.setPreferredSize(new Dimension((MyFrame.getWIDTH() / 2) - 250, MyFrame.getHEIGHT() - 500));
        this.cmykPanel.setBorder(new EmptyBorder(0, 0, 0, 50));
        this.modePanel = new JPanel();
        this.modePanel.setPreferredSize(new Dimension(MyFrame.getWIDTH(), 100));
        this.modePanel.setBackground(Color.red);

        this.rgbToCmykButton = new JRadioButton("RGB to CMYK");
        this.cmykToRgbButton = new JRadioButton("CMYK to RGB");

        this.rgbToCmykButton.addActionListener(this);
        this.cmykToRgbButton.addActionListener(this);

        this.rgbToCmykButton.setSelected(true);

        ButtonGroup modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(this.rgbToCmykButton);
        modeButtonGroup.add(this.cmykToRgbButton);

        rgbPanel.add(this.redSlider);
        rgbPanel.add(this.redSliderLabel);
        rgbPanel.add(this.greenSlider);
        rgbPanel.add(this.greenSliderLabel);
        rgbPanel.add(this.blueSlider);
        rgbPanel.add(this.blueSliderLabel);
        cmykPanel.add(this.cyanSlider);
        cmykPanel.add(this.cyanSliderLabel);
        cmykPanel.add(this.magentaSlider);
        cmykPanel.add(this.magentaSliderLabel);
        cmykPanel.add(this.yellowSlider);
        cmykPanel.add(this.yellowSliderLabel);
        cmykPanel.add(this.blackSlider);
        cmykPanel.add(this.blackSliderLabel);
        modePanel.add(this.rgbToCmykButton);
        modePanel.add(this.cmykToRgbButton);

        this.setLayout(new BorderLayout());
        this.add(rgbPanel, BorderLayout.WEST);
        this.add(cmykPanel, BorderLayout.EAST);
        this.add(modePanel, BorderLayout.NORTH);
    }

    public void convertRGBtoCMYK(int[] rgbData) {
        int red = rgbData[0];
        int green = rgbData[1];
        int blue = rgbData[2];

        // Normalize RGB values to the range [0, 1]
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        double black = Math.min(Math.min(1 - r, 1 - g), 1 - b);
        double cyan = (1 - r - black) / (1 - black);
        double magenta = (1 - g - black) / (1 - black);
        double yellow = (1 - b - black) / (1 - black);

        // Scale to byte values (0-255)
        this.cyanSlider.setValue((int) (cyan * 255));
        this.magentaSlider.setValue((int) (magenta * 255));
        this.yellowSlider.setValue((int) (yellow * 255));
        this.blackSlider.setValue((int) (black * 255));

    }

    public void convertCMYKtoRGB(int[] cmykData) {
        // Normalize CMYK values to the range [0, 1]
        float cNorm = cmykData[0] / 100f;
        float mNorm = cmykData[1] / 100f;
        float yNorm = cmykData[2] / 100f;
        float kNorm = cmykData[3] / 100f;

        // Calculate individual RGB values
        int r = (int) ((1 - cNorm) * (1 - kNorm) * 255);
        int g = (int) ((1 - mNorm) * (1 - kNorm) * 255);
        int b = (int) ((1 - yNorm) * (1 - kNorm) * 255);

        // Ensure RGB values are within the valid range [0, 255]
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        this.redSlider.setValue(r);
        this.greenSlider.setValue(g);
        this.blueSlider.setValue(b);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (this.rgbToCmykButton.isSelected()) {

            int[] rgbArray = {this.redSlider.getValue(), this.greenSlider.getValue(), this.blueSlider.getValue()};
            convertRGBtoCMYK(rgbArray);

            if (e.getSource() == this.redSlider) {
                this.redSliderLabel.setText("R: " + this.redSlider.getValue());
            } else if (e.getSource() == this.greenSlider) {
                this.greenSliderLabel.setText("G: " + this.greenSlider.getValue());
            } else if (e.getSource() == this.blueSlider) {
                this.blueSliderLabel.setText("B: " + this.blueSlider.getValue());
            }
        } else if (this.cmykToRgbButton.isSelected()) {

            int[] cmykArray = {this.cyanSlider.getValue(), this.magentaSlider.getValue(), this.yellowSlider.getValue(), this.blackSlider.getValue()};
            convertCMYKtoRGB(cmykArray);

            if (e.getSource() == this.cyanSlider) {
                this.cyanSliderLabel.setText("C: " + this.cyanSlider.getValue());
            } else if (e.getSource() == this.magentaSlider) {
                this.magentaSliderLabel.setText("M: " + this.magentaSlider.getValue());
            } else if (e.getSource() == this.yellowSlider) {
                this.yellowSliderLabel.setText("Y: " + this.yellowSlider.getValue());
            } else if (e.getSource() == this.blackSlider) {
                this.blackSliderLabel.setText("K: " + this.blackSlider.getValue());
            }
        }
    }

    public static String getSLIDERPANEL() {
        return SLIDERPANEL;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.rgbToCmykButton) {
            System.out.println("RGB TO CMYK");
            this.redSlider.setEnabled(true);
            this.greenSlider.setEnabled(true);
            this.blueSlider.setEnabled(true);
            this.cyanSlider.setEnabled(false);
            this.magentaSlider.setEnabled(false);
            this.yellowSlider.setEnabled(false);
            this.blackSlider.setEnabled(false);
        } else if (e.getSource() == this.cmykToRgbButton) {
            System.out.println("CMYK TO RGB");
            this.redSlider.setEnabled(false);
            this.greenSlider.setEnabled(false);
            this.blueSlider.setEnabled(false);
            this.cyanSlider.setEnabled(true);
            this.magentaSlider.setEnabled(true);
            this.yellowSlider.setEnabled(true);
            this.blackSlider.setEnabled(true);
        }
    }
}
