package com.example.demo.service;

import com.example.demo.WordCounter;

public class CounterService {
    private final WordCounter counter;

    public CounterService(boolean isPremium) {
        this.counter = isPremium
                ? new CounterPremium()
                : new CounterBasic();
    }
    public void acceptText(String text) {
        counter.acceptText(text);
    }

    public int getCount(String word) {
        return counter.getCount(word);
    }
}
