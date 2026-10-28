module com.example.repetich {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.repetich to javafx.fxml;
    exports com.example.repetich;
}