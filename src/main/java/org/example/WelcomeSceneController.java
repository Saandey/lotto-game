package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WelcomeSceneController {

    @FXML
    private TextField player1TextField;
    @FXML
    private TextField player2TextField;

    @FXML
    private Label player1ErrorLabel;

    @FXML
    private Label player2ErrorLabel;

    public void startGame(ActionEvent actionEvent) throws Exception  {

        // Загружаем вторую сцену (саму игру),передаем имена игроков
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainGameScene.fxml"));
        Parent root = loader.load();
        String strPlayer1name = (player1TextField != null && !player1TextField.getText().trim().isEmpty() ?
                player1TextField.getText().trim() : "Игрок 1");
        String strPlayer2name = (player2TextField != null && !player2TextField.getText().trim().isEmpty() ?
                player2TextField.getText().trim():"Игрок 2");

        // Получаем контроллер второй сцены
        MainGameSceneController controller = loader.getController();

        // Передаем имена игроков в контроллер второй сцены
        controller.displayPlayersNames(strPlayer1name, strPlayer2name);

        // Переходим на новую сцену
        Scene gameScene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        // Устанавливаем размеры окна
        double windowWidth = 533;
        double windowHeight = 625;

        // Получаем информацию о размере экрана
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Вычисляем координаты для центрирования окна
        double x = (screenBounds.getWidth() - windowWidth) / 2;
        double y = (screenBounds.getHeight() - windowHeight) / 2;

        // Если окно выходит за экран по высоте, подкорректируем его положение
        if (y < 0) {
            y = 0;  // Устанавливаем окно в верхнюю часть экрана, если оно не влезает
        }

        // Устанавливаем размеры и позицию окна
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    public void initialize() {
        // Для первого игрока
        player1TextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(player1TextField, newValue, player1ErrorLabel);  // Валидация имени
        });

        // Для второго игрока
        player2TextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(player2TextField, newValue, player2ErrorLabel);  // Валидация имени
        });
    }

    private void validateName(TextField textField, String newValue, Label errorLabel) {
        String namePattern = "^[A-Za-zА-Яа-я]+$";
        if (!newValue.matches(namePattern) || newValue.length() < 3 || newValue.length() > 15) {
            textField.setStyle("-fx-border-color: red;");  // Подсвечиваем ошибку
            errorLabel.setVisible(true);  // Показываем метку ошибки
        } else {
            textField.setStyle("-fx-border-color: none;");  // Убираем подсветку
            errorLabel.setVisible(false);  // Скрываем метку ошибки
        }
    }
}
