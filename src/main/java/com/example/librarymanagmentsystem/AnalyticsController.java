package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AnalyticsController implements Initializable {
    public Label homeLabel;
    public Label quizzesLabel;
    public Label analyticsLabel;
    public Label myProfileLabel;
    public Label logOutLabel;
    public BarChart<String, Number> barChart;
    public VBox vbox;
    public BorderPane borderPane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.getCenter().setStyle("-fx-background-color:  #b3e35f");
        vbox.setAlignment(Pos.CENTER);
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #190640;");
        barChart.setStyle("-fx-background-color:  #b3e35f");
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        try(Connection connection = ControllerMethods.establishConnection();
            PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "SELECT u.Id, q.Quiz_Name, a.correct_answers, a.result_percentage FROM Attempts a LEFT JOIN Users u ON a.User_id = u.Id LEFT JOIN Quizzes q ON a.Quizz_id = q.Quizz_Id WHERE u.Id = ?;")){
            if (LogInController.currentUser != null) {
                preparedStatement.setInt(1, LogInController.currentUser.getUserId());
            } else{
                preparedStatement.setInt(1, SignUpController.currentUser.getUserId());
            }
            ResultSet resultSet = ControllerMethods.getData(preparedStatement);

            while (resultSet.next()){
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.getData().add(new XYChart.Data<>(resultSet.getString(2), resultSet.getInt(4)));
                barChart.getData().add(series);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
