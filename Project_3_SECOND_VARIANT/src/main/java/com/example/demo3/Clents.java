package com.example.demo3;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Clents extends Application {
    private static int QUESTIONS_NUMBERS;
    private static int questions_NUMBERS =0;
    private static int second_Timer = 4;
    private static Timeline TIMER;
    private Stage window;
    private final double W = 600., H = 600;
    private Pane panes;
    private static List<String> ANSWERS_TEST = new ArrayList<>();
    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    private void connectToServer() throws IOException {
        socket = new Socket("localhost", 11111);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }
    public void START_TIMER(int second) throws Exception {
        if (second_Timer == -1) {
            TIMER.stop();
            second_Timer = 4;
            questions_NUMBERS++;
            if (QUESTIONS_NUMBERS > questions_NUMBERS) {
                window.setScene(new Scene(PLAY(), 400, 400.0));
                TIMER.play();
            } else if (QUESTIONS_NUMBERS == questions_NUMBERS) {
                window.setScene(new Scene(Resuilts(), 400, 400));
                TIMER.stop();
            }
        }
    }
    public Button kahootButton(String btnColor) {
        Button btn = new Button();
        btn.setMinWidth(W / 2. - 5);
        btn.setMinHeight(H / 2. - 5);
        btn.setStyle("-fx-background-color: " + btnColor);

        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15);
        btn.setFont(font);
        return btn;
    }

    public StackPane PLAY() throws IOException, InterruptedException {
        TIMER = new Timeline();
        TIMER.setCycleCount(Timeline.INDEFINITE);
        TIMER.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            try {
                START_TIMER(--second_Timer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        TIMER.play();

        StackPane stackPane = new StackPane();
        VBox vBox1 = new VBox(10);
        Button BUTTON_A = kahootButton("RED");
        Button BUTTON_C = kahootButton("BLUE");
        vBox1.getChildren().addAll(BUTTON_A, BUTTON_C);
        VBox vBox2 = new VBox(10);
        Button BUTTON_B = kahootButton("YELLOW");
        Button BUTTON_D = kahootButton("GREEN");
        vBox2.getChildren().addAll(BUTTON_B, BUTTON_D);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox1, vBox2);
        stackPane.getChildren().addAll(hBox);
        Image image = new Image("C:\\Users\\Dias\\Desktop\\kahoot.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);

        BUTTON_A.setOnAction(e -> {
            ANSWERS_TEST.add(questions_NUMBERS,"RED");
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(imageView);
            window.setScene(new Scene(borderPane));

        });
        BUTTON_B.setOnAction(e -> {
            ANSWERS_TEST.add(questions_NUMBERS,"YELLOW");
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(imageView);
            window.setScene(new Scene(borderPane));

        });
        System.out.println("count"+questions_NUMBERS);
        BUTTON_C.setOnAction(e -> {
            ANSWERS_TEST.add(questions_NUMBERS,"BLUE");
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(imageView);
            window.setScene(new Scene(borderPane));

        });

        BUTTON_D.setOnAction(e -> {
            ANSWERS_TEST.add(questions_NUMBERS,"GREEN");
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(imageView);
            window.setScene(new Scene(borderPane));
        });
        return stackPane;
    }
    public StackPane AFTER_ENTER_PANE() throws IOException {
        StackPane stackPane = new StackPane();
        TextField GAME_ENTER = new TextField();
        GAME_ENTER.setPromptText("Enter username");
        GAME_ENTER.setMaxWidth(W / 3);
        GAME_ENTER.setMinHeight(40);
        GAME_ENTER.setAlignment(Pos.CENTER);
        Button BUTTON_NAME = new Button("Enter");
        BUTTON_NAME.setMaxWidth(W / 3);
        BUTTON_NAME.setMinHeight(40);
        BUTTON_NAME.setStyle("-fx-background-color:#333333");
        BUTTON_NAME.setTextFill(Color.WHITE);
        BUTTON_NAME.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(W / 2);
        vBox.setMaxHeight(H / 2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(GAME_ENTER, BUTTON_NAME);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #46178f");

        BUTTON_NAME.setOnAction(e -> {
            try {
                int START = fromServer.readInt();
                if(START==1) {
                    window.setScene(new Scene(PLAY(), W, H));
                    window.setTitle(GAME_ENTER.getText());
                    QUESTIONS_NUMBERS = fromServer.readInt();

                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        return stackPane;
    }
    public StackPane ENTER_PIN() {
        StackPane stackPane = new StackPane();
        TextField GAME_PIN = new TextField();
        GAME_PIN.setPromptText("Game PIN");
        GAME_PIN.setMaxWidth(W / 3);
        GAME_PIN.setMinHeight(40);
        GAME_PIN.setAlignment(Pos.CENTER);
        Button BUTTON_ENTER = new Button("Enter");
        BUTTON_ENTER.setMaxWidth(W / 3);
        BUTTON_ENTER.setMinHeight(40);
        BUTTON_ENTER.setStyle("-fx-background-color:#333333");
        BUTTON_ENTER.setTextFill(Color.WHITE);
        BUTTON_ENTER.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(W / 2);
        vBox.setMaxHeight(H / 2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(GAME_PIN, BUTTON_ENTER);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #3e147f");

        BUTTON_ENTER.setOnAction(e -> {
            try {
                toServer.writeInt(Integer.parseInt(GAME_PIN.getText() + "") );
                String ANSWER = fromServer.readUTF();
                if (ANSWER.equals("CONNECT!")) {
                    window.setScene(new Scene(AFTER_ENTER_PANE(), W, H));
                    window.setTitle("Enter Nickname");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return stackPane;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        connectToServer();
        panes = ENTER_PIN();
        stage.setScene(new Scene(panes, W, H));
        stage.show();
        stage.setTitle("Enter PIN!");
        panes.requestFocus();
    }
    public static BorderPane Resuilts() throws FileNotFoundException {
        BorderPane borderPane = new BorderPane();
        Image image = new Image(new FileInputStream("C:\\Users\\Dias\\Desktop\\background.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        borderPane.setCenter(imageView);
        return borderPane;
    }

}
