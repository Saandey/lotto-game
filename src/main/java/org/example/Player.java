package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Ticket> tickets;

    public Player(String name, int numberOfTickets) {
        this.name = name;
        this.tickets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Ticket> getTickets() {
        return tickets;

    }

    // Присваиваем билеты игроку
    public void assignTickets(int numberOfTickets) {
        tickets.clear(); // Очищаем старые билеты, если есть
        tickets.addAll(Ticket.generateTickets(numberOfTickets));  // Генерация новых билетов
    }

    public void checkNumber(int number) {
        for (Ticket ticket : tickets) {
            ticket.markMatchedNumber(number);//Помечаем число на своей карточке если совпало.
        }
    }
}

