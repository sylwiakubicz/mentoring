package com.example.demo;


import java.util.List;
import java.util.Map;

public abstract class Counter implements WordCounter {
    protected Map<String, Integer> storage;

    public Counter(Map<String, Integer> storage) {
        this.storage = storage;
    }

    @Override
    public int getCount(String word) {
        String normalizedWord = normalizeWord(word);
        return storage.getOrDefault(normalizedWord, 0);

    }

    @Override
    public void acceptText(String text) {
        List<String> wordsToAdd = splitIntoWords(text);

        List<String> acceptedWords = wordsToAdd.stream()
                .map(this::normalizeWord)
                .filter(this::isValidWord)
                .toList();

        for (String word : acceptedWords) {
            incrementWordCount(word);
        }
    }

    public abstract List<String> splitIntoWords(String text);
    public abstract boolean isValidWord(String text);
    public abstract String normalizeWord(String word);
    public abstract void incrementWordCount(String word);
}
