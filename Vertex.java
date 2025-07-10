import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Vertex {

    private String label;              // The name/label of the vertex (e.g., A, B, etc.)
    private Circle circle;            // Circle shape representing the vertex
    private Label labelNode;          // Label shown near the circle
    private Consumer<Vertex> onSelected; // Callback when vertex is clicked
    private Consumer<Vertex> onDragged;  // Callback when vertex is dragged
    private Tooltip tooltip;          // Tooltip for additional info

    private static final double BASE_RADIUS = 15.0; // Radius of the circle
    private double dragStartX, dragStartY;          // Offset during dragging

    /**
     * Create a vertex at (x, y) with the given label.
     */
    public Vertex(String label, double x, double y) {
        this.label = label;

        // Create the circle (node shape)
        this.circle = new Circle(x, y, BASE_RADIUS);
        this.circle.setFill(Color.web("#000000"));      // black fill
        this.circle.setStroke(Color.web("#ffffff"));    // white border
        this.circle.setStrokeWidth(1.5);

        // Create the label
        this.labelNode = new Label(label);
        this.labelNode.setLayoutX(x - 6);
        this.labelNode.setLayoutY(y - 8);
        labelNode.setMouseTransparent(true);            // let clicks pass through
        this.labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;"); // white text

        // Create tooltip
        this.tooltip = new Tooltip("Node " + label);
        Tooltip.install(this.circle, this.tooltip);
        Tooltip.install(this.labelNode, this.tooltip);

        // Set up mouse events (click, drag, hover)
        setupMouseInteractions();
    }

    /**
     * Set up mouse click, drag, and hover interactions.
     */
    private void setupMouseInteractions() {
        // Handle clicking on the vertex
        circle.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && onSelected != null) {
                onSelected.accept(this);
            }
        });

        // Start dragging
        circle.setOnMousePressed(event -> {
            dragStartX = event.getSceneX() - circle.getCenterX();
            dragStartY = event.getSceneY() - circle.getCenterY();
        });

        // Handle dragging
        circle.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - dragStartX;
            double newY = event.getSceneY() - dragStartY;

            // Keep within pane bounds
            newX = Math.max(BASE_RADIUS, Math.min(graphPaneWidth() - BASE_RADIUS, newX));
            newY = Math.max(BASE_RADIUS, Math.min(graphPaneHeight() - BASE_RADIUS, newY));

            // Update position
            circle.setCenterX(newX);
            circle.setCenterY(newY);
            labelNode.setLayoutX(newX - 6);
            labelNode.setLayoutY(newY - 8);

            // Notify if needed
            if (onDragged != null) onDragged.accept(this);
        });

        // Hover effects: slightly enlarge & change color
        circle.setOnMouseEntered(event -> {
            circle.setRadius(BASE_RADIUS * 1.1);
            circle.setFill(Color.web("#1a1a1a")); // lighter black
            tooltip.setText("Node " + label + "\nDrag to move");
        });

        // When mouse leaves: restore
        circle.setOnMouseExited(event -> {
            circle.setRadius(BASE_RADIUS);
            circle.setFill(Color.web("#000000")); // back to black
            tooltip.setText("Node " + label);
        });
    }

    /**
     * Get width of the parent pane to keep vertex inside.
     */
    private double graphPaneWidth() {
        return circle.getParent().getLayoutBounds().getWidth();
    }

    /**
     * Get height of the parent pane to keep vertex inside.
     */
    private double graphPaneHeight() {
        return circle.getParent().getLayoutBounds().getHeight();
    }

    /**
     * Mark vertex as visited (e.g., during MST).
     */
    public void markVisited() {
        this.circle.setStroke(Color.web("#2b8a3e"));  // green border
        this.circle.setStrokeWidth(2.0);
    }

    /**
     * Mark vertex as active (e.g., being evaluated).
     */
    public void markActive() {
        this.circle.setStroke(Color.web("#e67700"));  // orange border
        this.circle.setStrokeWidth(2.0);
    }

    /**
     * Mark this vertex as the source (starting node).
     */
    public void markAsSource() {
        this.circle.setFill(Color.web("#2b8a3e"));    // green fill
        this.circle.setStroke(Color.web("#1b5e20"));  // darker green border
        this.circle.setStrokeWidth(2.5);
        this.labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
    }

    /**
     * Reset style to default.
     */
    public void resetStyle() {
        this.circle.setStroke(Color.web("#ffffff"));  // white border
        this.circle.setStrokeWidth(1.5);
    }

    /**
     * Highlight or unhighlight the vertex (e.g., disconnected).
     */
    public void highlight(boolean highlight) {
        if (highlight) {
            this.circle.setFill(Color.web("#fab005")); // yellow
        } else {
            this.circle.setFill(Color.web("#000000")); // back to black
        }
    }

    // Getters for various parts
    public String getLabel() {
        return label;
    }

    public Circle getCircle() {
        return circle;
    }

    public Label getLabelNode() {
        return labelNode;
    }

    // Set callbacks
    public void setOnSelected(Consumer<Vertex> listener) {
        onSelected = listener;
    }

    public void setOnDragged(Consumer<Vertex> listener) {
        onDragged = listener;
    }
}
