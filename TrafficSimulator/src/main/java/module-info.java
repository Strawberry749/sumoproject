module TrafficSimulator {
	
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    
    requires traas;
    
    opens application to javafx.fxml, javafx.graphics;
    
    exports application;
}