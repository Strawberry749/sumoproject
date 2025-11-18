package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.logging.Logger;

public class SumoPaneViewer extends Application {
    private static final Logger LOGGER = Logger.getLogger(SumoPaneViewer.class.getName());
    
    private Pane mapPane;
    private MapRenderer renderer;
    
    @Override
    public void start(Stage primaryStage) {
        mapPane = new Pane();
        renderer = new MapRenderer(mapPane);
        
        // Wrap in ScrollPane for navigation
        ScrollPane scrollPane = new ScrollPane(mapPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        BorderPane root = new BorderPane(scrollPane);
        Scene scene = new Scene(root, 1200, 800);
        
        primaryStage.setTitle("SUMO Map Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Try to load the SUMO network
        loadSumoNetwork("/home/user/sumoproject/sumoproject/src/main/config/network.net.xml");
    }
    
    private void loadSumoNetwork(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LOGGER.severe("File not found: " + filePath);
                LOGGER.info("Current directory: " + new File(".").getAbsolutePath());
                return;
            }
            
            LOGGER.info("Loading SUMO network from: " + file.getAbsolutePath());
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(null); // Disable DTD validation errors
            
            var doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            
            LOGGER.info("XML parsed successfully. Root: " + doc.getDocumentElement().getNodeName());
            
            renderer.renderNetwork(doc);
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load SUMO network: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
