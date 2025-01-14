package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private Set<Integer> drawnNumbers;
    private List<Player> players;
    private NumberGenerator numberGenerator;
    private List<Player> winners;

    public Game(List<Player> players) {
        this.players = players;
        this.drawnNumbers = new HashSet<>();
        this.numberGenerator = new NumberGenerator();
        this.winners = new ArrayList<>();
    }

    // Генерация нового числа
    public int drawNumber() {
        int number = numberGenerator.drawNumber();
        drawnNumbers.add(number);  // Добавляем число в список вытянутых
        return number;
    }

    // Проверка победителя
    public String checkWinner() {

        for (Player player : players) {
            boolean allTicketsClosed = true;

            // Проверяем каждый билет игрока
            for (Ticket ticket : player.getTickets()) {
                if (!ticket.isTicketClosed()) {
                    allTicketsClosed = false;
                    break;  // Если хотя бы один билет не закрыт, проверяем другого игрока
                }
            }

            // Если все билеты закрыты, добавляем игрока в список победителей
            if (allTicketsClosed) {
                winners.add(player);
            }
        }

        // Если в списке победителей больше одного игрока, это ничья
        if (winners.size() > 1) {
            return "Ничья";
        } else if (winners.size() == 1) {
            // Если в списке только один игрок, возвращаем его имя как победителя
            return winners.get(0).getName();
        }

        // Если нет игроков, у которых все билеты закрыты, значит, нет победителя
        return null;  // Нет победителя
    }

    // Метод для очистки списка победителей
    public void clearWinners() {
        winners.clear();  // Очищаем список победителей
    }
}
