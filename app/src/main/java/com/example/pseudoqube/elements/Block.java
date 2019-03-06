package com.example.pseudoqube.elements;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.pseudoqube.views.Consts;
import com.example.pseudoqube.views.PseudoCube;

public class Block implements Cloneable {
    private int x, y, size, rowIndex, colIndex;
    private int color;
    private RectF rect;
    char faceCharId;

    public Block(int size,  int x, int y, int color, char faceCharId, int rowIndex, int colIndex) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.faceCharId = faceCharId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        rect = new RectF(x + Consts.BLOCK_PADDING, y + Consts.BLOCK_PADDING, x + size - Consts.BLOCK_PADDING, y + size - Consts.BLOCK_PADDING);
    }

    public Block(Block block) {
        x = block.getX();
        y = block.getY();
        size = block.getSize();
        color = block.getColor();
        faceCharId = block.getFaceCharId();
        rowIndex = block.getRowIndex();
        colIndex = block.getColIndex();
        rect = new RectF(x + Consts.BLOCK_PADDING, y + Consts.BLOCK_PADDING, x + size - Consts.BLOCK_PADDING, y + size - Consts.BLOCK_PADDING);
    }

    public void draw(Canvas canvas) {
        if (!PseudoCube.animatedBlocks.contains(this) || PseudoCube.stationaryBlocksDrawn) {
            PseudoCube.paint.setColor(color);
            canvas.drawRoundRect(rect, Consts.BLOCK_RADIUS, Consts.BLOCK_RADIUS, PseudoCube.paint);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int newColor) {
        color = newColor;
    }

    public char getFaceCharId() {
        return faceCharId;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public RectF getRect() {
        return rect;
    }
}
