package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class CounterBasicTest {

    private CounterBasic counterBasic;

    @BeforeEach
    void setUp() {
        counterBasic = new CounterBasic();
    }

    @Test
    void shouldCountSingleWord() {
        counterBasic.acceptText("hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(1);
    }

    @Test
    void shouldBeCaseInsensitive() {
        counterBasic.acceptText("Hello HELLO hello");
        assertThat(counterBasic.getCount("hello")).isEqualTo(3);
    }

    @Test
    void shouldReturnZeroForUnknownWord() {
        assertThat(counterBasic.getCount("unknown")).isEqualTo(0);
    }

    @Test
    void shouldAcceptWordsWithEnglishLettersOnly() {
        counterBasic.acceptText("Hello! 123");
        assertThat(counterBasic.getCount("Hello!")).isEqualTo(0);
        assertThat(counterBasic.getCount("hello!")).isEqualTo(0);
        assertThat(counterBasic.getCount("hello")).isEqualTo(0);
        assertThat(counterBasic.getCount("123")).isEqualTo(0);
    }
}
