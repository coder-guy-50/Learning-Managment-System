package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    public Hyperlink link;
    public Stage stage;
    public TextField emailField;
    public PasswordField passwordField;

    public static currentUser currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        link.setOnAction(e->{
            Node source = (Node) e.getSource();
            stage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SignUp.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void submit() {
        if (emailField.getText().isEmpty()){
            ControllerMethods.showErrorAlert("Email cannot be empty");
        } else if (!ControllerMethods.isValidEmail(emailField.getText())) {
            ControllerMethods.showErrorAlert("Please enter a valid email format");
        } else if (passwordField.getText().isEmpty()) {
            ControllerMethods.showErrorAlert("Password cannot be empty");
        } else if (!emailField.getText().isEmpty()  &&  ControllerMethods.isValidEmail(emailField.getText())  &&  !passwordField.getText().isEmpty()) {
            try(Connection connection = ControllerMethods.establishConnection();
                PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "SELECT * FROM Users WHERE Email = ?;")){
                preparedStatement.setString(1, emailField.getText());
                try(ResultSet resultSet = ControllerMethods.getData(preparedStatement)){
                    if(resultSet.next()){
                        if (resultSet.getString("Password").equals(passwordField.getText())){
                            currentUser = new currentUser(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                            Stage currentStage = (Stage) emailField.getScene().getWindow();
                            currentStage.close();

                            Stage newStage = new Stage();
                            ControllerMethods.setHomeScene(newStage);
                            newStage.setMinWidth(1400);
                            newStage.setMinHeight(900);
                            newStage.show();

                            ControllerMethods.showInfoAlert("Logged in successfully");
                        } else{
                            ControllerMethods.showErrorAlert("Incorrect Password");
                        }
                    } else {
                        ControllerMethods.showErrorAlert("No account associated with this email");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            ControllerMethods.showErrorAlert("Could not log in");
        }
    }

}
