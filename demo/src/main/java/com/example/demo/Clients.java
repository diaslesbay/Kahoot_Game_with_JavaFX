package com.example.demo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Clients extends Application {

    private Stage stage;
    private BorderPane borderPane;
    public StackPane stackPane;
    List<String> fillIn = new ArrayList<>();
    public Timeline timeline;
    public VBox vBox;
    public Socket socket;

    public DataOutputStream send;
    public DataInputStream alu;

    private Button buttonJoin;
    private TextField textName;
    private TextField textPassword;

    int second1 = 15;
    int numbers;
    int j = 0;
    int p = 0;
    List<String> answers = new ArrayList<>();

    /** START */
    @Override
    public void start(Stage primaryStage) throws IOException {
        socket = new Socket("localhost", 1234);
        send = new DataOutputStream(socket.getOutputStream());
        alu = new DataInputStream(socket.getInputStream());
        String s = alu.readUTF();
        Join();
        buttonJoin.setOnAction(e -> {
            if (s.equals(textPassword.getText())) {
                stage.setScene(new Scene(join(),200,200));
                buttonJoin.setOnAction(e1->{
                    if(textName.getText()!=null){
                        try {
                            if(alu.readBoolean()){
                                numbers = alu.readInt();
                                stage.setScene(new Scene(starts(0)));
                                stage.setTitle(String.valueOf(textName.getText()));
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            else if(!s.equals(textPassword.getText()) && p==0) {
                Text textW = new Text("Wrong answers! Write again");
                textW.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 13));
                textW.setFill(Color.RED);
                Platform.runLater(() -> {
                    borderPane.setBottom(new StackPane(textW));
                });
                p++;
            }
        });

        stage = primaryStage;
        stage.setScene(new Scene(borderPane));
        stage.setTitle("Player");
        stage.show();
        borderPane.requestFocus();
    }

    /**  BACKGROUND   MAIN   PANE */
    public  void init(){
        borderPane= new BorderPane();
//        borderPane.setBackground(Background.fill(Color.BLUEVIOLET));
        borderPane.setPrefWidth(200);
        borderPane.setPrefHeight(200);
    }

    /**  END  THE  GAME  */
    public BorderPane end() throws IOException {
//        mediaPlayer.stop();
        BorderPane borderPane = new BorderPane();
        int count = 0;
        for (int i = 0; i < numbers; i++) {
            if(fillIn.get(i).equals(answers.get(i))){
                count++;
            }
        }
        send.writeInt(count);
        send.writeUTF(textName.getText());
        Text text1 = new Text("Your Score :");
        text1.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,18));
        text1.setFill(Color.WHITE);
        Text text = new Text(""+count);
        text.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,30));
        text.setFill(Color.WHITE);
        text1.setX(60);
        text1.setY(28);
        text.setX(93);
        text.setY(90);
        Pane pane = new Pane();
        pane.getChildren().addAll(text1,text);
        Image EndPhoto = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
        ImageView imageViewEndPhoto = new ImageView(EndPhoto);
        imageViewEndPhoto.setFitWidth(200);
        imageViewEndPhoto.setFitHeight(200);
        imageViewEndPhoto.setX(0);
        imageViewEndPhoto.setY(0);
        borderPane.getChildren().addAll(imageViewEndPhoto);
        borderPane.setPrefSize(200, 200);
        borderPane.setCenter(pane);
        return borderPane;
    }

    /**   WAITING  NEXT  QUESTIONS  */
    public StackPane Wait() throws FileNotFoundException {
        StackPane stackPane = new StackPane();
        Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
        ImageView imageViewEndPhoto = new ImageView(image);
        imageViewEndPhoto.setFitWidth(200);
        imageViewEndPhoto.setFitHeight(200);
        stackPane.getChildren().addAll(imageViewEndPhoto);
        stackPane.setPrefSize(200, 200);
        return stackPane;
    }

    /**  START  PANE */
    public BorderPane starts(int i) throws IOException {
        BorderPane borderPane = new BorderPane();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            try {
                secondSum(--second1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timeline.play();
        int k = alu.readInt();
        String s = alu.readUTF();
        answers.add(s);
        if (k == 4) {
            Button ButtonsA = new Button();
            Button ButtonsB = new Button();
            Button ButtonsC = new Button();
            Button ButtonsD = new Button();

            ButtonsA.setStyle("-fx-background-color : red");
            ButtonsA.setMinSize(95, 95);
            ButtonsB.setStyle("-fx-background-color : blue");
            ButtonsB.setMinSize(95, 95);
            ButtonsC.setStyle("-fx-background-color : yellow");
            ButtonsC.setMinSize(95, 95);
            ButtonsD.setStyle("-fx-background-color : green");
            ButtonsD.setMinSize(95, 95);
            VBox vBox1 = new VBox(3);
            vBox1.getChildren().addAll(ButtonsA,ButtonsC);
            VBox vBox2 = new VBox(3);
            vBox2.getChildren().addAll(ButtonsB,ButtonsD);
            HBox hBox = new HBox(3);
            hBox.getChildren().addAll(vBox1,vBox2);
            hBox.setPadding(new Insets(3));

            ButtonsA.setOnAction(e -> {
                fillIn.add(i,"A");
                try {
                    stage.setScene(new Scene(Wait()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            ButtonsB.setOnAction(e -> {
                fillIn.add(i,"B");

                try {
                    stage.setScene(new Scene(Wait()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            ButtonsC.setOnAction(e -> {
                fillIn.add(i,"C");
                try {
                    stage.setScene(new Scene(Wait()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            ButtonsD.setOnAction(e -> {
                fillIn.add(i,"D");
                try {
                    stage.setScene(new Scene(Wait()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            borderPane.setStyle("-fx-background-color : lightyellow");
            borderPane.setCenter(hBox);

        } else {
            Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\Kahoot_Game_with_JavaFX\\kahoot.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setY(50);

            TextField textField = new TextField();
            Text text = new Text("Your answer : ");
            text.setFont(Font.font("Times New Roman", FontWeight.BLACK, FontPosture.REGULAR, 12));
            VBox vBox = new VBox();
            textField.setMaxWidth(120);
            textField.setMaxHeight(10);
            textField.setPromptText("Type your answer here");
            textField.setOnKeyTyped(e->{
                fillIn.add(i,textField.getText());

            });
            vBox.getChildren().addAll(text, textField, imageView);
            vBox.setAlignment(Pos.CENTER);
            borderPane.setCenter(new StackPane(vBox));
            borderPane.setPrefSize(200, 200);
        }
        return borderPane;
    }

    /**  ENTER  PIN  CODE */
    public void Join() {
        textPassword = new TextField();
        textPassword.setPromptText("     Game PIN");
        vBox = new VBox(3);
        vBox.setMaxHeight(100);
        vBox.setMaxWidth(100);
        vBox.setAlignment(Pos.CENTER);
        buttonJoin = new Button("Enter");
        buttonJoin.setTextFill(Color.WHITE);
        buttonJoin.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,12));
        buttonJoin.setPrefSize( 100, 25);
        buttonJoin.setStyle("-fx-background-color : black");
        Text text = new Text("Kahoot!");
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 22));
        text.setFill(Color.WHITE);
        text.setX(20);
        text.setY(20);
        Pane pane = new Pane();
        pane.getChildren().add(text);
        vBox.getChildren().addAll(pane,textPassword,buttonJoin);
        borderPane.setCenter(new StackPane(vBox));
        Text text2 = new Text("   ");
//        text2.setTabSize(13);
        borderPane.setBottom(new StackPane(text2));
    }

    /** ENTER NAME */
    public StackPane join(){
        textName = new TextField();
        textName.setPromptText("    nickName");
        textName.setFocusTraversable(false);

        vBox = new VBox(3);
        vBox.setMaxHeight(100);
        vBox.setMaxWidth(100);
        vBox.setAlignment(Pos.CENTER);

        buttonJoin = new Button("Enter");
        buttonJoin.setTextFill(Color.WHITE);
        buttonJoin.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,12));
        buttonJoin.setPrefSize( 100, 25);
        buttonJoin.setStyle("-fx-background-color : black");

        Text text = new Text("Kahoot!");
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 22));
        text.setFill(Color.WHITE);
        text.setX(65);
        text.setY(55);

        Pane pane = new Pane();
        pane.getChildren().add(text);

        stackPane = new StackPane();
//        stackPane.setBackground(Background.fill(Color.BLUEVIOLET));
        stackPane.setPrefWidth(200);
        stackPane.setPrefHeight(200);
        vBox.getChildren().addAll(textName,buttonJoin);
        stackPane.getChildren().addAll(pane, vBox);
        return stackPane;
    }

    /** TIMER */
    public void secondSum(int second) throws IOException {
        if (second1 == -1){
            timeline.stop();
            second1 = 15;
            j++;
            if (numbers > j) {
                stage.setScene(new Scene(starts(j), 200, 200.0));
                timeline.play();
            }else if(numbers == j){
                stage.setScene(new Scene(end(),200,200));
                timeline.stop();
            }
        }
    }
}
