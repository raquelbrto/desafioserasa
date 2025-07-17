package com.dev.brito.desafioserasa.enums;

import com.dev.brito.desafioserasa.exceptions.InvalidScoreException;

public enum ScoreDescription {
    INSUFFICIENT(0, 200, "Insuficiente"),
    UNACCEPTABLE(201, 500, "Inaceitável"),
    ACCEPTABLE(501, 700, "Aceitável"),
    RECOMMENDED(701, 1000, "Recomendável");

    private final int min;
    private final int max;
    private final String description;

    ScoreDescription(int min, int max, String description) {
        this.min = min;
        this.max = max;
        this.description = description;
    }

    public static String fromScore(int score) {
        for (ScoreDescription sd : values()) {
            if (score >= sd.min && score <= sd.max) {
                return sd.description;
            }
        }

        throw new InvalidScoreException();
    }
}
