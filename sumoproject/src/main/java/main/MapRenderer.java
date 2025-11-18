package main;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.logging.Logger;

public class MapRenderer {
    private static final Logger LOGGER = Logger.getLogger(MapRenderer.class.getName());
    
    private Pane pane;
    private double scale = 1; // Smaller scale for better visualization
    private double offsetX = 400;
    private double offsetY = 300;

    public MapRenderer(Pane pane) {
        this.pane = pane;
    }

    public void renderNetwork(Document sumoDoc) {
        if (sumoDoc == null) {
            LOGGER.warning("SUMO document is null");
            return;
        }
        
        pane.getChildren().clear();
        
        // Render in order: edges first, then nodes on top
        renderEdges(sumoDoc);
        renderNodes(sumoDoc);
        renderConnections(sumoDoc);
        
        LOGGER.info("Rendering completed. Total children: " + pane.getChildren().size());
    }

    private void renderNodes(Document doc) {
        NodeList nodes = doc.getElementsByTagName("node");
        LOGGER.info("Found " + nodes.getLength() + " nodes");
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            try {
                String id = getAttributeSafe(node, "id", "node_" + i);
                double x = parseDoubleSafe(node, "x", 0);
                double y = parseDoubleSafe(node, "y", 0);
                String type = getAttributeSafe(node, "type", "unknown");
                
                Circle nodeCircle = createNodeCircle(x, y, type, id);
                pane.getChildren().add(nodeCircle);
                
            } catch (Exception e) {
                LOGGER.warning("Failed to render node " + i + ": " + e.getMessage());
            }
        }
    }

    private void renderEdges(Document doc) {
        NodeList edges = doc.getElementsByTagName("edge");
        LOGGER.info("Found " + edges.getLength() + " edges");
        
        for (int i = 0; i < edges.getLength(); i++) {
            Element edge = (Element) edges.item(i);
            try {
                String id = getAttributeSafe(edge, "id", "edge_" + i);
                renderLanes(edge, id);
            } catch (Exception e) {
                LOGGER.warning("Failed to render edge " + i + ": " + e.getMessage());
            }
        }
    }

    private void renderLanes(Element edge, String edgeId) {
        NodeList lanes = edge.getElementsByTagName("lane");
        LOGGER.info("Edge " + edgeId + " has " + lanes.getLength() + " lanes");
        
        for (int j = 0; j < lanes.getLength(); j++) {
            Element lane = (Element) lanes.item(j);
            try {
                String shape = getAttributeSafe(lane, "shape", "");
                double width = parseDoubleSafe(lane, "width", 3.0); // Default width
                
                if (!shape.isEmpty()) {
                    Polygon lanePolygon = createLanePolygon(shape, width, edgeId);
                    if (lanePolygon != null) {
                        pane.getChildren().add(lanePolygon);
                    }
                } else {
                    LOGGER.warning("Empty shape for lane in edge: " + edgeId);
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to render lane " + j + " in edge " + edgeId + ": " + e.getMessage());
            }
        }
    }

    private Polygon createLanePolygon(String shape, double width, String edgeId) {
        try {
            String[] points = shape.split(" ");
            if (points.length == 0) {
                LOGGER.warning("No points in shape: " + shape);
                return null;
            }
            
            Double[] coordinates = new Double[points.length * 2];
            
            for (int i = 0; i < points.length; i++) {
                String[] coords = points[i].split(",");
                if (coords.length != 2) {
                    LOGGER.warning("Invalid coordinate format: " + points[i]);
                    continue;
                }
                
                double x = parseDoubleSafe(coords[0], 0);
                double y = parseDoubleSafe(coords[1], 0);
                
                coordinates[i * 2] = x * scale + offsetX;
                coordinates[i * 2 + 1] = y * scale + offsetY;
            }
            
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(coordinates);
            polygon.setFill(Color.LIGHTGRAY);
            polygon.setStroke(Color.DARKGRAY);
            polygon.setStrokeWidth(0.5);
            polygon.setUserData(edgeId);
            
            // Add interactivity
            polygon.setOnMouseEntered(e -> {
                polygon.setStroke(Color.RED);
                polygon.setStrokeWidth(2);
            });
            
            polygon.setOnMouseExited(e -> {
                polygon.setStroke(Color.DARKGRAY);
                polygon.setStrokeWidth(0.5);
            });
            
            return polygon;
            
        } catch (Exception e) {
            LOGGER.warning("Failed to create lane polygon: " + e.getMessage());
            return null;
        }
    }

    private Circle createNodeCircle(double x, double y, String type, String id) {
        Circle circle = new Circle();
        circle.setCenterX(x * scale + offsetX);
        circle.setCenterY(y * scale + offsetY);
        circle.setRadius(2); // Smaller radius
        
        // Color coding based on node type
        switch (type) {
            case "traffic_light":
                circle.setFill(Color.RED);
                break;
            case "priority":
                circle.setFill(Color.ORANGE);
                break;
            default:
                circle.setFill(Color.BLUE);
        }
        
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.5);
        circle.setUserData(id);
        
        circle.setOnMouseClicked(e -> {
            System.out.println("Node clicked: " + circle.getUserData() + " at (" + x + "," + y + ")");
        });
        
        return circle;
    }

    private void renderConnections(Document doc) {
        // Optional: Implement connection rendering later
        NodeList connections = doc.getElementsByTagName("connection");
        LOGGER.info("Found " + connections.getLength() + " connections");
        // Implementation for connections can be added here
    }

    // Safe parsing methods
    private String getAttributeSafe(Element element, String attribute, String defaultValue) {
        String value = element.getAttribute(attribute);
        return value.isEmpty() ? defaultValue : value;
    }
    
    private double parseDoubleSafe(Element element, String attribute, double defaultValue) {
        try {
            String value = element.getAttribute(attribute);
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("Failed to parse " + attribute + " as double, using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    private double parseDoubleSafe(String value, double defaultValue) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("Failed to parse string as double: '" + value + "', using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    // Utility methods for zoom and pan
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public void setOffset(double x, double y) {
        this.offsetX = x;
        this.offsetY = y;
    }
}