package com.reward.cashbazar.Adapter;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

import com.reward.cashbazar.Async.Models.Menu_Listenu;

public class SideDrawerItemParentView implements Parent<SideDrawerItemChildView> {

    // a recipe contains several ingredients
    private Menu_Listenu menuListItem;
    private List<SideDrawerItemChildView> answer;

    public SideDrawerItemParentView(Menu_Listenu question, List<SideDrawerItemChildView> answer) {
        this.menuListItem = question;
        this.answer = answer;
    }

    @Override
    public List<SideDrawerItemChildView> getChildList() {
        return answer;
    }

    public Menu_Listenu getMenu() {
        return menuListItem;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
