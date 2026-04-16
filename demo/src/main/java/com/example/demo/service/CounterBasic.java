package com.example.demo.service;

import com.example.demo.Counter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CounterBasic extends Counter {

    public CounterBasic() {
        super(new HashMap<>());
    }

    @Override
    public List<String> splitIntoWords(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.asList(text.split("\\s+"));
    }

    @Override
    public boolean isValidWord(String word) {
        if (word == null || word.isBlank()) return false;
        return word.matches("[A-Za-z]+");
    }

    @Override
    public String normalizeWord(String word) {
        return word.toLowerCase();
    }

    @Override
    public void incrementWordCount(String word) {
        storage.merge(word, 1, Integer::sum);
    }
}
