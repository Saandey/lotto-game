package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class LottoGameGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Игра в Лото");
        InputStream iconStream = getClass().getResourceAsStream("/icons/icon.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcomeScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles" +
                "/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Запуск JavaFX-приложения
    public static void main(String[] args) {
        launch(args);
    }
}
