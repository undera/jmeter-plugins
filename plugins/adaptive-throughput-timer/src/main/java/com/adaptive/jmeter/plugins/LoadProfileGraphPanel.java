package com.adaptive.jmeter.plugins;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Swing panel for rendering the load profile graph visualization.
 * Displays TPS over time with ramp-up, sustained, and ramp-down phases.
 */
public class LoadProfileGraphPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private List<CSVThroughputEntry> entries;
    private int maxTPS = 1000;
    private long maxTimeMs = 300000; // Default 5 minutes
    
    // Colors for different phases
    private static final Color RAMP_UP_COLOR = new Color(52, 152, 219); // Blue
    private static final Color SUSTAINED_COLOR = new Color(46, 204, 113); // Green
    private static final Color RAMP_DOWN_COLOR = new Color(231, 76, 60); // Red
    private static final Color GRID_COLOR = new Color(200, 200, 200);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    
    // Padding and margins
    private static final int MARGIN_LEFT = 50;
    private static final int MARGIN_RIGHT = 20;
    private static final int MARGIN_TOP = 20;
    private static final int MARGIN_BOTTOM = 50;
    
    public LoadProfileGraphPanel() {
        this.entries = new ArrayList<>();
        setPreferredSize(new Dimension(600, 300));
        setBackground(Color.WHITE);
    }
    
    /**
     * Update the graph with new load profile data
     */
    public void updateGraph(List<CSVThroughputEntry> entries) {
        this.entries = new ArrayList<>(entries);
        
        if (!entries.isEmpty()) {
            // Calculate max TPS (with 10% buffer)
            this.maxTPS = (int) (entries.stream()
                .mapToInt(CSVThroughputEntry::getTargetTps)
                .max()
                .orElse(1000) * 1.1);
            
            // Round to nearest 100
            this.maxTPS = ((this.maxTPS + 99) / 100) * 100;
            
            // Calculate max time (with 10% buffer)
            long lastTimeMs = entries.get(entries.size() - 1).getTotalTimeMs();
            this.maxTimeMs = (long) (lastTimeMs * 1.1);
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (entries.isEmpty()) {
            // Draw placeholder text
            g2d.setColor(TEXT_COLOR);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String text = "No load profile loaded. Select a CSV/TXT/Excel file to display graph.";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, x, y);
            return;
        }
        
        // Draw grid and axes
        drawGrid(g2d);
        drawAxes(g2d);
        
        // Draw the load profile curve
        drawLoadProfileCurve(g2d);
        
        // Draw legend
        drawLegend(g2d);
    }
    
    /**
     * Draw background grid
     */
    private void drawGrid(Graphics2D g2d) {
        int graphWidth = getWidth() - MARGIN_LEFT - MARGIN_RIGHT;
        int graphHeight = getHeight() - MARGIN_TOP - MARGIN_BOTTOM;
        
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                1.0f, new float[]{5}, 0));
        
        // Vertical grid lines (for time)
        int timeGridStep = calculateTimeGridStep();
        for (int i = 0; i <= maxTimeMs; i += timeGridStep) {
            int x = MARGIN_LEFT + (int) ((long) i * graphWidth / maxTimeMs);
            if (x <= getWidth() - MARGIN_RIGHT) {
                g2d.drawLine(x, MARGIN_TOP, x, getHeight() - MARGIN_BOTTOM);
            }
        }
        
        // Horizontal grid lines (for TPS)
        int tpsGridStep = calculateTPSGridStep();
        for (int i = 0; i <= maxTPS; i += tpsGridStep) {
            int y = getHeight() - MARGIN_BOTTOM - (int) ((long) i * graphHeight / maxTPS);
            if (y >= MARGIN_TOP) {
                g2d.drawLine(MARGIN_LEFT, y, getWidth() - MARGIN_RIGHT, y);
            }
        }
    }
    
    /**
     * Draw X and Y axes
     */
    private void drawAxes(Graphics2D g2d) {
        int graphWidth = getWidth() - MARGIN_LEFT - MARGIN_RIGHT;
        int graphHeight = getHeight() - MARGIN_TOP - MARGIN_BOTTOM;
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // Y-axis
        g2d.drawLine(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT, getHeight() - MARGIN_BOTTOM);
        
        // X-axis
        g2d.drawLine(MARGIN_LEFT, getHeight() - MARGIN_BOTTOM, 
                     getWidth() - MARGIN_RIGHT, getHeight() - MARGIN_BOTTOM);
        
        // Y-axis label
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("TPS", 10, MARGIN_TOP);
        
        // X-axis label
        g2d.drawString("Time (mm:ss)", getWidth() - MARGIN_RIGHT - 60, getHeight() - 10);
        
        // Y-axis values
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int tpsGridStep = calculateTPSGridStep();
        for (int i = 0; i <= maxTPS; i += tpsGridStep) {
            int y = getHeight() - MARGIN_BOTTOM - (int) ((long) i * graphHeight / maxTPS);
            if (y >= MARGIN_TOP) {
                String label = String.valueOf(i);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(label, MARGIN_LEFT - fm.stringWidth(label) - 5, y + 3);
            }
        }
        
        // X-axis values (time)
        int timeGridStep = calculateTimeGridStep();
        for (int i = 0; i <= maxTimeMs; i += timeGridStep) {
            int x = MARGIN_LEFT + (int) ((long) i * graphWidth / maxTimeMs);
            if (x <= getWidth() - MARGIN_RIGHT) {
                int minutes = i / 60000;
                int seconds = (i % 60000) / 1000;
                String label = String.format("%02d:%02d", minutes, seconds);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(label, x - fm.stringWidth(label) / 2, 
                              getHeight() - MARGIN_BOTTOM + fm.getHeight() + 5);
            }
        }
    }
    
    /**
     * Draw the load profile curve connecting all data points
     */
    private void drawLoadProfileCurve(Graphics2D g2d) {
        int graphWidth = getWidth() - MARGIN_LEFT - MARGIN_RIGHT;
        int graphHeight = getHeight() - MARGIN_TOP - MARGIN_BOTTOM;
        
        if (entries.size() < 2) return;
        
        // Draw line path
        Path2D path = new Path2D.Double();
        
        for (int i = 0; i < entries.size(); i++) {
            CSVThroughputEntry entry = entries.get(i);
            
            int x = MARGIN_LEFT + (int) (entry.getTotalTimeMs() * graphWidth / maxTimeMs);
            int y = getHeight() - MARGIN_BOTTOM - (int) ((long) entry.getTargetTps() * graphHeight / maxTPS);
            
            // Clamp to graph boundaries
            x = Math.max(MARGIN_LEFT, Math.min(x, getWidth() - MARGIN_RIGHT));
            y = Math.max(MARGIN_TOP, Math.min(y, getHeight() - MARGIN_BOTTOM));
            
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        
        // Draw main line
        g2d.setColor(new Color(0, 100, 200));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.draw(path);
        
        // Draw data points
        for (CSVThroughputEntry entry : entries) {
            int x = MARGIN_LEFT + (int) (entry.getTotalTimeMs() * graphWidth / maxTimeMs);
            int y = getHeight() - MARGIN_BOTTOM - (int) ((long) entry.getTargetTps() * graphHeight / maxTPS);
            
            x = Math.max(MARGIN_LEFT, Math.min(x, getWidth() - MARGIN_RIGHT));
            y = Math.max(MARGIN_TOP, Math.min(y, getHeight() - MARGIN_BOTTOM));
            
            // Draw colored points based on phase
            Color phaseColor = getPhaseColor(entry);
            g2d.setColor(phaseColor);
            g2d.fillOval(x - 4, y - 4, 8, 8);
            
            // Draw circle outline
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(x - 4, y - 4, 8, 8);
        }
    }
    
    /**
     * Determine the phase color for a given entry
     */
    private Color getPhaseColor(CSVThroughputEntry entry) {
        if (entries.size() < 3) return RAMP_UP_COLOR;
        
        int firstTPS = entries.get(0).getTargetTps();
        int lastTPS = entries.get(entries.size() - 1).getTargetTps();
        int currentTPS = entry.getTargetTps();
        
        // Ramp-up: TPS increasing
        if (currentTPS > firstTPS && currentTPS < entries.stream()
                .mapToInt(CSVThroughputEntry::getTargetTps)
                .max()
                .orElse(currentTPS)) {
            return RAMP_UP_COLOR;
        }
        
        // Ramp-down: TPS decreasing (from peak)
        if (currentTPS < entries.stream()
                .mapToInt(CSVThroughputEntry::getTargetTps)
                .max()
                .orElse(currentTPS) && currentTPS > lastTPS) {
            return RAMP_DOWN_COLOR;
        }
        
        // Sustained: TPS at or near peak
        return SUSTAINED_COLOR;
    }
    
    /**
     * Draw legend to show phase colors
     */
    private void drawLegend(Graphics2D g2d) {
        int legendX = getWidth() - MARGIN_RIGHT - 150;
        int legendY = MARGIN_TOP + 10;
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Ramp-up
        g2d.setColor(RAMP_UP_COLOR);
        g2d.fillOval(legendX, legendY, 8, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(legendX, legendY, 8, 8);
        g2d.drawString("Ramp-up", legendX + 12, legendY + 8);
        
        // Sustained
        g2d.setColor(SUSTAINED_COLOR);
        g2d.fillOval(legendX, legendY + 20, 8, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(legendX, legendY + 20, 8, 8);
        g2d.drawString("Sustained", legendX + 12, legendY + 28);
        
        // Ramp-down
        g2d.setColor(RAMP_DOWN_COLOR);
        g2d.fillOval(legendX, legendY + 40, 8, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(legendX, legendY + 40, 8, 8);
        g2d.drawString("Ramp-down", legendX + 12, legendY + 48);
    }
    
    /**
     * Calculate appropriate grid step for time axis
     */
    private int calculateTimeGridStep() {
        if (maxTimeMs <= 30000) return 5000; // 5 seconds
        if (maxTimeMs <= 120000) return 15000; // 15 seconds
        if (maxTimeMs <= 300000) return 30000; // 30 seconds
        return 60000; // 1 minute
    }
    
    /**
     * Calculate appropriate grid step for TPS axis
     */
    private int calculateTPSGridStep() {
        if (maxTPS <= 100) return 10;
        if (maxTPS <= 500) return 50;
        if (maxTPS <= 1000) return 100;
        if (maxTPS <= 5000) return 500;
        return 1000;
    }
    
    /**
     * Clear the graph
     */
    public void clear() {
        this.entries = new ArrayList<>();
        repaint();
    }
}
