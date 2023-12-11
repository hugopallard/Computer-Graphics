package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements MouseListener {

    private final int cubeSize;
    private final ArrayList<Point> bezierPoints;
    private final ArrayList<Point> rectanglePoints;
    private final ArrayList<Point> linePoints;
    private String currentShape;
    private Point circleCenter;
    private Point rgbCubeCenter;

    public DrawPanel() {

        this.cubeSize = 100;
        this.bezierPoints = new ArrayList<>();
        this.rectanglePoints = new ArrayList<>();
        this.linePoints = new ArrayList<>();
        this.addMouseListener(this);
    }

    private void drawCircle(Graphics2D g2d, Point center) {
        if (center == null) {
            System.out.println("Can't draw a circle with a null center Point. Start again");
            return;
        }
        g2d.drawOval((int) center.getX(), (int) center.getY(), 100, 100);
    }

    private void drawRectangle(Graphics2D g2d, ArrayList<Point> rectanglePoints) {

        if (rectanglePoints.size() != 4) {
            System.out.println("Can't draw a rectangle with: " + rectanglePoints.size() + ". Start again");
            rectanglePoints.clear();
            return;
        }

        Path2D.Double path = new Path2D.Double();
        // Move to the first point
        path.moveTo(rectanglePoints.get(0).getX(), rectanglePoints.get(0).getY());

        // Connect the points to form the rectangle
        for (int i = 1; i < rectanglePoints.size(); i++) {
            path.lineTo(rectanglePoints.get(i).getX(), rectanglePoints.get(i).getY());
        }

        // Connect the last point to the first point to close the shape
        path.lineTo(rectanglePoints.get(0).getX(), rectanglePoints.get(0).getY());

        // Draw the rectangle
        g2d.draw(path);
    }

    private void drawLine(Graphics2D g2d, ArrayList<Point> linePoints) {
        if (linePoints.size() != 2) {
            System.out.println("Can't draw a line with: " + linePoints.size() + ". Start again");
            linePoints.clear();
            return;
        }
        Path2D.Double path = new Path2D.Double();
        // Move to the first point
        path.moveTo(linePoints.get(0).getX(), linePoints.get(0).getY());
        path.lineTo(linePoints.get(1).getX(), linePoints.get(1).getY());

        // Draw the line
        g2d.draw(path);
    }

    private void draw3DCube(Graphics2D g2d, Point center) {
        if (center == null) {
            System.out.println("Can't draw a circle with a null center Point. Start again");
            return;
        }

        int x = (int) center.getX();
        int y = (int) center.getY();

        // Draw the faces
        int[][] faces = {
                {x, y, x + cubeSize, y, x + cubeSize, y + cubeSize, x, y + cubeSize},  // Front face
                {x, y, x + cubeSize / 3, y - cubeSize / 3, x + cubeSize + cubeSize / 3, y - cubeSize / 3, x + cubeSize, y},  // Top face
                {x + cubeSize, y, x + cubeSize + cubeSize / 3, y - cubeSize / 3, x + cubeSize + cubeSize / 3, y + cubeSize - cubeSize / 3, x + cubeSize, y + cubeSize}  // Right face
        };

        for (int i = 0; i < faces.length; i++) {
            int[] face = faces[i];

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            for (int j = 0; j < 4; j++) {
                xPoints[j] = face[j * 2];
                yPoints[j] = face[j * 2 + 1];
            }

            if (i == 0) { // Front face
                // Draw the gradient for the front face (flipped by 90° around the x-axis)
                Color c1 = new Color(255, 0, 255);  // Bottom-left (Magenta)
                Color c2 = new Color(255, 255, 255);  // Top-left (White)
                Color c3 = new Color(255, 255, 0);  // Top-right (Cyan)
                Color c4 = new Color(255, 0, 0);  // Bottom-right (Blue)

                for (int xCoord = xPoints[0]; xCoord <= xPoints[1]; xCoord++) {
                    for (int yCoord = yPoints[0]; yCoord <= yPoints[3]; yCoord++) {
                        float ratioX = (float) (xCoord - xPoints[0]) / (xPoints[1] - xPoints[0]);
                        float ratioY = (float) (yCoord - yPoints[0]) / (yPoints[3] - yPoints[0]);

                        int r = (int) ((c1.getRed() * (1 - ratioX) + c2.getRed() * ratioX) * (1 - ratioY) + (c4.getRed() * (1 - ratioX) + c3.getRed() * ratioX) * ratioY);
                        int g = (int) ((c1.getGreen() * (1 - ratioX) + c2.getGreen() * ratioX) * (1 - ratioY) + (c4.getGreen() * (1 - ratioX) + c3.getGreen() * ratioX) * ratioY);
                        int b = (int) ((c1.getBlue() * (1 - ratioX) + c2.getBlue() * ratioX) * (1 - ratioY) + (c4.getBlue() * (1 - ratioX) + c3.getBlue() * ratioX) * ratioY);

                        g2d.setColor(new Color(r, g, b));
                        g2d.drawLine(xCoord, yCoord, xCoord, yCoord);
                    }
                }
            } else if (i == 1) { // Top face
                // Draw the gradient for the top face

                Color c1 = new Color(255, 0, 255);
                Color c2 = new Color(255, 255, 255);
                Color c3 = new Color(0, 255, 255);
                Color c4 = new Color(0, 0, 255);

                for (int yCoord = yPoints[0]; yCoord >= yPoints[2]; yCoord--) {
                    float ratioY = (float) (yPoints[0] - yCoord) / (yPoints[0] - yPoints[2]);

                    int xStart = (int) (xPoints[0] + ratioY * (xPoints[1] - xPoints[0]));
                    int xEnd = (int) (xPoints[3] - ratioY * (xPoints[3] - xPoints[2]));

                    for (int xCoord = xStart; xCoord <= xEnd; xCoord++) {
                        float ratioX = (float) (xCoord - xStart) / (xEnd - xStart);

                        int r = (int) ((c1.getRed() * (1 - ratioX) + c2.getRed() * ratioX) * (1 - ratioY) + (c4.getRed() * (1 - ratioX) + c3.getRed() * ratioX) * ratioY);
                        int g = (int) ((c1.getGreen() * (1 - ratioX) + c2.getGreen() * ratioX) * (1 - ratioY) + (c4.getGreen() * (1 - ratioX) + c3.getGreen() * ratioX) * ratioY);
                        int b = (int) ((c1.getBlue() * (1 - ratioX) + c2.getBlue() * ratioX) * (1 - ratioY) + (c4.getBlue() * (1 - ratioX) + c3.getBlue() * ratioX) * ratioY);

                        g2d.setColor(new Color(r, g, b));
                        g2d.drawLine(xCoord, yCoord, xCoord, yCoord);
                    }
                }
            } else {
                // Set gradient colors for the right face
                Color c1 = new Color(255, 255, 255);  // Top-left (White)
                Color c2 = new Color(0, 255, 255);    // Top-right (Cyan)
                Color c3 = new Color(0, 255, 0);      // Bottom-right (Green)
                Color c4 = new Color(255, 255, 0);    // Bottom-left (Yellow)

                // Draw the right face with a gradient using drawLine()
                for (int k = 0; k < yPoints.length; k++) {
                    int xCoordStart = xPoints[k];
                    int yCoordStart = yPoints[k];
                    int xCoordEnd = xPoints[(k + 1) % yPoints.length];
                    int yCoordEnd = yPoints[(k + 1) % yPoints.length];

                    int numberOfSteps = Math.max(Math.abs(xCoordEnd - xCoordStart), Math.abs(yCoordEnd - yCoordStart));

                    for (int step = 0; step <= numberOfSteps; step++) {
                        float t = (float) step / numberOfSteps;

                        int xCoord = Math.round(xCoordStart + t * (xCoordEnd - xCoordStart));
                        int yCoord = Math.round(yCoordStart + t * (yCoordEnd - yCoordStart));

                        float ratioX = (float) (xCoord - xPoints[0]) / (xPoints[1] - xPoints[0]);
                        float ratioY = (float) (yCoord - yPoints[0]) / (yPoints[3] - yPoints[0]);

                        int r = (int) ((c1.getRed() * (1 - ratioX) + c2.getRed() * ratioX) * (1 - ratioY)
                                + (c4.getRed() * (1 - ratioX) + c3.getRed() * ratioX) * ratioY);
                        int gValue = (int) ((c1.getGreen() * (1 - ratioX) + c2.getGreen() * ratioX) * (1 - ratioY)
                                + (c4.getGreen() * (1 - ratioX) + c3.getGreen() * ratioX) * ratioY);
                        int b = (int) ((c1.getBlue() * (1 - ratioX) + c2.getBlue() * ratioX) * (1 - ratioY)
                                + (c4.getBlue() * (1 - ratioX) + c3.getBlue() * ratioX) * ratioY);

                        r = Math.min(255, Math.max(0, r));
                        gValue = Math.min(255, Math.max(0, gValue));
                        b = Math.min(255, Math.max(0, b));

                        g2d.setColor(new Color(r, gValue, b));
                        g2d.drawLine(xCoord, yCoord, xCoord, yCoord);
                    }
                }
            }
        }
    }

    private void drawBezierCurve(Graphics2D g2d, ArrayList<Point> bezierPoints) {

        if (bezierPoints.size() < 3 || bezierPoints.size() % 3 != 1) {
            System.out.println("Not enough points to draw a Bezier curve: " + bezierPoints.size() + ". Start again");
            bezierPoints.clear();
            return;
        }

        g2d.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

        // Draw control points
        for (int i = 0; i < bezierPoints.size(); i++) {
            Point point = bezierPoints.get(i);
            g2d.fillOval(point.x - 5, point.y - 5, 10, 10);
            g2d.drawString(Integer.toString(i), point.x + 10, point.y - 10);
        }

        // Draw the Bezier curve
        g2d.setColor(Color.BLACK);
        int n = bezierPoints.size() - 1;

        Point prevPoint = null;
        for (double t = 0; t <= 1.0; t += 0.01) {
            int x = 0;
            int y = 0;

            for (int i = 0; i <= n; i++) {
                double basis = binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);
                x += (int) (basis * bezierPoints.get(i).x);
                y += (int) (basis * bezierPoints.get(i).y);
            }

            if (prevPoint != null) {
                g2d.drawLine(prevPoint.x, prevPoint.y, x, y);
            }

            prevPoint = new Point(x, y);
        }

        bezierPoints.clear();
    }

    private long binomialCoefficient(int n, int k) {
        long result = 1;

        for (int i = 1; i <= k; i++) {
            result = Math.floorDiv(result * (n - i + 1), i);
        }

        return result;
    }

    public void rotateRectangle() {
        if (rectanglePoints.size() != 4) {
            System.out.println("Can't rotate a rectangle with: " + rectanglePoints.size() + ". Start again");
            rectanglePoints.clear();
            return;
        }
        // Find the top-left vertex (let's assume it's the first point in the list)
        // We set the origin of the coordinates system as the top-left vertex
        Point topLeft = rectanglePoints.get(0);

        // Calculate the angle in radians
        double angle = Math.toRadians(45.0);

        // Rotate each point around the top-left vertex
        for (Point rectanglePoint : rectanglePoints) {
            int x = rectanglePoint.x - topLeft.x;
            int y = rectanglePoint.y - topLeft.y;

            // Perform rotation using rotation matrix
            int newX = (int) (x * Math.cos(angle) - y * Math.sin(angle));
            int newY = (int) (x * Math.sin(angle) + y * Math.cos(angle));

            // Adjust back to the original coordinate system
            rectanglePoint.x = newX + topLeft.x;
            rectanglePoint.y = newY + topLeft.y;
        }

        // After rotating all points, trigger repaint
        repaint();
    }

    public void shearRectangle(String axis) {
        if (rectanglePoints.size() != 4) {
            System.out.println("Can't rotate a rectangle with: " + rectanglePoints.size() + ". Start again");
            rectanglePoints.clear();
            return;
        }
        // Find the top-left vertex (let's assume it's the first point in the list)
        // We set the origin of the coordinates system as the top-left vertex
        Point topLeft = rectanglePoints.get(0);

        int shearFactorK = 4;

        // Shear each point along the given axis
        for (Point rectanglePoint : rectanglePoints) {
            int x = rectanglePoint.x - topLeft.x;
            int y = rectanglePoint.y - topLeft.y;

            int newX;
            int newY;

            // Perform shearing
            if (axis.equals("x")) {

                newX = (x + shearFactorK * y);
                newY = y;
            } else {

                newX = x;
                newY = y + shearFactorK * x;
            }

            // Adjust back to the original coordinate system
            rectanglePoint.x = newX + topLeft.x;
            rectanglePoint.y = newY + topLeft.y;
        }

        // After rotating all points, trigger repaint
        repaint();
    }

    public void scaleRectangle(int width, int height) {

        // Find the top-left vertex (let's assume it's the first point in the list)
        Point topLeft = rectanglePoints.get(0);

        // Scale each point around the top-left vertex without using an external object
        for (Point rectanglePoint : rectanglePoints) {
            int x = rectanglePoint.x - topLeft.x;
            int y = rectanglePoint.y - topLeft.y;

            // Perform scaling
            int newX = x * width;
            int newY = y * height;

            // Adjust back to the original coordinate system
            rectanglePoint.x = newX + topLeft.x;
            rectanglePoint.y = newY + topLeft.y;
        }

        // After scaling all points, trigger repaint
        repaint();
    }

    public void reflect(String about) {
        if (rectanglePoints.size() != 4) {
            System.out.println("Can't rotate a rectangle with: " + rectanglePoints.size() + ". Start again");
            rectanglePoints.clear();
            return;
        }

        // Find the top-left vertex (let's assume it's the first point in the list)
        // We set the origin of the coordinates system as the top-left vertex
        Point topLeft = rectanglePoints.get(0);

        // SCale each point around the top-left vertex
        for (Point rectanglePoint : rectanglePoints) {
            int x = rectanglePoint.x - topLeft.x;
            int y = rectanglePoint.y - topLeft.y;

            // Default initialization
            int newX = 0;
            int newY = 0;

            // Perform reflection
            switch (about) {
                case "origin" -> {
                    newX = -x;
                    newY = -y;
                }
                case "xAxis" -> {
                    newX = x;
                    newY = -y;
                }
                case "yAxis" -> {
                    newX = -x;
                    newY = y;
                }
            }


            // Adjust back to the original coordinate system
            rectanglePoint.x = newX + topLeft.x;
            rectanglePoint.y = newY + topLeft.y;
        }

        // After rotating all points, trigger repaint
        repaint();
    }

    public void translate(int deltaX, int deltaY) {
        if (rectanglePoints.size() != 4) {
            System.out.println("Can't translate a rectangle with: " + rectanglePoints.size() + " points. Start again");
            rectanglePoints.clear();
            return;
        }

        // Find the top-left vertex (let's assume it's the first point in the list)
        // We set the origin of the coordinates system as the top-left vertex
        Point topLeft = rectanglePoints.get(0);

        // Translate each point by adding the specified delta values
        for (Point rectanglePoint : rectanglePoints) {
            rectanglePoint.x += deltaX;
            rectanglePoint.y += deltaY;
        }

        // After translating all points, trigger repaint
        repaint();
    }

    public void clearPanel() {
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.clearRect(0, 0, getWidth(), getHeight());
        rectanglePoints.clear();
        bezierPoints.clear();
        // rotateButton.setEnabled(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (currentShape == null) {
            return;
        }
        switch (currentShape) {
            case "circle" -> {
                circleCenter = new Point(e.getX(), e.getY());
                repaint();
            }
            case "rectangle" -> {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left mouse button clicked, add Point to the array
                    rectanglePoints.add(new Point(e.getX(), e.getY()));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    repaint();
                }
            }
            case "line" -> {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    linePoints.add(new Point(e.getX(), e.getY()));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    repaint();
                }
            }
            case "rgbCube" -> {
                rgbCubeCenter = new Point(e.getX(), e.getY());
                repaint();
            }
            case "bezierCurve" -> {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    bezierPoints.add(new Point(e.getX(), e.getY()));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    repaint();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (currentShape == null) {
            return;
        }

        switch (currentShape) {
            case "line" -> {
                drawLine(g2d, linePoints);
            }
            case "rectangle" -> {
                drawRectangle(g2d, rectanglePoints);
            }
            case "circle" -> {
                drawCircle(g2d, circleCenter);
            }
            case "rgbCube" -> {
                draw3DCube(g2d, rgbCubeCenter);
            }
            case "bezierCurve" -> {
                drawBezierCurve(g2d, bezierPoints);
            }
        }
        g2d.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public void setCurrentShape(String currentShape) {
        this.currentShape = currentShape;
    }

}
