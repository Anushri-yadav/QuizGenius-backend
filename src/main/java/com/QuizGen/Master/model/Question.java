package com.QuizGen.Master.model;

import java.util.List;

public class Question {

    private String question;
    private List<String> options;
    private int correctAnswer;
    private String hint;

    public Question() {
    }

    public Question(String question,
                    List<String> options,
                    int correctAnswer,
                    String hint) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.hint = hint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
