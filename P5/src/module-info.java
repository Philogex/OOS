module P5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ui to javafx.fxml;
    opens bank;
    opens bank.exceptions;
    exports bank.exceptions;
    exports bank;
    exports ui;
}