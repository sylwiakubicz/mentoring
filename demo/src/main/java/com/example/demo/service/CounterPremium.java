package com.example.demo.service;

import com.example.demo.Counter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CounterPremium extends Counter {
    private final ConcurrentMap<String, Integer> concurrentStorage;

    public CounterPremium() {
        this(new ConcurrentHashMap<>());
    }

    public CounterPremium(ConcurrentMap<String, Integer> storage) {
        super(storage);
        this.concurrentStorage = storage;
    }

    @Override
    public List<String> splitIntoWords(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.asList(text.split("[\\s,.!?;:]+"));
    }

    @Override
    public boolean isValidWord(String word) {
        if (word == null || word.isBlank()) return false;
        return word.matches("[A-Za-z]+(?:['-][A-Za-z]+)*$");
    }

    @Override
    public String normalizeWord(String word) {
        return word.trim();
    }

    @Override
    public void incrementWordCount(String word) {
        concurrentStorage.merge(word, 1, Integer::sum);
    }
}
