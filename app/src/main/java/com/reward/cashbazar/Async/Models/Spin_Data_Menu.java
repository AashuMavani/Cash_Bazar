package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Spin_Data_Menu {

    @SerializedName("block_bg")
    private String blockBg;
    @SerializedName("block_icon")
    private String blockIcon;
    @SerializedName("block_id")
    private String blockId;
    @SerializedName("block_points")
    private String blockPoints;

    @SerializedName("block_text_color")
    private String blockTextColor;

    public String getBlockBg() {
        return blockBg;
    }

    public void setBlockBg(String blockBg) {
        this.blockBg = blockBg;
    }

    public String getBlockIcon() {
        return blockIcon;
    }

    public void setBlockIcon(String blockIcon) {
        this.blockIcon = blockIcon;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockPoints() {
        return blockPoints;
    }

    public void setBlockPoints(String blockPoints) {
        this.blockPoints = blockPoints;
    }

    public String getBlockTextColor() {
        return blockTextColor;
    }

    public void setBlockTextColor(String blockTextColor) {
        this.blockTextColor = blockTextColor;
    }
}

