package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ControllerMethods {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        return email.matches(emailRegex);
    }

    public static Connection establishConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "username", "password");
    }

    public static PreparedStatement newPreparedStatement(Connection connection, String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }

    public static ResultSet getData(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    public static int updateData(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public static void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("Please try again");
        alert.show();
    }
    public static void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);
        alert.setContentText("Please continue");
        alert.show();
    }

    public static void setHomeScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMethods.class.getResource("Home.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
    }
    public static void setQuizzesScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMethods.class.getResource("Quizzes.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    public static void setAnalyticsScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMethods.class.getResource("Analytics.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    public static void setMyProfileScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerMethods.class.getResource("MyProfile.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
