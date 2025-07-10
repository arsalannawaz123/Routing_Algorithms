import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Set;
import java.util.HashSet;

public class AnimationManager {
    private GraphController controller; // To update the UI and log progress
    private Timeline timeline;         // To manage animation steps
    private int delay = 700;           // Delay between steps in milliseconds

    public AnimationManager(GraphController controller) {
        this.controller = controller;
    }

    // Allow changing the delay speed
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Animate the Prim's Minimum Spanning Tree (MST) algorithm
     * @param sourceLabel label of the starting vertex
     */
    public void animateMST(Map<String, Vertex> vertices, List<Edge> edges, String sourceLabel) {
        // Run Prim’s algorithm and get the list of edges that form the MST
        List<Edge> mstEdges = PrimMST.runPrim(vertices, edges, sourceLabel);

        // Reset styles of all edges and vertices before starting the animation
        edges.forEach(Edge::resetStyle);
        vertices.values().forEach(Vertex::resetStyle);

        this.timeline = new Timeline(); // Create a new animation timeline
        final double[] totalCost = {0.0}; // To keep track of MST total cost
        Set<String> visited = new HashSet<>(); // To keep track of visited vertices
        visited.add(sourceLabel);
        vertices.get(sourceLabel).markVisited(); // Mark the starting vertex as visited

        int stepIndex = 0;

        // Loop through all edges to animate their evaluation
        for (Edge edge : edges) {
            Vertex u = edge.getStart();
            Vertex v = edge.getEnd();

            // First keyframe: show that we are evaluating this edge
            KeyFrame evaluateFrame = new KeyFrame(Duration.millis(delay * (stepIndex * 2 + 1)), e -> {
                edge.highlight("evaluating");   // Highlight the edge as being evaluated
                u.markActive();                // Mark both vertices as active
                v.markActive();
                controller.log("Evaluating: " + u.getLabel() + " → " + v.getLabel() + " (" + edge.getWeight() + ")");
            });

            // Second keyframe: decide if the edge is part of the MST or not
            KeyFrame decisionFrame = new KeyFrame(Duration.millis(delay * (stepIndex * 2 + 2)), e -> {
                u.resetStyle(); // Reset vertices to default style
                v.resetStyle();

                if (mstEdges.contains(edge)) {
                    // If the edge is part of MST, accept it
                    edge.highlight("accepted");
                    totalCost[0] += edge.getWeight();
                    controller.updateTotalCost(totalCost[0]);

                    // Mark both vertices as visited if they weren't already
                    if (!visited.contains(u.getLabel())) {
                        u.markVisited();
                        visited.add(u.getLabel());
                    }
                    if (!visited.contains(v.getLabel())) {
                        v.markVisited();
                        visited.add(v.getLabel());
                    }

                    controller.log("Added to MST: " + u.getLabel() + " → " + v.getLabel());
                } else {
                    // Otherwise, discard it
                    edge.highlight("discarded");
                    controller.log("Discarded edge");
                }
            });

            // Add both keyframes to the timeline
            this.timeline.getKeyFrames().addAll(evaluateFrame, decisionFrame);
            stepIndex++;
        }

        // Final keyframe: clean up and show completion message
        KeyFrame doneFrame = new KeyFrame(Duration.millis(delay * (stepIndex * 2 + 1)), e -> {
            controller.removeEdgesOutsideMST(mstEdges); // Remove edges that are not in the MST
            controller.log("MST completed. Total cost: " + totalCost[0]);
        });

        // Add the final keyframe to the timeline
        this.timeline.getKeyFrames().add(doneFrame);

        // Start playing the animation
        this.timeline.play();
    }
}
