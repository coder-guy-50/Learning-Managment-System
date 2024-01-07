module com.example.librarymanagmentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.librarymanagmentsystem to javafx.fxml;
    exports com.example.librarymanagmentsystem;
}