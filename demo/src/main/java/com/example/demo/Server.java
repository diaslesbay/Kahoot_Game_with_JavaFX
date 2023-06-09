package com.example.demo;
import Structure.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static javafx.scene.paint.Color.*;
public class Server extends Application {
    public Quiz quiz;
    public Stage primaryStage;
    public MediaPlayer mediaPlayer;
    public Media media;
    public Label label;
    public Timeline timeline;
    public Label labelTimer;

    volatile int second1 = 15;
    volatile boolean starts;
    public String[] fillIn;
    public String PIN;
    private int j = 0;
    List<String> names = new ArrayList<>();
    List<Integer>  scores = new ArrayList<>();
    /**  STARTS  */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setScene(new Scene(chooseFile(), 500.0, 400.0));
        primaryStage.setTitle("Server");
        primaryStage.show();
        new Thread(() -> {
            try {
                ServerSocket main = new ServerSocket(1234);
                System.out.println("Connecting wait");
                int count = 1;
                while (true) {
                    Socket clientten = main.accept();
                    DataOutputStream send = new DataOutputStream(clientten.getOutputStream());
                    System.out.println("Success! Player " + count);
                    count++;
                    send.writeUTF(PIN);
                    new Thread(() -> {
                        try {
                            DataInputStream alu = new DataInputStream(clientten.getInputStream());
                            while (!starts) {
                                Thread.onSpinWait();
                            }
                            send.writeBoolean(starts);
                            send.writeInt(quiz.suraqtar.size());
                            for (int i = 0; i < quiz.suraqtar.size(); i++) {
                                if(quiz.Variants.get(i).size() > 1) {
                                    if (quiz.answers.get(i).equals(quiz.Variants.get(i).get(0))) {
                                        fillIn[i] = "A";
                                    }else  if (quiz.answers.get(i).equals(quiz.Variants.get(i).get(1))) {
                                        fillIn[i] = "B";
                                    }else  if (quiz.answers.get(i).equals(quiz.Variants.get(i).get(2))) {
                                        fillIn[i] = "C";
                                    }else  if (quiz.answers.get(i).equals(quiz.Variants.get(i).get(3))) {
                                        fillIn[i] = "D";
                                    }
                                }else{
                                    fillIn[i] = quiz.answers.get(i);
                                }
                            }
                            for (int i = 0; i < quiz.suraqtar.size(); i++) {
                                send.writeInt(quiz.Variants.get(i).size());
                                send.writeUTF(fillIn[i]);
                            }

                            int count1 = alu.readInt();
                            String name = alu.readUTF();
                            scores.add(count1);
                            names.add(name);
                        } catch (IOException ignored) {
                        }
                    }).start();
                }
            } catch (IOException ignored) {
            }
        }).start();
    }
    /**   LOAD  FROM  FILE  */
    public void options(int i) {
        quiz.s();
        while (i != quiz.suraqtar.size()) {
            quiz.Suraqtardy_Qosu(i);
            (quiz.TRUE_FALSE ? quiz.T : quiz.F).toString();
            i++;
        }
    }

    /**  CHOOSE FILE  */
    public StackPane chooseFile() throws IOException {
        Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(500);
        StackPane stackPane = new StackPane();
        Button buttonChoose = new Button("Choose a file");
        stackPane.getChildren().addAll(imageView, buttonChoose);
        buttonChoose.setOnAction(e -> {
            quiz = new Quiz();
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                quiz.filedan(file.getPath());
                fillIn = new String[quiz.suraqtar.size()];
                for (int i = 0; i < quiz.suraqtar.size(); i++) {
                    Collections.shuffle(quiz.Variants.get(i));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                primaryStage.setScene(new Scene(password(), 500.0, 400.0));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return stackPane;
    }

    /**  START  THE GAME  */
    public BorderPane start(int j1) throws IOException, InterruptedException {
        BorderPane borderPane = new BorderPane();
        if(quiz.suraqtar.size() == j1){
            timeline.stop();
            Thread.sleep(10);
            return end();
        }
        else{
            timeline = new Timeline();
            labelTimer = new Label();
            labelTimer.setText(secondSum(second1));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                try {
                    labelTimer.setText(secondSum(--second1));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }));
            timeline.play();
            options(0);

            label = new Label(j1 + 1 + ". " + quiz.suraqtar.get(j1));
            Circle circle = new Circle(40,Color.MEDIUMPURPLE);
            circle.setCenterX(58);
            circle.setCenterY(155);
            label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
            label.setWrapText(true);

            if (quiz.Variants.get(j1).size() == 4) {
                Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(240);
                imageView.setFitHeight(150);
                imageView.setX(130);
                imageView.setY(80);
                borderPane.getChildren().addAll(imageView);

                Button ButtonsA = new Button();
                Button ButtonsB = new Button();
                Button ButtonsC = new Button();
                Button ButtonsD = new Button();
                ButtonsA.setText(String.valueOf(quiz.Variants.get(j1).get(0)));
                ButtonsB.setText(String.valueOf(quiz.Variants.get(j1).get(1)));
                ButtonsC.setText(String.valueOf(quiz.Variants.get(j1).get(2)));
                ButtonsD.setText(String.valueOf(quiz.Variants.get(j1).get(3)));
                ButtonsA.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,15));
                ButtonsB.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,15));
                ButtonsC.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,15));
                ButtonsD.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,15));
                ButtonsA.setTextFill(WHITE);
                ButtonsB.setTextFill(WHITE);
                ButtonsC.setTextFill(WHITE);
                ButtonsD.setTextFill(WHITE);

                ButtonsA.setStyle("-fx-background-color : red");
                ButtonsA.setMinSize(245, 50);
                ButtonsB.setStyle("-fx-background-color : blue");
                ButtonsB.setMinSize(245, 50);
                ButtonsC.setStyle("-fx-background-color : yellow");
                ButtonsC.setMinSize(245, 50);
                ButtonsD.setStyle("-fx-background-color : green");
                ButtonsD.setMinSize(245, 50);
                VBox vBox1 = new VBox(3);
                vBox1.getChildren().addAll(ButtonsA,ButtonsC);
                VBox vBox2 = new VBox(3);
                vBox2.getChildren().addAll(ButtonsB,ButtonsD);
                HBox hBox = new HBox(3);
                hBox.getChildren().addAll(vBox1,vBox2);
                hBox.setPadding(new Insets(3));
                borderPane.setBottom(new StackPane(hBox));
                borderPane.setTop(new StackPane(label));
            }
            else {
                Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(240);
                imageView.setFitHeight(150);
                imageView.setX(130);
                imageView.setY(80);
                borderPane.getChildren().addAll(imageView);
                Image fillInLogo = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
                ImageView imageViewLogo = new ImageView(fillInLogo);
                imageViewLogo.setFitHeight(20);
                imageViewLogo.setFitWidth(35);
                label.setGraphic(imageViewLogo);
                label.setContentDisplay(ContentDisplay.LEFT);
                borderPane.setTop(new StackPane(label));

                TextField textField = new TextField();
                Text text = new Text("Type your answer here: ");
                text.setFont(Font.font("Times New Roman", FontWeight.BLACK, FontPosture.REGULAR, 13));
                VBox vBox = new VBox();
                textField.setMinHeight(20);
                textField.setMinWidth(300);
                vBox.getChildren().addAll(text, textField);
                vBox.setAlignment(Pos.BASELINE_CENTER);
                vBox.setLayoutX(250);
                vBox.setLayoutY(250);
                borderPane.getChildren().addAll(vBox);
                textField.setOnKeyTyped(e -> {
                    fillIn[j1] = textField.getText();
                });
            }
            labelTimer.setLayoutX(50);
            labelTimer.setLayoutY(143);
            labelTimer.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 22));
            labelTimer.setTextFill(WHITE);
            Pane pane = new Pane();
            pane.getChildren().addAll(circle,labelTimer);
            borderPane.getChildren().add(pane);
            return borderPane;
        }
    }

    /**  PASSWORD  PANE  */
    public BorderPane password() throws FileNotFoundException {

        File mediaFile = new File("C:\\Users\\Dias\\Downloads\\resourcesFile\\resources\\kahoot_music.mp3");
        media = new Media(mediaFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        BorderPane borderPane = new BorderPane();
        PIN = "" + (int) (Math.random() * 1000000);
        Text text = new Text("PIN : " + PIN);
        text.setFont(Font.font("Times New Roman", FontWeight.BLACK, FontPosture.ITALIC, 20));
        text.setFill(Color.DARKBLUE);
        text.setX(197);
        text.setY(81);
        Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\kahoot.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(500);
        Rectangle rectangle = new Rectangle(130, 30, 130, 30);
        rectangle.setArcHeight(6);
        rectangle.setArcWidth(6);
        rectangle.setFill(Color.WHITE);
        rectangle.setX(190);
        rectangle.setY(60);
        Button start = new Button("START");
        start.setStyle("-fx-background-color : khaki ");
        start.setFont(Font.font("Times New Roman", FontWeight.BLACK, FontPosture.ITALIC, 14));
        start.setTextFill(Color.DARKBLUE);
        start.setPrefSize(80, 20);
        start.setLayoutX(215);
        start.setLayoutY(110);
        Pane pane = new Pane();
        start.setOnAction(e -> {
            try {
                mediaPlayer.play();
                primaryStage.setScene(new Scene(start(0), 500, 400));
                starts = true;
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        pane.getChildren().addAll(start);
        borderPane.getChildren().addAll(imageView, rectangle, text, pane);
        return borderPane;
    }

    /**  END  THE  GAME  */
    public BorderPane end() throws IOException, InterruptedException {
        mediaPlayer.stop();
        timeline.stop();
        Thread.sleep(1000);

        for (int i = 0; i <scores.size() ; i++) {
            int index = i;
            for (int k = i+1; k < scores.size(); k++) {
                if(scores.get(k)>scores.get(index)){
                    index = k;
                }
            }
            int count = scores.get(index);
            scores.set(index,scores.get(i));
            scores.set(i,count);

            String temp = names.get(index);
            names.set(index,names.get(i));
            names.set(i,temp);
        }

        BorderPane borderPane1 = new BorderPane();
        Image EndPhoto = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
        ImageView imageViewEndPhoto = new ImageView(EndPhoto);
        imageViewEndPhoto.setFitWidth(500);
        imageViewEndPhoto.setFitHeight(400);
        borderPane1.setCenter(imageViewEndPhoto);
        Text textName1 = new Text("" + names.get(0));
        Text textName2 = new Text("" + names.get(1));
        Text textName3 = new Text("" + names.get(2));
        textName1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
        textName2.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        textName3.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        textName1.setFill(WHITE);
        textName2.setFill(WHITE);
        textName3.setFill(WHITE);
        textName2.setX(110);
        textName2.setY(230);
        textName1.setX(230);
        textName1.setY(170);
        textName3.setX(360);
        textName3.setY(250);
        borderPane1.getChildren().addAll(textName1,textName2,textName3);

        return borderPane1;
    }

    /**   TIMER  */
    public String secondSum(int second) throws IOException, InterruptedException {
        if (second1 == -1) {
            timeline.stop();
            second1 = 15;
            j++;
            if (quiz.suraqtar.size() >= j) {
                primaryStage.setScene(new Scene(start(j), 500.0, 400.0));
                timeline.play();
            }
        }
        return ""+second;
    }
}