package org.example;

import java.util.*;

public class Ticket {
    private List<Integer> numbers;
    private List<Integer> matchedNumbers; // Список для хранения совпавших чисел

    public Ticket() {
        numbers = new ArrayList<>();
        Random rand = new Random();

        // Генерация 24 уникальных чисел (например, от 1 до 90)
        while (numbers.size() < 24) {
            int number = rand.nextInt(90) + 1;
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }
        matchedNumbers = new ArrayList<>();
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public List<Integer> getMatchedNumbers() {
        return matchedNumbers;
    }

    public static List<Ticket> generateTickets(int numberOfTickets) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < numberOfTickets; i++) {
            tickets.add(new Ticket());
        }
        return tickets;
    }

    public void markMatchedNumber(int number) {
        if (numbers.contains(number) ) {
            matchedNumbers.add(number); // Добавляем число в список совпавших
        }
    }

    public boolean isTicketClosed() {
        return matchedNumbers.size() == 24;  // Если все 24 чисел вычеркнуты, билет закрыт
    }

}
