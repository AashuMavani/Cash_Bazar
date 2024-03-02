package com.reward.cashbazar.customviews.imagepuzzle;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

public class Puzzle_Piece extends AppCompatImageView {
    public int xCoord;
    public int yCoord;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;

    public Puzzle_Piece(Context context) {
        super(context);
    }
}