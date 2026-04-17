package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

public class CounterBasicTest {

    private CounterBasic counterBasic;

    @BeforeEach
    void setUp() {
        counterBasic = new CounterBasic();
    }


    @Test
    void shouldDemonstrateRaceCondition() throws InterruptedException {
        int threads = 1000;
        int repeats = 100;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < repeats; j++) {
                        counterBasic.acceptText("hello");
                    }
                });
            }
        }

        assertThat(counterBasic.getCount("hello"))
                .isLessThanOrEqualTo(threads * repeats);

        System.out.println("Wynik: " + counterBasic.getCount("hello") +
                " oczekiwano: " + (threads * repeats));
    }

    // splitIntoWords
    @Test
    void shouldReturnEmptyList_whenTextIsNull() {
        assertThat(counterBasic.splitIntoWords(null)).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenTextIsBlank() {
        assertThat(counterBasic.splitIntoWords("   ")).isEmpty();
    }

    @Test
    void shouldSplitTextByWhitespace() {
        List<String> result = counterBasic.splitIntoWords("Hello   world Java");
        assertThat(result).isEqualTo(List.of("Hello", "world", "Java"));
    }

    @Test
    void shouldKeepPunctuationAttachedToWord() {
        List<String> result = counterBasic.splitIntoWords("Hello, world!");
        assertThat(result).isEqualTo(List.of("Hello,", "world!"));
    }


    // isValidWord
    @Test
    void shouldReturnFalse_whenWordIsNull() {
        assertThat(counterBasic.isValidWord(null)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenWordIsBlank() {
        assertThat(counterBasic.isValidWord("  ")).isFalse();
    }

    @Test
    void shouldReturnFalse_forWordContainingDigits() {
        assertThat(counterBasic.isValidWord("world123")).isFalse();
    }

    @Test
    void shouldReturnTrue_forAlphabeticWord() {
        assertThat(counterBasic.isValidWord("world")).isTrue();
    }

    @Test
    void shouldReturnFalse_forWordContainingPunctuation() {
        assertThat(counterBasic.isValidWord("world!")).isFalse();
        assertThat(counterBasic.isValidWord("don't")).isFalse();
        assertThat(counterBasic.isValidWord("mother-in-law")).isFalse();
    }

    // normalizeWord
    @Test
    void shouldConvertWordToLowerCase() {
        assertThat(counterBasic.normalizeWord("HeLlO")).isEqualTo("hello");
    }

    @Test
    void shouldKeepLowerCaseWordUnchanged() {
        assertThat(counterBasic.normalizeWord("hello")).isEqualTo("hello");
    }


    // incrementWord
    @Test
    void shouldAddWordWithCountOne_whenWordIsNotPresent() {
        counterBasic.incrementWordCount("hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldIncreaseCount_whenWordAlreadyExists() {
        counterBasic.incrementWordCount("hello");
        counterBasic.incrementWordCount("hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(2);
    }

    // acceptText
    @Test
    void shouldCountSingleValidWord() {
        counterBasic.acceptText("hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldAddMultipleValidWords() {
        counterBasic.acceptText("hello hello world");
        assertThat(counterBasic.getCount("hello")).isEqualTo(2);
        assertThat(counterBasic.getCount("world")).isEqualTo(1);
    }

    @Test
    void shouldIgnorePunctuation_attachedToWord() {
        counterBasic.acceptText("hello, world!");
        // "hello," i "world!" nie przechodzą walidacji
        assertThat(counterBasic.getCount("hello")).isEqualTo(0);
        assertThat(counterBasic.getCount("world")).isEqualTo(0);
    }

    @Test
    void shouldBeCaseInsensitive() {
        counterBasic.acceptText("Hello HELLO hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(3);
    }

    @Test
    void shouldIgnoreWordsWithDigits() {
        counterBasic.acceptText("hello123 world");
        assertThat(counterBasic.getCount("hello123")).isEqualTo(0);
        assertThat(counterBasic.getCount("world")).isEqualTo(1);
    }

    // getCount
    @Test
    void shouldReturnZeroForUnknownWord() {
        assertThat(counterBasic.getCount("unknown")).isEqualTo(0);
    }

}
