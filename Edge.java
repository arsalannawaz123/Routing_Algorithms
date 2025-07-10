import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * This class represents an edge in the graph connecting two vertices.
 * It visually draws a line between two nodes and shows the edge's weight.
 */
public class Edge {
    private final Vertex start;       // Starting vertex
    private final Vertex end;         // Ending vertex
    private final double weight;      // Weight of the edge

    private final Line line;          // Visual line between vertices
    private final Label weightLabel;  // Label showing the weight on the line
    private final Tooltip tooltip;    // Tooltip showing detailed info

    // Constructor that initializes edge components
    public Edge(Vertex start, Vertex end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;

        // Create the line connecting the two vertices
        this.line = new Line();
        this.line.setStroke(Color.web("#868e96"));  // Default gray color
        this.line.setStrokeWidth(1.8);
        this.line.setUserData(this); // Store reference for later use (e.g., removal)

        // Label to display the weight at the center of the edge
        this.weightLabel = new Label(String.format("%.1f", weight));
        this.weightLabel.setStyle(
                "-fx-font-weight: bold; -fx-text-fill: #495057;" +
                        "-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 2px 4px;"
        );
        this.weightLabel.setUserData(this); // Store reference for removal

        // Tooltip shown when mouse hovers on line or label
        this.tooltip = new Tooltip(String.format("Edge: %s — %s (%.1f)", start.getLabel(), end.getLabel(), weight));
        Tooltip.install(this.line, this.tooltip);
        Tooltip.install(this.weightLabel, this.tooltip);

        update(); // Set initial positions
    }

    // Updates the line and label positions based on vertex positions
    public void update() {
        double sx = start.getCircle().getCenterX();
        double sy = start.getCircle().getCenterY();
        double ex = end.getCircle().getCenterX();
        double ey = end.getCircle().getCenterY();

        line.setStartX(sx);
        line.setStartY(sy);
        line.setEndX(ex);
        line.setEndY(ey);

        // Position the weight label at the midpoint
        double midX = (sx + ex) / 2.0;
        double midY = (sy + ey) / 2.0;
        weightLabel.setLayoutX(midX - 15);
        weightLabel.setLayoutY(midY - 10);
    }

    // Getters for use in controller or animation logic
    public Vertex getStart() { return start; }
    public Vertex getEnd() { return end; }
    public double getWeight() { return weight; }
    public Line getLine() { return line; }
    public Label getWeightLabel() { return weightLabel; }

    // Checks if the edge connects two specific vertices (in any order)
    public boolean connects(Vertex v1, Vertex v2) {
        return (start == v1 && end == v2) || (start == v2 && end == v1);
    }

    // Checks if the edge includes a specific vertex
    public boolean hasVertex(Vertex v) {
        return start == v || end == v;
    }

    // Highlights the edge visually based on animation status
    public void highlight(String status) {
        switch (status) {
            case "evaluating":
                line.setStroke(Color.web("#f59f00")); // Orange
                line.setStrokeWidth(2.2);
                break;
            case "accepted":
                line.setStroke(Color.web("#2b8a3e")); // Green
                line.setStrokeWidth(2.5);
                break;
            case "discarded":
                line.setStroke(Color.web("#e03131")); // Red
                line.setStrokeWidth(1.5);
                break;
            default:
                resetStyle(); // Back to default style
        }
    }

    // Makes the edge look faded (used for non-MST edges)
    public void fade() {
        line.setStroke(Color.web("#dee2e6")); // Light gray
        line.setStrokeWidth(1.2);
        weightLabel.setOpacity(0.5); // Faded label
    }

    // Resets the edge appearance to default
    public void resetStyle() {
        line.setStroke(Color.web("#868e96")); // Default gray
        line.setStrokeWidth(1.8);
        weightLabel.setOpacity(1.0); // Fully visible
    }
}