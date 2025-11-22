package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InjectVehicleController {
	
	@FXML private ChoiceBox<String> routeChoiceBox;
	@FXML private ChoiceBox<String> modelChoiceBox;
	@FXML private ChoiceBox<String> colorChoiceBox;
	@FXML private ChoiceBox<String> batchChoiceBox;
	@FXML private TextField speedTextField;
	
	@FXML
	public void handleInject(ActionEvent event) {
		// Inject Vehicle with selected Parameters
	}
	@FXML
	public void handleBatch(ActionEvent event) {
		// Batch Inject Vehicles on selected edge/all edges (Stress Test)
	}
}
