package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGameSceneController {
    private Game game;
    private List<Player> players;

    @FXML
    private Label drawnNumberLabel;
    @FXML
    private Button drawButton;
    @FXML
    private Button autoDrawButton;
    // Флаг, чтобы остановить автоматическое вытягивание
    private boolean isGameFinished = false;
    // Флаг для отслеживания состояния игры
    private boolean isAutoDrawActive = false;
    //private boolean isGameRunning = false; // Игра не начата по умолчанию
    @FXML
    private GridPane ticketsPlayer1; // Панель для билета Игрок1
    @FXML
    private GridPane ticketsPlayer2; // Панель для билета Игрок2
    @FXML
    private GridPane allNumbersGridPane; // Панель для кнопок с номерами
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label drawnNumberText;
    @FXML
    private HBox hBoxPlayer1;
    @FXML
    private HBox hBoxPlayer2;

    private Label totalMatchesPlayer1;
    private Label totalMatchesPlayer2;

    private Timeline timeline;

    private List<GridPane> ticketGrids = new ArrayList<>();  // Список для карточек игроков

    public void displayPlayersNames(String strPlayer1name, String strPlayer2name) {

        player1Label.setText(strPlayer1name);
        player2Label.setText(strPlayer2name);

        players = new ArrayList<>();
        players.add(new Player(strPlayer1name, 1));
        players.add(new Player(strPlayer2name, 1));

    }

    public void initialize() {

        // Создание сетки со всеми номерами лотто 1-90
        createLottoCard();
        initializeGame();

    }

    public void initializeGame() {

        drawButton.setDisable(true);
        autoDrawButton.setDisable(true);

        drawnNumberText.setVisible(false);
        drawnNumberLabel.setVisible(false);

        totalMatchesPlayer1 = new Label();
        hBoxPlayer1.getChildren().add(totalMatchesPlayer1);

        totalMatchesPlayer2 = new Label();
        hBoxPlayer2.getChildren().add(totalMatchesPlayer2);

        game = new Game(players);
    }

    private void createLottoCard() {
        // Создание ячеек на карточке лото
        int rows = 9; // 9 строк
        int columns = 10; // 10 столбцов (всего 90 чисел)

        for (int i = 0; i < 90; i++) {
            Label label = new Label(String.valueOf(i + 1)); // создаём метку для каждого числа
            label.setPrefSize(23, 23); // Размер ячейки
            label.setStyle("-fx-border-color: black; -fx-padding: 0px; -fx-font-size: 12px; -fx-alignment: center;"); // Стилизация метки

            // Добавляем метку в сетку
            allNumbersGridPane.add(label, i % columns, i / columns); // Размещение меток в GridPane
        }
    }

    public void distributeTickets(ActionEvent actionEvent) {

        restartGame();
        createTickets();
        drawButton.setDisable(false);
        autoDrawButton.setDisable(false);

    }

    private void createTickets() {

        ticketGrids.clear();

        ticketGrids.add(ticketsPlayer1);
        ticketGrids.add(ticketsPlayer2);

        // Генерация билетов для каждого игрока
        for (Player player : players) {
            player.assignTickets(1);
            int rows = 3; // 3 строки
            int columns = 8; // 8 столбцов

            // Для каждого билета заполняем соответствующий GridPane

            GridPane ticketGrid = ticketGrids.get(players.indexOf(player));
            ticketGrid.getChildren().clear();  // Очистить старые метки

            // Для каждого билета создаем метки
            for (Ticket ticket : player.getTickets()) {
                List<Integer> ticketNumbers = ticket.getNumbers();  // Получаем числа билета

                // Для каждого числа в билете создаем метку и добавляем в GridPane
                for (int i = 0; i < ticketNumbers.size(); i++) {
                    int number = ticketNumbers.get(i);

                    // Создаем метку для этого числа
                    Label label = new Label(String.valueOf(number));
                    label.setPrefSize(23, 23);  // Размер ячейки
                    label.setStyle("-fx-border-color: black; -fx-padding: 0px; -fx-font-size: 12px; -fx-alignment: center;");  // Стилизация метки

                    // Добавляем метку в сетку (размещение по колонкам и строкам)
                    ticketGrid.add(label, i % columns, i / columns);  // Размещение меток в GridPane
                }
            }
        }
    }

    public void drawNumber(ActionEvent actionEvent) {

        // Генерация нового числа
        int number = game.drawNumber();
        drawnNumberText.setVisible(true);
        drawnNumberLabel.setVisible(true);
        drawnNumberLabel.setText("" + number);  // Обновляем метку с числом "Выпавшее число: " +

        highlightNumber(number);

        // Ищем число у каждого игрока
        for (Player player : players) {
            player.checkNumber(number);  // Проверяем совпадение на своей карточке.

        }
        for (int i = 0; i < players.size(); i++){
            for (Ticket ticket:players.get(i).getTickets()) {
                int totalMatches = ticket.getMatchedNumbers().size();
                int totalNumbers = ticket.getNumbers().size();

                // Обновляем текст метки для первого игрока (если i == 0)
                if (i == 0) {
                    totalMatchesPlayer1.setText(totalMatches + " / " + totalNumbers);// + ticket.getMatchedNumbers());
                }
                // Обновляем текст метки для второго игрока (если i == 1)
                else if (i == 1) {
                    totalMatchesPlayer2.setText(totalMatches + " / " + totalNumbers);// + ticket.getMatchedNumbers());
                }
            }
        }

        // Проверяем победителя только если игра не завершена
        if (!isGameFinished) {

            String result = game.checkWinner();
            if (result != null) {
                isGameFinished = true;
                showWinner(result);
            }
        }
    }

    public void autoDraw(ActionEvent actionEvent) {
        if (isAutoDrawActive) {
            return; // Если автогейм уже активен, не запускаем его снова
        }

        // Устанавливаем флаг, что автогейм активен
        isAutoDrawActive = true;
        timeline = new Timeline( //Timeline
            new KeyFrame(Duration.millis(10), event -> {
                if (isGameFinished) {
                    return;  // Останавливаем таймер, если игра завершена
                }
                drawNumber(actionEvent);
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);  // Запускаем таймер до завершения игры
        timeline.play();

        String winner = game.checkWinner();
        if (winner != null) {  // Если найден победитель
            isGameFinished = true;
            // Останавливаем таймлайн, чтобы прекратить анимацию
            timeline.stop();

            // Показываем диалог в главном потоке
            Platform.runLater(() -> showWinner(winner));
        }

    }

    // Метод для подсветки числа
    private void highlightNumber(int number) {
        // Список всех сеток, в которых нужно выделить число
        List<GridPane> grids = Arrays.asList(allNumbersGridPane, ticketsPlayer1, ticketsPlayer2);

        // Проходим по каждой сетке
        for (GridPane grid : grids) {
            // Ищем все метки в сетке и выделяем нужную метку
            for (Node node : grid.getChildren()) {
                if (node instanceof Label) {
                    Label label = (Label) node;
                    if (Integer.parseInt(label.getText()) == number) {
                        label.setStyle("-fx-background-color: yellow;");// -fx-border-color: black; -fx-padding: 10px; -fx-font-size: 12px; -fx-alignment: center;");
                    }
                }
            }
        }
    }

    private void resetHighlighting() {
        // Ищем все метки в сетке и сбрасываем их стиль
        for (Node node : allNumbersGridPane.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-border-color: black; -fx-padding: 0px; -fx-font-size: 12px; -fx-alignment: center;"); // Возвращаем исходный стиль
            }
        }
    }

    @FXML
    private void restartGame() {
        // Если автогейм активен, остановим его
        if (isAutoDrawActive && timeline != null) {
            timeline.stop();  // Останавливаем таймер
        }
        // Останавливаем автогейм, если он был активен
        isAutoDrawActive = false;
        // Сбросить все элементы для начала новой игры
        resetHighlighting();
        game.clearWinners();

//        try {
//            System.out.println(game.checkWinner());
//        } catch (NullPointerException e) {
//
//        }

        isGameFinished = false;

        hBoxPlayer1.getChildren().remove(totalMatchesPlayer1);
        hBoxPlayer2.getChildren().remove(totalMatchesPlayer2);

        for (Player player:players) {
            player.getTickets().clear();
        }
        for (GridPane ticket : ticketGrids) {
            ticket.getChildren().clear();
        }

        initializeGame();
    }

    private void showWinner(String result) {

        Platform.runLater(() -> {
            // Создаем Alert с типом INFORMATION
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Результат игры");
            alert.setHeaderText(null);  // Без заголовка

            if (result.equals("Ничья")) {
                alert.setContentText("Игра окончена! Ничья!");
            } else {
                alert.setContentText("Игра окончена!\nПобедитель: " + result);  // Имя победителя
            }

            // Добавляем кнопки "Играть снова" и "Выход"
            ButtonType newGameButton = new ButtonType("Новая игра");
            ButtonType exitButton = new ButtonType("Выход");

            alert.getButtonTypes().setAll(newGameButton, exitButton);

            // Привязываем Alert к основному окну, чтобы он отображался в центре
            Stage stage = (Stage) drawButton.getScene().getWindow();
            alert.initOwner(stage);

            // Показываем Alert и ждем выбора пользователя
            alert.showAndWait().ifPresent(response -> {
                if (response == newGameButton) {
                    restartGame();
                } else if (response == exitButton) {
                    System.exit(0);
                }
            });
        });
    }
}
