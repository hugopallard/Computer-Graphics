package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements ActionListener {
    private final CardLayout cardLayout;
    private final JPanel panelContainer;
    private final JMenuItem shapeMenuItem;
    private final JMenuItem imageMenuItem;
    private final JMenuItem rgbCMYKMenuItem;
    private final static int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final static int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public MyFrame() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu panelChooser = new JMenu("Current Panel");
        shapeMenuItem = new JMenuItem("Shape panel");
        imageMenuItem = new JMenuItem("Image panel");
        rgbCMYKMenuItem = new JMenuItem("RGB-CMYK panel");

        panelChooser.add(shapeMenuItem);
        panelChooser.add(imageMenuItem);
        panelChooser.add(rgbCMYKMenuItem);
        jMenuBar.add(panelChooser);

        shapeMenuItem.addActionListener(this);
        imageMenuItem.addActionListener(this);
        rgbCMYKMenuItem.addActionListener(this);

        // Assuming ShapePanel is included elsewhere
        ShapePanel shapePanel = new ShapePanel();
        ImagePanel imagePanel = new ImagePanel();
        SliderPanel sliderPanel = new SliderPanel();

    /*// Create a PPM file
    new PPMImage("P6", 200, 200, 255, "output.ppm");*/

        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);
        panelContainer.add(shapePanel, ShapePanel.getSHAPEPANEL());
        panelContainer.add(imagePanel, ImagePanel.getIMAGEPANEL());
        panelContainer.add(sliderPanel, SliderPanel.getSLIDERPANEL());

        cardLayout.show(panelContainer, ShapePanel.getSHAPEPANEL());

        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(jMenuBar);
        this.add(panelContainer);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shapeMenuItem) {
            System.out.println("Go to shape panel");
            cardLayout.show(panelContainer, ShapePanel.getSHAPEPANEL());
        } else if (e.getSource() == imageMenuItem) {
            System.out.println("Go to image panel");
            cardLayout.show(panelContainer, ImagePanel.getIMAGEPANEL());
        } else if (e.getSource() == rgbCMYKMenuItem) {
            System.out.println("Go to slider panel");
            cardLayout.show(panelContainer, SliderPanel.getSLIDERPANEL());
        }
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
}
