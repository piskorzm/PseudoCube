package com.example.pseudoqube.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.example.pseudoqube.elements.Block;
import com.example.pseudoqube.elements.Face;

import java.util.HashMap;
import java.util.Map;

public class PseudoCube extends View {

    public static Paint paint;

    private HashMap<Character, Face> faces;
    private int faceSize;
    private int v_centre, h_centre;

    private VelocityTracker mVelocityTracker = null;
    private Block selectedBlock;
    float velocityX, velocityY;

    public PseudoCube(Context context) {
        super(context);

        init(null);
    }

    public PseudoCube(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public PseudoCube(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        faces = new HashMap<>();

        int width = this.getResources().getDisplayMetrics().widthPixels;
        int height =  this.getResources().getDisplayMetrics().heightPixels;

        int shorterScreenEdge = (width < height ? width : height);

        faceSize = shorterScreenEdge / 3;
        v_centre = height / 2;
        h_centre = width / 2;

        paint = new Paint();
        paint.setAntiAlias(true);

        Face frontFace = new Face('F', faceSize, h_centre - faceSize/2, v_centre - faceSize/2, Color.WHITE);
        Face westFace = new Face('W', faceSize, frontFace.getHorizontalPosition() - faceSize, frontFace.getVerticalPosition(), Color.RED);
        Face northFace = new Face('N', faceSize, frontFace.getHorizontalPosition(), frontFace.getVerticalPosition() - faceSize, Color.BLUE);
        Face eastFace = new Face('E', faceSize, frontFace.getHorizontalPosition() + faceSize, frontFace.getVerticalPosition(), Color.rgb(255, 150, 0));
        Face southFace = new Face('S', faceSize, frontFace.getHorizontalPosition(), frontFace.getVerticalPosition() + faceSize, Color.GREEN);

        faces.put(frontFace.getCharId(), frontFace);
        faces.put(westFace.getCharId(), westFace);
        faces.put(northFace.getCharId(), northFace);
        faces.put(eastFace.getCharId(), eastFace);
        faces.put(southFace.getCharId(), southFace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.rgb(15,15,30));

        //canvas.rotate(45, h_centre, v_centre);

        for(Map.Entry<Character, Face> entry : faces.entrySet()) {
            entry.getValue().draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {
            case MotionEvent.ACTION_DOWN:

                if(mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    mVelocityTracker.clear();
                }
                if (selectedBlock == null) {
                    Block block = getTouchedRectangle(event.getX(), event.getY());
                    selectedBlock = block;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                velocityX = mVelocityTracker.getXVelocity(pointerId);
                velocityY = mVelocityTracker.getYVelocity(pointerId);

                if (selectedBlock == null) {
                    Block block = getTouchedRectangle(event.getX(), event.getY());
                    selectedBlock = block;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:



                performAction();

                selectedBlock = null;
                velocityX = 0;
                velocityY = 0;
                mVelocityTracker.clear();
                break;
        }

        return true;
    }

    protected void performAction() {
        if (selectedBlock != null && (velocityX != 0 || velocityY !=0)) {
            char direction = getDirection(velocityX, velocityY);

            if ((direction == 'U' || direction == 'D') && (selectedBlock.getFaceCharId() == 'F' || selectedBlock.getFaceCharId() == 'N' || selectedBlock.getFaceCharId() == 'S')) {
                shiftCol(selectedBlock.getColIndex(), direction);
            }
            else if ((direction == 'L' || direction == 'R') && (selectedBlock.getFaceCharId() == 'F' || selectedBlock.getFaceCharId() == 'W' || selectedBlock.getFaceCharId() == 'E')) {
                shiftRow(selectedBlock.getRowIndex(), direction);
            }
            else if ((direction == 'L' || direction == 'R') && selectedBlock.getFaceCharId() == 'N') {
                rotateFloor(2 - selectedBlock.getRowIndex(), direction);
            }
            else if (selectedBlock.getFaceCharId() == 'S') {
                if (direction == 'L') {
                    rotateFloor(selectedBlock.getRowIndex(), 'R');
                }
                else if (direction == 'R') {
                    rotateFloor(selectedBlock.getRowIndex(), 'L');
                }
            }
            else if (selectedBlock.getFaceCharId() == 'W') {
                if (direction == 'U') {
                    rotateFloor(2 - selectedBlock.getColIndex(), 'R');
                }
                else if (direction == 'D') {
                    rotateFloor(2 - selectedBlock.getColIndex(), 'L');
                }
            }
            else if (selectedBlock.getFaceCharId() == 'E') {
                if (direction == 'U') {
                    rotateFloor(selectedBlock.getColIndex(), 'L');
                }
                else if (direction == 'D') {
                    rotateFloor(selectedBlock.getColIndex(), 'R');
                }
            }
        }
        invalidate();
    }

    protected char getDirection(float x, float y) {
        char direction = 'I';
        if(Math.abs(velocityX) < Math.abs(velocityY)) {
            if(velocityY < 0) {
                direction = 'U';
            }
            else {
                direction = 'D';
            }
        } else {
            if(velocityX < 0) {
                direction = 'L';
            }
            else {
                direction = 'R';
            }
        }
        return direction;
    }


    protected Block getTouchedRectangle(float x, float y) {
        for(Map.Entry<Character, Face> entry : faces.entrySet()) {
            Face face = entry.getValue();
            Block[][] blocks = face.getBlocks();

            for(int i = 0; i < blocks.length; i++) {
                for(int j = 0; j < blocks[i].length; j ++) {
                    Block block = blocks [i][j];

                    if (block.getRect().contains(x, y)) {
                        return block;
                    }
                }
            }
        }
        return null;
    }

    protected void shiftRow(int row, char direction) {
        if (direction == 'R') {
            int[] temporary = faces.get('E').getRowColors(row).clone();
            faces.get('E').setRowColors(faces.get('F').getRowColors(row), row);
            faces.get('F').setRowColors(faces.get('W').getRowColors(row), row);
            faces.get('W').setRowColors(temporary, row);
        }
        else if (direction == 'L') {
            int[] temporary = faces.get('W').getRowColors(row).clone();
            faces.get('W').setRowColors(faces.get('F').getRowColors(row), row);
            faces.get('F').setRowColors(faces.get('E').getRowColors(row), row);
            faces.get('E').setRowColors(temporary, row);
        }
    }

    protected void shiftCol(int col, char direction) {
        if (direction == 'U') {
            int[] temporary = faces.get('N').getColColors(col).clone();
            faces.get('N').setColColors(faces.get('F').getColColors(col), col);
            faces.get('F').setColColors(faces.get('S').getColColors(col), col);
            faces.get('S').setColColors(temporary, col);
        }
        else if (direction == 'D') {
            int[] temporary = faces.get('S').getColColors(col).clone();
            faces.get('S').setColColors(faces.get('F').getColColors(col), col);
            faces.get('F').setColColors(faces.get('N').getColColors(col), col);
            faces.get('N').setColColors(temporary, col);
        }
    }

    protected void rotateFloor(int floor, char direction) {
        if (floor == 0) {
            rotateFrontFace(direction);
        }
        if (direction == 'R') {
            int[] temporary = faces.get('N').getRowColors(2 - floor).clone();
            faces.get('N').setRowColors(inverted(faces.get('W').getColColors(2 - floor)), 2 - floor);
            faces.get('W').setColColors(faces.get('S').getRowColors(floor), 2 - floor);
            faces.get('S').setRowColors(inverted(faces.get('E').getColColors(floor)), floor);
            faces.get('E').setColColors(temporary, floor);
        }
        if (direction == 'L') {
            int[] temporary = inverted(faces.get('N').getRowColors(2 - floor).clone());
            faces.get('N').setRowColors(faces.get('E').getColColors(floor), 2 - floor);
            faces.get('E').setColColors(inverted(faces.get('S').getRowColors(floor)), floor);
            faces.get('S').setRowColors(faces.get('W').getColColors(2 - floor), floor);
            faces.get('W').setColColors(temporary, 2 - floor);
        }
    }

    protected void rotateFrontFace(char direction) {
        Face frontFace = faces.get('F');
        int tempMiddleLeftColor = frontFace.getBlockColor(1, 0);
        int tempMiddleRightColor = frontFace.getBlockColor(1, 2);
        int[] tempBottomRow = frontFace.getRowColors(2).clone();
        if (direction == 'R') {
            frontFace.setColColors(frontFace.getRowColors(0), 2);
            frontFace.setColColors(tempBottomRow, 0);
            frontFace.setBlockColor(tempMiddleLeftColor, 0, 1);
            frontFace.setBlockColor(tempMiddleRightColor, 2, 1);
        }
        if (direction == 'L') {
            frontFace.setColColors(inverted(frontFace.getRowColors(0)), 0);
            frontFace.setColColors(inverted(tempBottomRow), 2);
            frontFace.setBlockColor(tempMiddleLeftColor, 2, 1);
            frontFace.setBlockColor(tempMiddleRightColor, 0, 1);
        }
    }

    public int[] inverted(int[] array) {
        int[] invertedArray = array.clone();
        for (int i = 0; i < array.length / 2; i++) {
            invertedArray[i] = array[array.length - 1 - i];
            invertedArray[array.length - 1 - i] = array[i];
        }
        return invertedArray;
    }
}

