package application;

import java.util.List;

import javafx.event.ActionEvent;
import de.tudresden.sumo.objects.SumoColor;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class InjectVehicleController {
	@FXML private ChoiceBox<String> routeChoiceBox;
    @FXML private ChoiceBox<String> modelChoiceBox;
    @FXML private ChoiceBox<String> colorChoiceBox;
    @FXML private TextField speedTextField;
    @FXML private Button injectButton;
    @FXML private Label errorLabel;
    
    private SumoManager sumoManager;
    private java.util.Map<String, SumoColor> colorMap = new java.util.LinkedHashMap<>();
    
    @FXML
    private void initialize() {
        // Creates pre-defined list of colors
        colorMap.put("Red", new SumoColor(255, 0, 0, 255));
        colorMap.put("Green", new SumoColor(0, 255, 0, 255));
        colorMap.put("Blue", new SumoColor(0, 0, 255, 255));
        colorMap.put("Yellow", new SumoColor(255, 255, 0, 255));
        colorMap.put("White", new SumoColor(255, 255, 255, 255));
        colorMap.put("Black", new SumoColor(0, 0, 0, 255));
        
        colorChoiceBox.getItems().addAll(colorMap.keySet());
        colorChoiceBox.setValue("White"); // Default
    }
    
    public void initData(SumoManager manager, List<String> routes, List<String> vehicleTypes) {
    	// Gets data from fxml files 
        this.sumoManager = manager;
        
        routeChoiceBox.getItems().addAll(routes);
        modelChoiceBox.getItems().addAll(vehicleTypes);

        if (!routes.isEmpty()) {
            routeChoiceBox.setValue(routes.get(0));
        }
        if (!vehicleTypes.isEmpty()) {
            modelChoiceBox.setValue(vehicleTypes.get(0));
        }
    }
    @FXML
    void handleInjectButton(ActionEvent event) {
    	// sets data as variables, and passes them to functions
        
        String route = routeChoiceBox.getValue();
        String model = modelChoiceBox.getValue();
        String speedStr = speedTextField.getText();
        String colorName = colorChoiceBox.getValue();

        double speed;
        double maxSpeed = 50.0;
        double minSpeed = 0.0;
        try { 
        	speed = Double.parseDouble(speedStr);
        	if (speed < minSpeed || speed > maxSpeed) {
        		showError("Speed must be between " + minSpeed + " and " + maxSpeed + ".");
        		return;
        		}
        } catch (NumberFormatException e) {
        showError("Speed must be a valid number.");
        return;
        }


        // forces all fields to be filled
        if (route == null || model == null || colorName == null) {
            showError("Please fill out all fields.");
            return;
        }

        try {
            SumoColor selectedColor = colorMap.get(colorName);

            // Calls function injectvehicle in Sumomanager with parameters acquired/input
            String newVehicleId = sumoManager.injectVehicle(route, model, speedStr);
            sumoManager.setVehicleColor(newVehicleId, selectedColor);
            
            Stage stage = (Stage) injectButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Injection Failed");
    		alert.setHeaderText("Could not inject the vehicle");
    		alert.setContentText("Error " + e.getMessage());
    		alert.showAndWait();
        }
    }
    
    private void showError(String message) {
    	// Helper function to output errors in this class
        System.out.println(message); 
        Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Input Error");
		alert.setHeaderText(message);
		alert.showAndWait();
    }
}

