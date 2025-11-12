package client;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Authentic Ludo Board Panel - Classic Ludo Game Design
 * Features traditional cross-shaped path, colored home areas, and safe zones
 */
public class BoardPanel extends JPanel {
    private Map<String, Integer> positions;
    
    // Classic Ludo colors
    private static final Color RED = new Color(239, 68, 68);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color YELLOW = new Color(250, 204, 21);
    private static final Color BLUE = new Color(59, 130, 246);
    
    private static final Color RED_LIGHT = new Color(254, 202, 202);
    private static final Color GREEN_LIGHT = new Color(187, 247, 208);
    private static final Color YELLOW_LIGHT = new Color(254, 249, 195);
    private static final Color BLUE_LIGHT = new Color(191, 219, 254);
    
    private static final Color PATH_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(0, 0, 0);
    private static final Color STAR_COLOR = new Color(255, 215, 0);
    
    // Board dimensions (15x15 grid)
    private static final int GRID_SIZE = 15;
    private static final int CELL_SIZE = 36;
    
    public BoardPanel() {
        setBackground(new Color(240, 240, 245));
        setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 40, GRID_SIZE * CELL_SIZE + 40));
    }
    
    public void updatePositions(Map<String, Integer> pos) {
        this.positions = pos;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int boardSize = GRID_SIZE * CELL_SIZE;
        int offsetX = (getWidth() - boardSize) / 2;
        int offsetY = (getHeight() - boardSize) / 2;
        
        // Draw board shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRoundRect(offsetX + 3, offsetY + 3, boardSize, boardSize, 15, 15);
        
        // Draw white board background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(offsetX, offsetY, boardSize, boardSize, 15, 15);
        
        // Draw four colored home areas (6x6 each) - Traditional Ludo layout
        drawHomeArea(g2d, offsetX, offsetY, RED_LIGHT, RED); // Top-left (Red)
        drawHomeArea(g2d, offsetX + 9 * CELL_SIZE, offsetY, GREEN_LIGHT, GREEN); // Top-right (Green)
        drawHomeArea(g2d, offsetX, offsetY + 9 * CELL_SIZE, YELLOW_LIGHT, YELLOW); // Bottom-left (Yellow)
        drawHomeArea(g2d, offsetX + 9 * CELL_SIZE, offsetY + 9 * CELL_SIZE, BLUE_LIGHT, BLUE); // Bottom-right (Blue)
        
        // Draw the cross-shaped paths
        drawPaths(g2d, offsetX, offsetY);
        
        // Draw home triangles (winning paths)
        drawHomeTriangles(g2d, offsetX, offsetY);
        
        // Draw center home area
        drawCenterHome(g2d, offsetX + boardSize / 2, offsetY + boardSize / 2);
        
        // Draw grid lines
        drawGridLines(g2d, offsetX, offsetY, boardSize);
        
        // Draw player tokens
        if (positions != null) {
            drawTokens(g2d, offsetX, offsetY);
        }
    }
    
    private void drawHomeArea(Graphics2D g2d, int x, int y, Color lightColor, Color darkColor) {
        // Draw 6x6 colored square
        g2d.setColor(lightColor);
        g2d.fillRect(x, y, 6 * CELL_SIZE, 6 * CELL_SIZE);
        
        // Draw border
        g2d.setColor(darkColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x, y, 6 * CELL_SIZE, 6 * CELL_SIZE);
        
        // Draw starting area circles (2x2 grid in center)
        int centerX = x + (6 * CELL_SIZE) / 2;
        int centerY = y + (6 * CELL_SIZE) / 2;
        int circleSize = CELL_SIZE;
        int spacing = (int)(CELL_SIZE * 1.5);
        
        int[][] positions = {
            {centerX - spacing/2 - circleSize/2, centerY - spacing/2 - circleSize/2},
            {centerX + spacing/2 - circleSize/2, centerY - spacing/2 - circleSize/2},
            {centerX - spacing/2 - circleSize/2, centerY + spacing/2 - circleSize/2},
            {centerX + spacing/2 - circleSize/2, centerY + spacing/2 - circleSize/2}
        };
        
        for (int[] pos : positions) {
            // Draw circle shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillOval(pos[0] + 2, pos[1] + 2, circleSize, circleSize);
            
            // Draw circle
            g2d.setColor(Color.WHITE);
            g2d.fillOval(pos[0], pos[1], circleSize, circleSize);
            g2d.setColor(darkColor);
            g2d.setStroke(new BasicStroke(2.5f));
            g2d.drawOval(pos[0], pos[1], circleSize, circleSize);
        }
    }
    
    private void drawPaths(Graphics2D g2d, int offsetX, int offsetY) {
        // Traditional Ludo has a cross-shaped path around the board
        // Each arm of the cross is 3 cells wide
        
        // Left vertical arm (Red's path going up)
        for (int row = 0; row < 6; row++) {
            for (int col = 6; col < 9; col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                g2d.setColor(PATH_COLOR);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Top horizontal arm (Green's path going left)
        for (int row = 6; row < 9; row++) {
            for (int col = 9; col < 15; col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                g2d.setColor(PATH_COLOR);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Right vertical arm (Blue's path going down)
        for (int row = 9; row < 15; row++) {
            for (int col = 6; col < 9; col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                g2d.setColor(PATH_COLOR);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Bottom horizontal arm (Yellow's path going right)
        for (int row = 6; row < 9; row++) {
            for (int col = 0; col < 6; col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                g2d.setColor(PATH_COLOR);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Draw starting positions and safe spots
        // Red starting position (moved left 4 boxes)
        drawStartingCircle(g2d, offsetX + 2 * CELL_SIZE, offsetY + 6 * CELL_SIZE, RED);
        drawStar(g2d, offsetX + (7 * CELL_SIZE) + CELL_SIZE/2, offsetY + (2 * CELL_SIZE) + CELL_SIZE/2, 8);
        
        // Green starting position (moved up 4 boxes)
        drawStartingCircle(g2d, offsetX + 8 * CELL_SIZE, offsetY + 2 * CELL_SIZE, GREEN);
        drawStar(g2d, offsetX + (12 * CELL_SIZE) + CELL_SIZE/2, offsetY + (7 * CELL_SIZE) + CELL_SIZE/2, 8);
        
        // Blue starting position (top of right column - moved right 4 boxes)
        drawStartingCircle(g2d, offsetX + 12 * CELL_SIZE, offsetY + 8 * CELL_SIZE, BLUE);
        drawStar(g2d, offsetX + (7 * CELL_SIZE) + CELL_SIZE/2, offsetY + (12 * CELL_SIZE) + CELL_SIZE/2, 8);
        
        // Yellow starting position (right of bottom row - moved down 4 boxes)
        drawStartingCircle(g2d, offsetX + 6 * CELL_SIZE, offsetY + 12 * CELL_SIZE, YELLOW);
        drawStar(g2d, offsetX + (2 * CELL_SIZE) + CELL_SIZE/2, offsetY + (7 * CELL_SIZE) + CELL_SIZE/2, 8);
    }
    
    private void drawStartingCircle(Graphics2D g2d, int x, int y, Color color) {
        // Draw a colored circle to mark starting position
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }
    
    private void drawHomeTriangles(Graphics2D g2d, int offsetX, int offsetY) {
        // Home triangles - colored paths leading to center from each corner
        // Red home path (from right to center, one row down from middle)
        for (int i = 0; i < 6; i++) {
            g2d.setColor(RED);
            g2d.fillRect(offsetX + (8 + i) * CELL_SIZE, offsetY + 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        // Green home path (from top to center, vertical like Blue)
        for (int i = 0; i < 6; i++) {
            g2d.setColor(GREEN);
            g2d.fillRect(offsetX + 7 * CELL_SIZE, offsetY + (i + 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        // Yellow home path (from bottom left, pointing toward center)
        for (int i = 0; i < 6; i++) {
            g2d.setColor(YELLOW);
            g2d.fillRect(offsetX + (i + 1) * CELL_SIZE, offsetY + 7 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        // Blue home path (from bottom right, pointing toward center)
        for (int i = 0; i < 6; i++) {
            g2d.setColor(BLUE);
            g2d.fillRect(offsetX + 7 * CELL_SIZE, offsetY + (8 + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }
    
    private void drawCenterHome(Graphics2D g2d, int centerX, int centerY) {
        int size = (int)(CELL_SIZE * 1.5);
        
        // Draw four colored triangles pointing to center forming a square pattern
        // Yellow triangle (from bottom - bottom left)
        int[] yellowX = {centerX - size, centerX, centerX};
        int[] yellowY = {centerY, centerY, centerY + size};
        g2d.setColor(YELLOW);
        g2d.fillPolygon(yellowX, yellowY, 3);
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawPolygon(yellowX, yellowY, 3);
        
        // Green triangle (from top - top right)
        int[] greenX = {centerX, centerX + size, centerX};
        int[] greenY = {centerY - size, centerY, centerY};
        g2d.setColor(GREEN);
        g2d.fillPolygon(greenX, greenY, 3);
        g2d.setColor(BORDER_COLOR);
        g2d.drawPolygon(greenX, greenY, 3);
        
        // Blue triangle (from right - bottom right)
        int[] blueX = {centerX, centerX + size, centerX};
        int[] blueY = {centerY, centerY, centerY + size};
        g2d.setColor(BLUE);
        g2d.fillPolygon(blueX, blueY, 3);
        g2d.setColor(BORDER_COLOR);
        g2d.drawPolygon(blueX, blueY, 3);
        
        // Red triangle (from left - top left)
        int[] redX = {centerX - size, centerX, centerX};
        int[] redY = {centerY, centerY, centerY - size};
        g2d.setColor(RED);
        g2d.fillPolygon(redX, redY, 3);
        g2d.setColor(BORDER_COLOR);
        g2d.drawPolygon(redX, redY, 3);
        
        // Draw center circle
        int circleSize = (int)(size * 0.6);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
        
        // Draw star in center
        g2d.setColor(STAR_COLOR);
        drawStar(g2d, centerX, centerY, circleSize/3);
        
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
    }
    
    private void drawGridLines(Graphics2D g2d, int offsetX, int offsetY, int boardSize) {
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(1));
        
        // Draw vertical lines
        for (int i = 0; i <= GRID_SIZE; i++) {
            int x = offsetX + i * CELL_SIZE;
            g2d.drawLine(x, offsetY, x, offsetY + boardSize);
        }
        
        // Draw horizontal lines
        for (int i = 0; i <= GRID_SIZE; i++) {
            int y = offsetY + i * CELL_SIZE;
            g2d.drawLine(offsetX, y, offsetX + boardSize, y);
        }
        
        // Draw bold border
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(offsetX, offsetY, boardSize, boardSize, 15, 15);
    }
    
    private void drawTokens(Graphics2D g2d, int offsetX, int offsetY) {
        Color[] playerColors = {RED, GREEN, YELLOW, BLUE};
        int index = 0;
        
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            int pos = entry.getValue();
            Color color = playerColors[index % 4];
            
            // Convert position to grid coordinates
            int gridX = pos % GRID_SIZE;
            int gridY = pos / GRID_SIZE;
            
            int x = offsetX + gridX * CELL_SIZE;
            int y = offsetY + gridY * CELL_SIZE;
            
            drawToken(g2d, x, y, color, entry.getKey());
            index++;
        }
    }
    
    private void drawToken(Graphics2D g2d, int x, int y, Color color, String label) {
        int tokenSize = CELL_SIZE - 8;
        int centerX = x + CELL_SIZE / 2 - tokenSize / 2;
        int centerY = y + CELL_SIZE / 2 - tokenSize / 2;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(centerX + 2, centerY + 2, tokenSize, tokenSize);
        
        // Draw token
        g2d.setColor(color);
        g2d.fillOval(centerX, centerY, tokenSize, tokenSize);
        
        // Draw border
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(centerX, centerY, tokenSize, tokenSize);
        
        // Draw highlight
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillOval(centerX + tokenSize/4, centerY + tokenSize/4, tokenSize/3, tokenSize/3);
        
        // Draw label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String initial = label.substring(0, Math.min(1, label.length())).toUpperCase();
        int textX = centerX + (tokenSize - fm.stringWidth(initial)) / 2;
        int textY = centerY + (tokenSize + fm.getAscent()) / 2 - 2;
        g2d.drawString(initial, textX, textY);
    }
    
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int radius) {
        int points = 5;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        int innerRadius = radius / 2;
        
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI / 2 + (i * Math.PI / points);
            int r = (i % 2 == 0) ? radius : innerRadius;
            xPoints[i] = (int) (centerX + r * Math.cos(angle));
            yPoints[i] = (int) (centerY - r * Math.sin(angle));
        }
        
        g2d.setColor(STAR_COLOR);
        g2d.fillPolygon(xPoints, yPoints, points * 2);
        g2d.setColor(STAR_COLOR.darker());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, points * 2);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GRID_SIZE * CELL_SIZE + 40, GRID_SIZE * CELL_SIZE + 40);
    }
}
