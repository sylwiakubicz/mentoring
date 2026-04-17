package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterPremiumTest {

    private CounterPremium counterPremium;

    @BeforeEach
    void setUp() {
        counterPremium = new CounterPremium();
    }


    // Thread safe
    @Test
    void shouldHandleConcurrentAccess() throws InterruptedException {
        int threads = 1000;
        int repeats = 100;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < repeats; j++) {
                        counterPremium.acceptText("hello");
                    }
                });
            }
        }

        assertThat(counterPremium.getCount("hello")).isEqualTo(threads * repeats);
    }

    // splitIntoWords
    @Test
    void shouldReturnEmptyList_whenTextIsNull() {
        assertThat(counterPremium.splitIntoWords(null)).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenTextIsBlank() {
        assertThat(counterPremium.splitIntoWords("   ")).isEmpty();
    }

    @Test
    void shouldSplitTextByWhitespace() {
        List<String> result = counterPremium.splitIntoWords("Hello   world Java");
        assertThat(result).isEqualTo(List.of("Hello", "world", "Java"));
    }

    @Test
    void shouldSplitTextByPunctuation() {
        List<String> result = counterPremium.splitIntoWords("Hello,world!");
        assertThat(result).isEqualTo(List.of("Hello", "world"));
    }

    @Test
    void shouldTreatMultipleSeparatorsAsSingleDelimiter() {
        List<String> result = counterPremium.splitIntoWords("Hello,  world! ");
        assertThat(result).isEqualTo(List.of("Hello", "world"));
    }


    // isValidWord
    @Test
    void shouldReturnFalse_whenWordIsNull() {
        assertThat(counterPremium.isValidWord(null)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenWordIsBlank() {
        assertThat(counterPremium.isValidWord("  ")).isFalse();
    }

    @Test
    void shouldReturnFalse_forWordContainingDigits() {
        assertThat(counterPremium.isValidWord("world123")).isFalse();
    }

    @Test
    void shouldReturnTrue_forAlphabeticWord() {
        assertThat(counterPremium.isValidWord("world")).isTrue();
    }

    @Test
    void shouldReturnTrue_forWordContainingProperPunctuation() {
        assertThat(counterPremium.isValidWord("don't")).isTrue();
        assertThat(counterPremium.isValidWord("mother-in-law")).isTrue();
    }

    @Test
    void shouldReturnFalse_forWordContainingIncorrectPunctuation() {
        assertThat(counterPremium.isValidWord("don''t")).isFalse();
        assertThat(counterPremium.isValidWord("don't'")).isFalse();
        assertThat(counterPremium.isValidWord("'don't")).isFalse();
        assertThat(counterPremium.isValidWord("mother--in-law")).isFalse();
        assertThat(counterPremium.isValidWord("mother--in-law-")).isFalse();
        assertThat(counterPremium.isValidWord("-mother--in-law")).isFalse();
    }

    @Test
    void shouldReturnFalse_forWordContainingUnsupportedPunctuation() {
        assertThat(counterPremium.isValidWord("World!")).isFalse();
    }

    // normalizeWord
    @Test
    void shouldTrimTheWord() {
        assertThat(counterPremium.normalizeWord("  hello  ")).isEqualTo("hello");
    }

    @Test
    void shouldKeepWordUnchanged() {
        assertThat(counterPremium.normalizeWord("HeLLo")).isEqualTo("HeLLo");
    }


    // incrementWord
    @Test
    void shouldAddWordWithCountOne_whenWordIsNotPresent() {
        counterPremium.incrementWordCount("hello");
        assertThat(counterPremium.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldIncreaseCount_whenWordAlreadyExists() {
        counterPremium.incrementWordCount("hello");
        counterPremium.incrementWordCount("hello");
        assertThat(counterPremium.getCount("hello")).isEqualTo(2);
    }

    // acceptText
    @Test
    void shouldCountSingleValidWord() {
        counterPremium.acceptText("hello");
        assertThat(counterPremium.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldAddMultipleValidWords() {
        counterPremium.acceptText("hello hello world");
        assertThat(counterPremium.getCount("hello")).isEqualTo(2);
        assertThat(counterPremium.getCount("world")).isEqualTo(1);
    }

    @Test
    void shouldSplitByPunctuationAttachedToWord() {
        counterPremium.acceptText("hello, world!");
        assertThat(counterPremium.getCount("hello")).isEqualTo(1);
        assertThat(counterPremium.getCount("world")).isEqualTo(1);
    }

    @Test
    void shouldBeCaseSensitive() {
        counterPremium.acceptText("Hello HELLO hello");
        assertThat(counterPremium.getCount("Hello")).isEqualTo(1);
        assertThat(counterPremium.getCount("HELLO")).isEqualTo(1);
        assertThat(counterPremium.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldIgnoreWordsWithDigits() {
        counterPremium.acceptText("hello123 world");
        assertThat(counterPremium.getCount("hello123")).isEqualTo(0);
        assertThat(counterPremium.getCount("world")).isEqualTo(1);
    }

    // getCount
    @Test
    void shouldReturnZeroForUnknownWord() {
        assertThat(counterPremium.getCount("unknown")).isEqualTo(0);
    }

}
