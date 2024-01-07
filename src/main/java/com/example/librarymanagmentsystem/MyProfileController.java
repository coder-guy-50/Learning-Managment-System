package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyProfileController implements Initializable {
    public Label quizzesLabel;
    public Label myProfileLabel;
    public Label logOutLabel;
    public Label analyticsLabel;
    public Label homeLabel;
    public Label firstName;
    public Label lastName;
    public Label email;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(LogInController.currentUser != null) {
            firstName.setText(LogInController.currentUser.getFirstName());
            lastName.setText(LogInController.currentUser.getLastName());
            email.setText(LogInController.currentUser.getEmail());
        } else {
            firstName.setText(SignUpController.currentUser.getFirstName());
            lastName.setText(SignUpController.currentUser.getLastName());
            email.setText(SignUpController.currentUser.getEmail());
        }
    }
    public void setHomeScene() throws IOException {
        Stage stage = (Stage) homeLabel.getScene().getWindow();
        ControllerMethods.setHomeScene(stage);
    }

    public void setQuizzesScene() throws IOException {
        Stage stage = (Stage) quizzesLabel.getScene().getWindow();
        ControllerMethods.setQuizzesScene(stage);
    }
    public void setProfileScene() throws IOException {
        Stage stage = (Stage) myProfileLabel.getScene().getWindow();
        ControllerMethods.setMyProfileScene(stage);
    }
    public void setAnalyticsScene() throws IOException {
        Stage stage = (Stage) analyticsLabel.getScene().getWindow();
        ControllerMethods.setAnalyticsScene(stage);
    }
    public void logOut(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to log out?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            try{
                Stage currentStage = (Stage) logOutLabel.getScene().getWindow();
                currentStage.close();

                if (SignUpController.currentUser != null){
                    SignUpController.currentUser = null;
                } else{
                    LogInController.currentUser = null;
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setMinWidth(480);
                newStage.setMinHeight(650);
                newStage.setTitle("Log In");
                newStage.setScene(scene);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            alert.close();
        }
    }



}