module de.frauas {
    //needed for JavaFX 
    requires javafx.controls;
    requires javafx.fxml;
    //needed for traas implementation?
    requires traas;
    //needed for xml parsing 
    requires java.xml;

    requires java.logging;
    
    opens de.frauas to javafx.fxml;
    exports de.frauas;
    exports main;
}
