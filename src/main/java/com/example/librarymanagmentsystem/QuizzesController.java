package com.example.librarymanagmentsystem;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class QuizzesController implements Initializable {
    public Label quizzesLabel;
    public Label myProfileLabel;
    public Label logOutLabel;
    public Label analyticsLabel;
    public Label homeLabel;
    public BorderPane borderPane;
    public GridPane gridPane;

    public int currentQuestion = 0;
    public int correctAnswers = 0;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int numberOfQuizzes = 0;
        ArrayList<String> quizNames = new ArrayList<>();
        ArrayList<InputStream> images = new ArrayList<>();
        ArrayList<Integer> quizLengths = new ArrayList<>();

        try(Connection connection = ControllerMethods.establishConnection();
            PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "SELECT COUNT(*) FROM Quizzes;");
            PreparedStatement secondPreparedStatement = ControllerMethods.newPreparedStatement(connection, "SELECT Quiz_Name, Image, Total_Questions from Quizzes;")){
            ResultSet resultSet = ControllerMethods.getData(preparedStatement);
            if (resultSet.next()){
                numberOfQuizzes = resultSet.getInt(1);
            }

            ResultSet secondResultSet = ControllerMethods.getData(secondPreparedStatement);
            while (secondResultSet.next()){
                quizNames.add(secondResultSet.getString(1));
                images.add(secondResultSet.getBinaryStream("Image"));
                quizLengths.add(secondResultSet.getInt("Total_Questions"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:  #b3e35f");
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        gridPane.setPrefHeight(521);
        gridPane.setPrefWidth(400);
        gridPane.setAlignment(Pos.CENTER);

        int rows = (int) Math.ceil((double) numberOfQuizzes / 3);
        int count = 0;


        for(int i=0; i<rows; i++){
            for (int j = 0; j < 3; j++) {
                if (count < quizNames.size()){
                    VBox vBox = new VBox();
                    vBox.setStyle("-fx-background-color: white;");
                    vBox.setCursor(Cursor.HAND);

                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(images.get(count)));
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(300);
                    int finalCount = count;
                    imageView.setOnMouseClicked(mouseEvent -> startQuiz(quizNames.get(finalCount), quizLengths.get(finalCount)));
                    vBox.getChildren().add(imageView);

                    Label label = new Label(quizNames.get(count));
                    label.setFont(new Font(25));
                    label.setPrefSize(300, 75);
                    label.setStyle("-fx-background-color:  #190640; -fx-text-fill: #a4e82e;");
                    label.setAlignment(Pos.CENTER);
                    label.setOnMouseClicked(mouseEvent -> startQuiz(quizNames.get(finalCount), quizLengths.get(finalCount)));
                    vBox.getChildren().add(label);

                    gridPane.add(vBox, j, i);
                    count++;
                }
            }
        }
        borderPane.setCenter(gridPane);
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

    public void showResults(int result, int quizLength){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #a4e82e");
        vBox.setSpacing(200);


        Label label = new Label("Your score is: " + result + "/" + quizLength);
        label.setFont(Font.font("System", FontWeight.BOLD, 50));

        Button back = new Button("Back To Other Quizzes");
        back.setPrefHeight(40);
        back.setPrefWidth(200);
        back.setTextFill(Color.YELLOW);
        back.setStyle("-fx-background-color: #190640");
        back.setOnAction(actionEvent -> {
            try {
                ControllerMethods.setQuizzesScene((Stage)quizzesLabel.getScene().getWindow());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        vBox.getChildren().addAll(label, back);
        borderPane.setCenter(vBox);
    }

    public void startQuiz(String quizName, int quizLength){
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> optionAs = new ArrayList<>();
        ArrayList<String> optionBs = new ArrayList<>();
        ArrayList<String> optionCs = new ArrayList<>();
        ArrayList<String> optionDs = new ArrayList<>();
        ArrayList<String> correctOptions = new ArrayList<>();

        try(Connection connection = ControllerMethods.establishConnection();
            PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "SELECT * FROM " + quizName.replaceAll("\\s+", ""))) {
            ResultSet resultSet = ControllerMethods.getData(preparedStatement);
            while (resultSet.next()){
                questions.add(resultSet.getString(1));
                optionAs.add(resultSet.getString(2));
                optionBs.add(resultSet.getString(3));
                optionCs.add(resultSet.getString(4));
                optionDs.add(resultSet.getString(5));
                correctOptions.add(resultSet.getString(6));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: #a4e82e");

        ToggleGroup toggleGroup = new ToggleGroup();
        Button next = new Button("Next");
        next.setDisable(true);
        next.setStyle("-fx-background-color: #190640");
        next.setFont(Font.font("System", FontWeight.BOLD, 20));
        next.setTextFill(Color.YELLOW);
        next.setPrefHeight(25);
        next.setPrefWidth(100);

        Label quizLabel = new Label(quizName);
        quizLabel.setStyle("-fx-text-fill: #190640");
        quizLabel.setPadding(new Insets(0, 0, 50, 0));
        quizLabel.setFont(Font.font("System", FontWeight.BOLD, 30));
        quizLabel.setWrapText(true);


        Label question = new Label((currentQuestion+1) + ". "+ questions.get(currentQuestion));
        question.setFont(Font.font("System", FontWeight.BOLD, 25));
        question.setPadding(new Insets(0, 0, 50, 0));
        question.setWrapText(true);

        RadioButton option1 = new RadioButton(optionAs.get(currentQuestion));
        option1.setToggleGroup(toggleGroup);
        option1.setStyle("-fx-text-fill: #190640");
        option1.setFont(Font.font("System", FontWeight.NORMAL, 15));
        option1.setPrefHeight(50);
        option1.setPrefWidth(500);
        RadioButton option2 = new RadioButton(optionBs.get(currentQuestion));
        option2.setToggleGroup(toggleGroup);
        option2.setStyle("-fx-text-fill: #190640");
        option2.setFont(Font.font("System", FontWeight.NORMAL, 15));
        option2.setPrefHeight(50);
        option2.setPrefWidth(500);
        RadioButton option3 = new RadioButton(optionCs.get(currentQuestion));
        option3.setToggleGroup(toggleGroup);
        option3.setStyle("-fx-text-fill: #190640");
        option3.setFont(Font.font("System", FontWeight.NORMAL, 15));
        option3.setPrefHeight(50);
        option3.setPrefWidth(500);
        RadioButton option4 = new RadioButton(optionDs.get(currentQuestion));
        option4.setToggleGroup(toggleGroup);
        option4.setStyle("-fx-text-fill: #190640");
        option4.setFont(Font.font("System", FontWeight.NORMAL, 15));
        option4.setPrefHeight(50);
        option4.setPrefWidth(500);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                next.setDisable(true);
            } else {
                next.setDisable(false);
            }
        });
        if (currentQuestion<quizLength && ((currentQuestion+1)<quizLength)) {
            next.setOnAction(actionEvent -> {
                RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
                if (selected.getText().equals(correctOptions.get(currentQuestion))){
                    correctAnswers++;
                }
                currentQuestion++;
                startQuiz(quizName, quizLength);
            });
        } else{
            next.setText("Submit");
            next.setOnAction(actionEvent -> {
                RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
                if (selected.getText().equals(correctOptions.get(currentQuestion))){
                    correctAnswers++;
                }
                showResults(correctAnswers, quizLength);
                try(Connection connection = ControllerMethods.establishConnection();
                    PreparedStatement preparedStatement = ControllerMethods.newPreparedStatement(connection, "INSERT INTO Attempts(Quizz_id, User_id, total_questions, correct_answers, result_percentage) VALUES(?, ?, ?, ?, ?);");
                    PreparedStatement preparedStatement2 = ControllerMethods.newPreparedStatement(connection, "SELECT Quizz_Id FROM Quizzes WHERE Quiz_Name=?;")) {
                    preparedStatement2.setString(1, quizName);
                    ResultSet resultSet = ControllerMethods.getData(preparedStatement2);
                    int Quiz_Id = 0;
                    if (resultSet.next()) {
                        Quiz_Id = resultSet.getInt(1);
                    }
                    preparedStatement.setInt(1, Quiz_Id);
                    if (LogInController.currentUser!=null) {
                        preparedStatement.setInt(2, LogInController.currentUser.getUserId());
                    }else{
                        preparedStatement.setInt(2, SignUpController.currentUser.getUserId());
                    }
                    preparedStatement.setInt(3, quizLength);
                    preparedStatement.setInt(4, correctAnswers);
                    preparedStatement.setDouble(5, ((double)correctAnswers/quizLength)*100);

                    int rows = ControllerMethods.updateData(preparedStatement);
                    if (!(rows>0)){
                        ControllerMethods.showErrorAlert("Data could not be submitted.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });}
        vBox.getChildren().addAll(quizLabel, question, option1, option2, option3, option4, next);
        borderPane.setCenter(vBox);
    }




}