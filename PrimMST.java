import java.util.*;

public class PrimMST {

    public static boolean isConnected(Map<String, Vertex> vertices, List<Edge> edges) {
        if (vertices.isEmpty()) return true;

        Set<String> visited = new HashSet<>();
        String startNode = vertices.keySet().iterator().next();
        dfs(vertices.get(startNode), edges, visited);

        return visited.size() == vertices.size();
    }

    private static void dfs(Vertex current, List<Edge> edges, Set<String> visited) {
        if (current == null || visited.contains(current.getLabel())) return;

        visited.add(current.getLabel());

        for (Edge edge : edges) {
            Vertex neighbor = null;

            if (edge.getStart().equals(current)) {
                neighbor = edge.getEnd();
            } else if (edge.getEnd().equals(current)) {
                neighbor = edge.getStart();
            }

            if (neighbor != null && !visited.contains(neighbor.getLabel())) {
                dfs(neighbor, edges, visited);
            }
        }
    }

    public static List<Edge> runPrim(Map<String, Vertex> vertices, List<Edge> edges, String sourceLabel) {
        if (!vertices.containsKey(sourceLabel)) {
            throw new IllegalArgumentException("Invalid source node: " + sourceLabel);
        }

        if (!isConnected(vertices, edges)) {
            throw new IllegalArgumentException("Graph is disconnected. MST cannot be run.");
        }

        // Clear any previous MST state (if stored externally, handle it there too)

        List<Edge> mstEdges = new ArrayList<>();
        Set<String> inMST = new HashSet<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        // Always start fresh from the new source
        inMST.add(sourceLabel);

        for (Edge edge : edges) {
            if (edge.getStart().getLabel().equals(sourceLabel) ||
                    edge.getEnd().getLabel().equals(sourceLabel)) {
                edgeQueue.add(edge);
            }
        }

        while (!edgeQueue.isEmpty() && inMST.size() < vertices.size()) {
            Edge edge = edgeQueue.poll();

            String u = edge.getStart().getLabel();
            String v = edge.getEnd().getLabel();

            String next = null;

            if (inMST.contains(u) && !inMST.contains(v)) {
                next = v;
            } else if (inMST.contains(v) && !inMST.contains(u)) {
                next = u;
            } else {
                continue;
            }

            mstEdges.add(edge);
            inMST.add(next);

            for (Edge e : edges) {
                String start = e.getStart().getLabel();
                String end = e.getEnd().getLabel();

                if ((start.equals(next) && !inMST.contains(end)) ||
                        (end.equals(next) && !inMST.contains(start))) {
                    edgeQueue.add(e);
                }
            }
        }

        return mstEdges;
    }
}