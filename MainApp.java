// Import necessary JavaFX classes
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    private GraphController graphController; // Main logic controller for graph actions

    @Override
    public void start(Stage primaryStage) {
        // Main area where nodes and edges are drawn
        Pane graphPane = new Pane();
        graphPane.setPrefSize(800, 600);
        graphPane.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef;");
        // Console area to show log messages
        TextArea consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.setPrefRowCount(10);
        consoleArea.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #212529; " +
                "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-border-color: #dee2e6;");
        // Buttons to control graph actions
        Button addNodeBtn = createButton("Add Node", "#090620");
        Button addEdgeBtn = createButton("Add Edge", "#090620");
        Button removeNodeBtn = createButton("Remove Node", "#090620");
        Button removeEdgeBtn = createButton("Remove Edge", "#090620");  // New feature
        Button clearGraphBtn = createButton("Clear Graph", "#090620");
        Button runPrimBtn = createButton("Run Prim's", "#090620");
        // Dropdown to select the source node for Prim’s algorithm
        ComboBox<String> sourceComboBox = new ComboBox<>();
        sourceComboBox.setPromptText("Source Node");
        sourceComboBox.setPrefWidth(120);
        // Slider to control the animation speed
        Slider speedSlider = new Slider(100, 1500, 700);
        speedSlider.setShowTickLabels(true);
        speedSlider.setPrefWidth(150);
        Label speedLabel = new Label("Speed:");
        // Label to show total cost of MST
        Label totalCostLabel = new Label("Total Cost: 0.0");
        totalCostLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #073b4c;");
        // Top panel holds buttons and controls
        HBox topPanel = new HBox(15);
        topPanel.setPadding(new Insets(12));
        topPanel.setStyle("-fx-background-color: #ededf4; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        topPanel.setAlignment(Pos.CENTER_LEFT);
        // Group of action buttons
        HBox buttonGroup = new HBox(10, addNodeBtn, addEdgeBtn, removeNodeBtn, removeEdgeBtn, clearGraphBtn, runPrimBtn);
        buttonGroup.setAlignment(Pos.CENTER_LEFT);
        // Group of control options (source node, speed, cost)
        HBox controlGroup = new HBox(15);
        controlGroup.getChildren().addAll(
                createControlBox("Source:", sourceComboBox),
                createControlBox(speedLabel, speedSlider),
                createControlBox("", totalCostLabel)
        );
        controlGroup.setAlignment(Pos.CENTER_LEFT);
        // Add buttons and controls to the top
        topPanel.getChildren().addAll(buttonGroup, controlGroup);
        HBox.setMargin(controlGroup, new Insets(0, 0, 0, 30));
        // Right panel for the execution log
        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(15));
        rightPanel.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ededf4; -fx-border-width: 0 0 0 1;");
        rightPanel.getChildren().addAll(new Label("Execution Log:"), consoleArea);
        VBox.setVgrow(consoleArea, javafx.scene.layout.Priority.ALWAYS);
        // Main layout using BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topPanel);
        root.setCenter(graphPane);
        root.setRight(rightPanel);
        // Create the controller to manage graph logic and actions
        this.graphController = new GraphController(graphPane, consoleArea, sourceComboBox, totalCostLabel, runPrimBtn);
        // Set actions for buttons
        addNodeBtn.setOnAction(e -> graphController.enableAddVertexOnClick());
        addEdgeBtn.setOnAction(e -> graphController.enableEdgeMode());
        removeNodeBtn.setOnAction(e -> graphController.enableRemoveNodeMode());
        removeEdgeBtn.setOnAction(e -> graphController.enableRemoveEdgeMode());
        clearGraphBtn.setOnAction(e -> graphController.clearGraph());
        runPrimBtn.setOnAction(e -> graphController.runPrimsMST());
        // Connect speed slider with animation delay
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int delay = newVal.intValue();
            graphController.setAnimationDelay(delay);
        });
        // Set up and show the window
        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setTitle("Prim's Minimum Spanning Tree Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
        // Enable add node mode by default
        graphController.enableAddVertexOnClick();
    }
    // Helper function to create a styled button with hover effect
    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-weight: 700; -fx-padding: 8px 15px; -fx-font-size: 10px;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", -20%); " +
                "-fx-text-fill: white; -fx-font-weight: 700; " +
                "-fx-padding: 8px 15px; -fx-font-size: 10px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; -fx-font-weight: 700; " +
                "-fx-padding: 8px 15px; -fx-font-size: 10px;"));
        return btn;
    }
    // Helper to group label + control vertically
    private VBox createControlBox(String label, Control control) {
        return createControlBox(new Label(label), control);
    }
    private VBox createControlBox(Node label, Control control) {
        VBox box = new VBox(5);
        box.getChildren().addAll(label, control);
        return box;
    }
    // Launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}