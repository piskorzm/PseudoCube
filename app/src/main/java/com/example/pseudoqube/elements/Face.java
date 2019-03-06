package com.example.pseudoqube.elements;

import android.graphics.Canvas;

import com.example.pseudoqube.views.Consts;

public class Face {

    private int x, y, size;
    private int color;
    private Character charId;
    private Block[][] blocks = new Block[3][3];

    public Face(Character charId, int size,  int x, int y, int color) {
        this.charId = charId;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;

        int blockSize = (size - Consts.FACE_PADDING * 2) / 3;
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j ++) {
                int blockX = x + Consts.FACE_PADDING + j * blockSize;
                int blockY = y + Consts.FACE_PADDING + i * blockSize;
                blocks[i][j] = new Block(blockSize, blockX, blockY, color, charId, i, j);
            }
        }
    }

    public Character getCharId() {
        return this.charId;
    }

    public int getHorizontalPosition() {
        return this.x;
    }

    public int getVerticalPosition() {
        return this.y;
    }
    
    public int getSize() {
        return size;
    }

    public void draw(Canvas canvas) {
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j ++) {
                blocks[i][j].draw(canvas);
            }
        }
    }

    public Block getBlock(int rowIndex, int colIndex) {
        return blocks[rowIndex][colIndex];
    }

    public void setBlock(Block newBlock, int rowIndex, int colIndex) {
        blocks[rowIndex][colIndex] = newBlock;
    }

    public int getBlockColor(int rowIndex, int colIndex) {
        return blocks[rowIndex][colIndex].getColor();
    }

    public void setBlockColor(int newColor, int rowIndex, int colIndex) {
        blocks[rowIndex][colIndex].setColor(newColor);
    }

    public Block[] getRow(int rowIndex) {
        return blocks[rowIndex];
    }

    public void setRow(Block[] newRow, int rowIndex) {
        blocks[rowIndex] = newRow;
    }

    public int[] getRowColors(int rowIndex) {
        Block[] row = getRow(rowIndex);
        int[] colors = new int[row.length] ;
        for(int i = 0; i < row.length; i++) {
            colors[i] = row[i].getColor();
        }
        return colors;
    }

    public void setRowColors(int[] colors, int rowIndex) {
        Block[] row = getRow(rowIndex);
        for(int i = 0; i < row.length; i++) {
            row[i].setColor(colors[i]);
        }
    }

    public Block[] getCol(int colIndex) {
        Block[] col = new Block[blocks.length];
        for(int i = 0; i < blocks.length; i++) {
            col[i] = blocks[i][colIndex];
        }
        return col;
    }

    public void setCol(Block[] newCol,int colIndex) {
        Block[] col = getCol(colIndex);
        for(int i = 0; i < col.length; i++) {
            col[i] = newCol[i];
        }
    }

    public int[] getColColors(int colIndex) {
        Block[] col = getCol(colIndex);
        int[] colors = new int[col.length] ;
        for(int i = 0; i < col.length; i++) {
            colors[i] = col[i].getColor();
        }
        return colors;
    }

    public void setColColors(int[] colors, int colIndex) {
        Block[] col = getCol(colIndex);
        for(int i = 0; i < col.length; i++) {
            col[i].setColor(colors[i]);
        }
    }

    public Block[][] getBlocks() {
        return blocks;
    }
}
