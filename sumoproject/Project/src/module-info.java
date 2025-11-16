module Project { 
	
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	requires traas; 
	
	opens application to javafx.fxml;
	
	exports application;
}