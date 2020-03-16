package com.company.tools;

import com.company.MainWidget;
import com.company.constants.FieldMarkers;
import com.company.constants.Stylesheets;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GameAreaBuilder
{
    private MainWidget mainWidget;

    private Integer rowCount, columnCount, minesCount;
    private QGridLayout gridLayout;

    public GameAreaBuilder(QWidget targetWidget)
    {
        mainWidget = (MainWidget)targetWidget;

        this.rowCount = mainWidget.rowCount;
        this.columnCount = mainWidget.columnCount;
        this.minesCount = mainWidget.minesCount;
        this.gridLayout = mainWidget.gridLayout;
    }

    public void placeMinesRandomly()
    {
        Random random = new Random(400);
        boolean gameArea[][] = new boolean[rowCount][columnCount];
        LinkedList<Boolean> tempList = new LinkedList<Boolean>();
        boolean minePlaced = false;
        int randomRow, randomColumn;
        QLabel fieldLabel;

        // preliminary drawing of mined places
        for ( int mineCounter = 0; mineCounter < minesCount; mineCounter++ )
        {
            while ( !minePlaced )
            {
                randomRow = random.nextInt(rowCount);
                randomColumn = random.nextInt(columnCount);
                if ( !gameArea[randomRow][randomColumn] )
                {
                    gameArea[randomRow][randomColumn] = true;
                    minePlaced = true;
                }
            }
            minePlaced = false;
        }

        // shuffling - row by row:
        for ( int row = 0; row < rowCount; row++ )
        {
            // take a specified row from gameArea and write it into a tempList...
            for ( int column = 0; column < columnCount; column++ )
            {
                tempList.add( gameArea[row][column] );
            }

            // ... and shuffle the list
            Collections.shuffle( tempList );

            // at the end - write a tempList back into gameArea row
            for ( int column = 0; column < columnCount; column++ )
            {
                gameArea[row][column] = tempList.get(column);
            }

            tempList.clear();
        }

        // shuffling - column by column
        for ( int column = 0; column < columnCount; column++ )
        {
            // take a specified column from gameArea and write it into a tempList...
            for ( int row = 0; row < rowCount; row++ )
            {
                tempList.add( gameArea[row][column] );
            }

            // ... and shuffle the list
            Collections.shuffle( tempList );

            // at the end - write a tempList back into gameArea column
            for ( int row = 0; row < rowCount; row++ )
            {
                gameArea[row][column] = tempList.get(row);
            }

            tempList.clear();
        }

        // place mines on a game area
        for ( int row = 0; row < rowCount; row++ )
        {
            for ( int column = 0; column < columnCount; column++ )
            {
                if ( gameArea[row][column] )
                {
                    fieldLabel = (QLabel) gridLayout.itemAtPosition( row, column ).widget();
                    fieldLabel.setText(FieldMarkers.MINE);
                    fieldLabel.setObjectName(Stylesheets.M_OBJECT);
                    fieldLabel.setStyleSheet(Stylesheets.MINED_FIELD);
                }
            }
        }
    }
}
