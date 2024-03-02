package com.reward.cashbazar.Adapter;

import com.reward.cashbazar.Async.Models.Sub_ItemList_Item;

public class SideDrawerItemChildView {

    // a recipe contains several ingredients
    private Sub_ItemList_Item subMenu;

    public SideDrawerItemChildView(Sub_ItemList_Item name) {
        subMenu = name;
    }

    public Sub_ItemList_Item getMenu() {
        return subMenu;
    }
}
