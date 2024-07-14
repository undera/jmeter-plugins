package kg.apc.charting;

public class GraphRowDrawing {
    public static final int MARKER_SIZE_NONE = 0;
    public static final int MARKER_SIZE_SMALL = 2;
    public static final int MARKER_SIZE_BIG = 4;
    public static final float LINE_THICKNESS_BIG = 3.0f;

    private boolean drawLine = false;
    private boolean drawThickLines = false;
    private boolean drawBar = false;
    private boolean drawSpline = false;
    private int markerSize = MARKER_SIZE_NONE;

    public boolean isDrawThickLines() {
        return drawThickLines;
    }

    public void setDrawThickLines(boolean drawThickLines) {
        this.drawThickLines = drawThickLines;
    }

    public boolean isDrawLine() {
        return drawLine;
    }

    public void setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
    }

    public int getMarkerSize() {
        return markerSize;
    }

    public void setMarkerSize(int markerSize) {
        this.markerSize = markerSize;
    }

    public boolean isDrawBar() {
        return drawBar;
    }

    public void setDrawBar(boolean drawBar) {
        this.drawBar = drawBar;
    }

    public boolean isDrawSpline() {
        return drawSpline;
    }

    public void setDrawSpline(boolean drawSpline) {
        this.drawSpline = drawSpline;
    }
}
