package org.example;

import java.util.HashSet;
import java.util.Set;

public class NumberGenerator {
    private Set<Integer> drawnNumbers = new HashSet<>();

    public int drawNumber() {
        int number;
        do {
            number = (int) (Math.random() * 90) + 1; // Число от 1 до 90
        } while (drawnNumbers.contains(number)); // Убедимся, что число не повторяется
        drawnNumbers.add(number);
        return number;
    }
}
