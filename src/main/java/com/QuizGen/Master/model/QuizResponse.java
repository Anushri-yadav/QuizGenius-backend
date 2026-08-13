package com.QuizGen.Master.model;







import java.util.List;

public class QuizResponse {

    private String title;
    private List<Question> questions;

    public QuizResponse() {
    }

    public QuizResponse(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}