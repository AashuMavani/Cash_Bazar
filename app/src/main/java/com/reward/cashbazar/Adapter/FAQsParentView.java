package com.reward.cashbazar.Adapter;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class FAQsParentView implements Parent<FAQsChildView> {

    // a recipe contains several ingredients
    private String question;
    private List<FAQsChildView> answer;

    public FAQsParentView(String question, List<FAQsChildView> answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public List<FAQsChildView> getChildList() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}