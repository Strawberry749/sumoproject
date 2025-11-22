module main {
    //needed for JavaFX 
    requires javafx.controls;
    requires javafx.fxml;
    //needed for traas implementation?
    requires traas;
    //needed for xml parsing 
    requires java.xml;

    requires java.logging;
    
    exports main to javafx.graphics;
    opens main to javafx.fxml;

}
