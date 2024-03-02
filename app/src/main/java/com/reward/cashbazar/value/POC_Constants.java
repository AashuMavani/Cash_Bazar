package com.reward.cashbazar.value;

import android.content.Intent;

public class POC_Constants {


    public static String WATCH_WEBSITE_RESULT = "WATCH_WEBSITE_RESULT";
    public static String TASK_TYPE_HIGHEST_PAYING = "1";
    public static int SHOW_RATE_US_POPUP_COUNT = 3;
    public static String APP_OPEN_ADD_DISMISSED = "APP_OPEN_ADD_DISMISSED";
    public static String APP_OPEN_ADD_LOADED = "APP_OPEN_ADD_LOADED";
    public static final String DAILY_TARGET_RESULT = "DAILY_TARGET_RESULT";
    public static final String QUICK_TASK_RESULT = "QUICK_TASK_RESULT";
    public static final String LIVE_MILESTONE_RESULT = "LIVE_MILESTONE_RESULT";
    public static int ratePopupHourDifference = 24;
    public static int ratePopupDelay = 60000;// 1 minute

    public static String msg_Service_Error = "Oops! This service is taking too much time to respond. please check your internet connection & try again.";
    public static String STATUS_ERROR = "0";
    public static String STATUS_SUCCESS = "1";
    public static String STATUS_LOGOUT = "5";
    public static int countDownTimerCount = 5;
    public static String GOOGLE_AD = "1";
    public static String APPlOVIN_AD = "1";
    public static String CUSTOM_AD = "2";

    public static String APPLOVIN_INTERSTITIAL = "1";
    public static String APPLOVIN_REWARD = "2";
    public static String whatsappPackageName = "com.whatsapp";
    public static String telegramPackageName = "org.telegram.messenger";

    public interface HistoryType {
        String SPIN = "6";
        String WITHDRAW_HISTORY = "17";
        String TASK = "11";
        String DAILY_LOGIN = "15";
        String ALL = "0";
        String REFER_POINT = "13";
        String REFER_USERS = "16";
        String WATCH_VIDEO = "18";
        String GIVE_AWAY = "19";
//        String CPXSurvey = "29";
        String SCRATCH_CARD = "20";
        String LUCKY_NUMBER_MY_CONTEST = "1";
        String LUCKY_NUMBER_ALL_CONTEST = "2";
        String IMAGE_PUZZLE = "21";
        String DAILY_REWARD = "22";
        String Count = "23";

        String Dice_Game = "24";

        String Cards = "25";
        String Color = "26";
        String ALPHABET_GAME = "27";
        String QUIZ_MY_CONTEST = "1";
        String QUIZ_ALL_CONTEST = "2";
        String MILESTONES = "28";
        String MINE_SWEEPER = "30";
        String LIVE_MILESTONES = "44";
        String TYPE_TEXT_TYPING = "31";
        String WORD_SORTING = "33";
    }

    public static String TASK_TYPE_ALL = "0";

    public interface HomeDataType {
        String ICON_LIST = "iconlist";
        String SINGLE_SLIDER = "singleslider";
        String TWO_GRID = "twogrid"; // title, image, points
        String SINGLE_BIG_TASK = "singleBigTask";
        String NATIVE_AD = "nativeAd";
        String EARN_GRID = "earnGrid";
        String TASK_LIST = "taskList";
        String GRID = "grid";
//        String CPXSurvey = "CPXSurvey";
        String HORIZONTAL_TASK = "horizontaltask";

        String LIVE_CONTEST = "live_contest";
        String LIVE_MILESTONES = "live_milestones";
        String DAILY_TARGET = "daily_target";
        String QUICK_TASK = "Quicktask";
    }

    public interface RewardDataType {
        String DAILY_BONUS = "dailyBonus";
        String DAILY_SPIN = "dailySpin";
        String NATIVE_AD = "nativeAd";
        String SINGLE_SLIDER = "singleslider";
//        String CPXSurvey = "CPXSurvey";
        String GRID = "grid";
        String WATCH_VIDEO = "watchVideo";
        String SCRATCH_CARD = "scratchCard";
        String GIVE_AWAY = "giveAway";
        String LUCKY_NUMBER = "luckyNumber";
        String IMAGE_PUZZLE = "imagePuzzle";
        String QUIZ = "quiz";
        String ADJOE = "adjoe";
        String DAILY_REWARDS_CHALLENGE = "daily_reward_challenge";
        String LIVE_MILESTONES = "live_milestones";
        String QUICK_TASK = "Quicktask";
        String DAILY_TARGET = "daily_target";
        String LIVE_CONTEST = "live_contest";
    }

    public static final String HIGHSCORE = "highscore";

    public static final String CURRENT_SCORE = "currentscore";
    public static final String CURRENT_BEST = "currentbest";

    public static final float LIGHT_EASY = 0.3f;
    public static final float LIGHT_MEDIUM = 0.15f;
    public static final float LIGHT_HARD = 0.09f;

    public static final int BLOCK_2 = 2;
    public static final int BLOCK_3 = 3;
    public static final int BLOCK_4 = 4;
    public static final int BLOCK_5 = 5;
    public static final int BLOCK_6 = 6;
    public static final int BLOCK_7 = 7;
    public static final int BLOCK_8 = 8;

    public static final String[] colors = new String[]{"#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e", "#f1c40f", "#e67e22", "#e74c3c", "#7f8c8d", "#523D1F", "#D33257", "#45362E", "#E3000E", "#60646D", "#AAB69B", "#8870FF", "#3E4651", "#EB9532", "#FACA9B", "#D8335B", "#897FBA", "#BF4ACC", "#710301", "#8F6910", "#FFF457"};

    public static native String getAppURL();

    public static native String getMIV();

    public static native String getMKEY();

    public static native String getUSERTOKEN();
}
