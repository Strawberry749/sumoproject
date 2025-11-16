package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SceneController {
	// private variables to create Windows
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private SumoManager sumoManager = new SumoManager(); // Creates a new instance of SumoManager
	
	public void openInjectVehicle(ActionEvent event) throws IOException {
		// Creates Window InjectVehicle when corresponding button is pressed
		List<String> routes = sumoManager.getRouteIDs();
        List<String> vehicleTypes = sumoManager.getVehicleTypeIDs();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InjectVehicle.fxml"));
        root = loader.load();
        InjectVehicleController controller = loader.getController();
        controller.initData(sumoManager, routes, vehicleTypes);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage = new Stage();
		stage.setTitle("Inject Vehicle");
		stage.setScene(scene);
		stage.show();
	}
	
	public void openStatistics(ActionEvent event) throws IOException {
		// Creates Window Statistics when corresponding button is pressed
		root = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage = new Stage();
		stage.setTitle("Statistics");
		stage.setScene(scene);
		stage.show();
	}
	
	public void openMapView(ActionEvent event) throws IOException {
		// Creates Window MapView when corresponding button is pressed
		root = FXMLLoader.load(getClass().getResource("MapView.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage = new Stage();
		stage.setTitle("MapView");
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	public void handleStartSimulation(ActionEvent event) {
        //Connects Start Simulation button to this method in first_draft.fxml
        System.out.println("Starting simulation...");
        try {
        	boolean success = sumoManager.startSimulation();
        	if(success) {
        		sumoManager.startSimulationLoop();
        	} else {
        		showErrorAlert("Failed to start SUMO", "Check console for path errors");
        }
        } catch (Exception e) {
        	e.printStackTrace();
        	showErrorAlert("An error occured.", e.getMessage());
        }
    }
	private void showErrorAlert(String title, String content) {
		// Helper function to show error and error content
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.showAndWait();
	}
	@FXML
	public void handleStopSimulation(ActionEvent event) {
		// stops simulation and exits the GUI
        System.out.println("Stopping simulation...");
        sumoManager.stopSimulation();
        Platform.exit();
    }

}
