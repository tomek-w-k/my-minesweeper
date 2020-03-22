package com.company.tools;

import com.company.MainWidget;
import com.company.constants.ErrorMessages;
import com.company.constants.FieldMarkers;
import com.company.constants.Stylesheets;
import com.company.elements.Field;
import com.company.elements.FieldButton;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.gui.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GameAreaBuilder
{
    private MainWidget mainWidget;
    private GameAreaActionsResolver gameAreaActionsResolver;
    private TimeCounter timeCounter;

    private Integer rowCount, columnCount, minesCount;
    private QGridLayout gridLayout;
    private FieldButton[][] fieldButtons;
    private QLabel[][] fieldLabels;
    private QSignalMapper signalMapper;
    private Integer markedMinesCounter;
    private Integer properlyMarkedMinesCounter;
    private QLabel fieldLabel;
    private Integer fieldSize;
    private FieldButton fieldButton;

    public GameAreaBuilder(QWidget targetWidget)
    {
        mainWidget = (MainWidget)targetWidget;
        gameAreaActionsResolver = new GameAreaActionsResolver(mainWidget);

        this.rowCount = mainWidget.rowCount;
        this.columnCount = mainWidget.columnCount;
        this.minesCount = mainWidget.minesCount;
        this.gridLayout = mainWidget.gridLayout;
        this.fieldButtons = mainWidget.fieldButtons;
        this.fieldLabels = mainWidget.fieldLabels;
        this.signalMapper = mainWidget.signalMapper;
        this.markedMinesCounter = mainWidget.markedMinesCounter;
        this.properlyMarkedMinesCounter = mainWidget.properlyMarkedMinesCounter;
        this.fieldLabel = mainWidget.fieldLabel;
        this.fieldSize = mainWidget.fieldSize;
        this.fieldButton = mainWidget.fieldButton;
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

    private void surround(AdjacentFieldRelativePos adjacentFieldRelativePos, int baseFieldRow, int baseFieldColumn)
    {
        Field adjacentField = gameAreaActionsResolver.adjacentField(adjacentFieldRelativePos, baseFieldRow, baseFieldColumn);
        QLabel adjacentFieldLabel = adjacentField.getLabel();

        if ( !adjacentFieldLabel.text().contentEquals(FieldMarkers.MINE) )
        {
            String text = adjacentFieldLabel.text();
            if ( text.contentEquals(FieldMarkers.EMPTY_FIELD) )
            {
                adjacentFieldLabel.setText(FieldMarkers.DIGIT_ONE);
            }
            else
            {
                try
                {
                    Integer markerContent = Integer.parseInt(text);
                    markerContent++;
                    adjacentFieldLabel.setText( markerContent.toString() );
                }
                catch(NumberFormatException e)
                {
                    System.out.println(ErrorMessages.NUMBERING_SURROUNDING_FIELDS_ERROR + baseFieldRow + ErrorMessages.SPACE + baseFieldColumn);
                }
            }
        }
    }

    public void surroundMinesWithMarkers()
    {
        QLabel fieldLabel;

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                fieldLabel = (QLabel) gridLayout.itemAtPosition(row, column).widget();
                // If given field contains a mine...
                if (fieldLabel.text().contentEquals(FieldMarkers.MINE)) {
                    // ...then surround it with markers as follows:
                    // Left-adjacent field:
                    if (column > 0) {
                        surround(AdjacentFieldRelativePos.LEFT, row, column);
                    }

                    // Right-adjacent field:
                    if (column < columnCount - 1) {
                        surround(AdjacentFieldRelativePos.RIGHT, row, column);
                    }

                    // Up-adjacent three fields:
                    if (row > 0) {
                        // Up-left adjacent field:
                        if (column > 0) {
                            surround(AdjacentFieldRelativePos.UP_LEFT, row, column);
                        }

                        // Up-adjacent field:
                        surround(AdjacentFieldRelativePos.UP, row, column);

                        // Up-right adjacent field:
                        if (column < columnCount - 1) {
                            surround(AdjacentFieldRelativePos.UP_RIGHT, row, column);
                        }
                    }

                    // Down-adjacent three fields:
                    if (row < rowCount - 1) {
                        // Down-left adjacent field:
                        if (column > 0) {
                            surround(AdjacentFieldRelativePos.DOWN_LEFT, row, column);
                        }

                        // Down-adjacent field:
                        surround(AdjacentFieldRelativePos.DOWN, row, column);

                        // Down-right adjacent field:
                        if (column < columnCount - 1) {
                            surround(AdjacentFieldRelativePos.DOWN_RIGHT, row, column);
                        }
                    }
                }
            }
        }
    }
}
