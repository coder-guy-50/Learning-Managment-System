package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    public Hyperlink link;
    public Stage stage;
    public TextField emailField;
    public PasswordField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;

    public static currentUser currentUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        link.setOnAction(e -> {
            Node source = (Node) e.getSource();
            stage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void register() {
        if (firstNameField.getText().isEmpty()) {
            ControllerMethods.showErrorAlert("First Name cannot be empty");
        } else if (lastNameField.getText().isEmpty()) {
            ControllerMethods.showErrorAlert("Last Name cannot be empty");
        } else if (emailField.getText().isEmpty()) {
            ControllerMethods.showErrorAlert("Email cannot be empty");
        } else if (!ControllerMethods.isValidEmail(emailField.getText())) {
            ControllerMethods.showErrorAlert("Please enter a valid email format");
        } else if (passwordField.getText().isEmpty()) {
            ControllerMethods.showErrorAlert("Password cannot be empty");
        } else if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty() && !emailField.getText().isEmpty() && ControllerMethods.isValidEmail(emailField.getText()) && !passwordField.getText().isEmpty()) {
            try (Connection connection = ControllerMethods.establishConnection();
                 PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "INSERT INTO Users(FirstName, LastName, Email, Password, SignUpDate) VALUES(?, ?, ?, ?, now());")) {

                preparedStatement.setString(1, firstNameField.getText());
                preparedStatement.setString(2, lastNameField.getText());
                preparedStatement.setString(3, emailField.getText());
                preparedStatement.setString(4, passwordField.getText());

                int data = ControllerMethods.updateData(preparedStatement);

                if(data>0){
                    Stage currentStage = (Stage) firstNameField.getScene().getWindow();
                    currentStage.close();

                    currentUser = new currentUser(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());

                    Stage newStage = new Stage();
                    ControllerMethods.setHomeScene(newStage);
                    newStage.setMinWidth(1400);
                    newStage.setMinHeight(900);
                    newStage.show();

                    ControllerMethods.showInfoAlert("Your account was created successfully");
                } else {
                    ControllerMethods.showErrorAlert("Something went wrong...");
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}