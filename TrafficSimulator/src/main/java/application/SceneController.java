package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import de.tudresden.sumo.cmd.Vehicle;

public class SceneController {
	private boolean started;
	private boolean running;
	@FXML private Label VechileCountLabel; //getIDCount();
	@FXML private Label StatsEdgeLabel; //get Edge chosen from dropdown menu -> display stats of that edge density on graph
	@FXML private Label CongestionHotspotsLabel; //get all edges that are congestion hotspots(# of Vehicles exceeds lane capacity)
	
	@FXML
	public void handleStart(ActionEvent event) {
		// Starts Simulation
	}
	@FXML
	public void handleQuit(ActionEvent event) {
		// Closes Simulation
		System.exit(0);
	}
	@FXML
	public void handleInject(ActionEvent event) {
		// opens InjectVehicle Window
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/InjectVehicle.fxml"));
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.setTitle("Inject Vehicle");
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		  }
		
	}
	@FXML
	public void handleATLC(ActionEvent event) {
		// opens Advanced Traffic Control Window
	}
	@FXML
	public void handleExportCSV(ActionEvent event) {
		// Exports Data as a CSV
	}
	@FXML
	public void handleExportPDF(ActionEvent event) {
		// Exports Data as a PDF
	}
	@FXML
	public void handleRestart(ActionEvent event) {
		// Restarts Simulation
	}
	@FXML
	public void handleHowTo(ActionEvent event) {
		// opens User Handbook
	}
	@FXML
	public void handleAbout(ActionEvent event) {
		// opens About window
	}
	@FXML
	public void handlePause(ActionEvent event) {
		if(running) {
			// Pause Simulation
		} 
		else if(started) {
			// unpause simulation
		}
		else {
			// do nothing
		}
	}
	@FXML
	public void handleStep(ActionEvent event) {
		// Advances 1 step in the simulation
	}
	
}
