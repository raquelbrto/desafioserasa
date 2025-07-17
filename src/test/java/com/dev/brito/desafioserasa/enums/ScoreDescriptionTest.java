package com.dev.brito.desafioserasa.enums;

import com.dev.brito.desafioserasa.exceptions.InvalidScoreException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreDescriptionTest {

    @Test
    void shouldReturnInsufficientDescription() {
        assertEquals("Insuficiente", ScoreDescription.fromScore(0));
        assertEquals("Insuficiente", ScoreDescription.fromScore(100));
        assertEquals("Insuficiente", ScoreDescription.fromScore(200));
    }

    @Test
    void shouldReturnUnacceptableDescription() {
        assertEquals("Inaceitável", ScoreDescription.fromScore(201));
        assertEquals("Inaceitável", ScoreDescription.fromScore(300));
        assertEquals("Inaceitável", ScoreDescription.fromScore(500));
    }

    @Test
    void shouldReturnAcceptableDescription() {
        assertEquals("Aceitável", ScoreDescription.fromScore(501));
        assertEquals("Aceitável", ScoreDescription.fromScore(600));
        assertEquals("Aceitável", ScoreDescription.fromScore(700));
    }

    @Test
    void shouldReturnRecommendedDescription() {
        assertEquals("Recomendável", ScoreDescription.fromScore(701));
        assertEquals("Recomendável", ScoreDescription.fromScore(850));
        assertEquals("Recomendável", ScoreDescription.fromScore(1000));
    }

    @Test
    void shouldReturnInvalidDescriptionWhenOutOfBounds() {
        assertThrows(InvalidScoreException.class, () -> ScoreDescription.fromScore(-1));
        assertThrows(InvalidScoreException.class, () -> ScoreDescription.fromScore(1001));
    }
}