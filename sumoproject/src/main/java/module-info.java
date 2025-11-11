module de.frauas {
    requires javafx.controls;
    requires javafx.fxml;
    requires traas;
    opens de.frauas to javafx.fxml;
    exports de.frauas;
}