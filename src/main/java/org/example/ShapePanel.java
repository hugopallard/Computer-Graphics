package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapePanel extends JPanel implements ActionListener {
    private final static String SHAPEPANEL = "Panel for shapes";
    private final JButton rotateButton;
    private final JButton drawCircleButton;
    private final JButton drawRectButton;
    private final JButton drawLineButton;
    private final JButton drawRgbCubeButton;
    private final JButton drawBezierCurveButton;
    private final JButton clearButton;
    private final DrawPanel drawPanel;
    private final JButton shearButtonX;
    private final JButton shearButtonY;
    private final JButton scaleButton;
    private final JButton reflectOriginButton;
    private final JButton reflectXButton;
    private final JButton reflectYButton;
    private final JButton translateButton;

    public ShapePanel() {

        drawCircleButton = new JButton("Draw circle");
        drawCircleButton.addActionListener(this);

        drawRectButton = new JButton("Draw rectangle");
        drawRectButton.addActionListener(this);

        drawLineButton = new JButton("Draw line");
        drawLineButton.addActionListener(this);

        drawRgbCubeButton = new JButton("Draw RGB Cube");
        drawRgbCubeButton.addActionListener(this);

        drawBezierCurveButton = new JButton("Draw Bezier curve");
        drawBezierCurveButton.addActionListener(this);

        rotateButton = new JButton("Rotate rectangle by 45");
        rotateButton.addActionListener(this);

        shearButtonX = new JButton("Shear rectangle along X-axis");
        shearButtonX.addActionListener(this);

        shearButtonY = new JButton("Shear rectangle along Y-axis");
        shearButtonY.addActionListener(this);

        scaleButton = new JButton("Scale rectangle");
        scaleButton.addActionListener(this);

        reflectOriginButton = new JButton("Reflect rectangle (Origin)");
        reflectOriginButton.addActionListener(this);

        reflectXButton = new JButton("Reflect rectangle (X-Axis)");
        reflectXButton.addActionListener(this);

        reflectYButton = new JButton("Reflect rectangle (Y-Axis)");
        reflectYButton.addActionListener(this);

        translateButton = new JButton("Translate");
        translateButton.addActionListener(this);

        clearButton = new JButton("Clear screen");
        clearButton.addActionListener(this);

        JPanel commandPanel = new JPanel();
        commandPanel.setPreferredSize(new Dimension(250, MyFrame.getHEIGHT()));
        commandPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        commandPanel.add(drawCircleButton);
        commandPanel.add(drawRectButton);
        commandPanel.add(drawLineButton);
        commandPanel.add(drawRgbCubeButton);
        commandPanel.add(drawBezierCurveButton);
        commandPanel.add(rotateButton);
        commandPanel.add(shearButtonX);
        commandPanel.add(shearButtonY);
        commandPanel.add(scaleButton);
        commandPanel.add(reflectOriginButton);
        commandPanel.add(reflectXButton);
        commandPanel.add(reflectYButton);
        commandPanel.add(translateButton);
        commandPanel.add(clearButton);
        commandPanel.setBackground(Color.RED);

        this.setLayout(new BorderLayout());
        commandPanel.setBackground(Color.RED);
        this.add(commandPanel, BorderLayout.WEST);

        drawPanel = new DrawPanel();
        this.add(drawPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rotateButton) {
            drawPanel.rotateRectangle();
        } else if (e.getSource() == shearButtonX) {
            drawPanel.shearRectangle("x");
        } else if (e.getSource() == shearButtonY) {
            drawPanel.shearRectangle("y");
        } else if (e.getSource() == scaleButton) {
            drawPanel.scaleRectangle(4, 4);
        } else if (e.getSource() == reflectOriginButton) {
            drawPanel.reflect("origin");
        } else if (e.getSource() == reflectXButton) {
            drawPanel.reflect("xAxis");
        } else if (e.getSource() == reflectYButton) {
            drawPanel.reflect("yAxis");
        } else if (e.getSource() == translateButton) {
            drawPanel.translate(50, 50);
        } else if (e.getSource() == drawCircleButton) {
            System.out.println("Switched to circle");
            drawPanel.setCurrentShape("circle");
        } else if (e.getSource() == drawRectButton) {
            System.out.println("Switched to rectangle");
            drawPanel.setCurrentShape("rectangle");
        } else if (e.getSource() == drawLineButton) {
            System.out.println("Switched to line");
            drawPanel.setCurrentShape("line");
        } else if (e.getSource() == drawRgbCubeButton) {
            System.out.println("Switched to RGB Cube");
            drawPanel.setCurrentShape("rgbCube");
        } else if (e.getSource() == drawBezierCurveButton) {
            System.out.println("Switched to Bezier Curve");
            drawPanel.setCurrentShape("bezierCurve");
        } else if (e.getSource() == clearButton) {
            System.out.println("Clear panel");
            drawPanel.clearPanel();
        }
    }

    public static String getSHAPEPANEL() {
        return SHAPEPANEL;
    }


}