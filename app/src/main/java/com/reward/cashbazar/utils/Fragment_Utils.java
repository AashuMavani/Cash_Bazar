package com.reward.cashbazar.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Fragment_Utils {


    public static void add(FragmentManager fragmentManager, Fragment fragment, int containerId, String tag) {
        try {
            fragmentManager.beginTransaction().add(containerId, fragment, tag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        try {
            fragmentManager.beginTransaction().replace(containerId, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void hide(FragmentManager fragmentManager, Fragment fragment) {
        try {
            fragmentManager.beginTransaction().hide(fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(FragmentManager fragmentManager, Fragment fragment) {
        try {
            fragmentManager.beginTransaction().show(fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
