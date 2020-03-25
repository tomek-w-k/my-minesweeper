package com.company.tools;

import com.company.Main;
import com.company.constants.ErrorMessages;
import com.company.constants.FieldMarkers;
import com.company.constants.MiscParams;
import com.company.constants.Stylesheets;
import com.company.elements.Field;
import com.company.elements.GameArea;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GameAreaBuilder extends QObject
{
    // GAME AREA ELEMENTS

    // Grid layout - holds all Fields
    public QGridLayout gridLayout;

    // Fields
    private Field[][] fields;

    // Number of rows, columns and mines for entire area:
    public Integer rowCount, columnCount, minesCount;

    // TEMPORARY (to include into a Field class): Size of fields:
    public Integer fieldSize = 0;

    // Reference to the game area
    private GameArea gameArea;

    // Determines an adjacent field for a given field - used in marker's surrounding algorithm
    private AdjacentFieldResolver adjacentFieldResolver;

    // Handles mouse clicking actions on field buttons
    private GameAreaActionsResolver gameAreaActionsResolver;

    private TimeCounter timeCounter;


    public QGridLayout getGridLayout() { return gridLayout; }
    public void setGridLayout(QGridLayout gridLayout) { this.gridLayout = gridLayout; }

    public Integer getRowCount() { return rowCount; }
    public void setRowCount(Integer lRowCount) { this.rowCount = lRowCount; }

    public Integer getColumnCount() { return columnCount; }
    public void setColumnCount(Integer lColumnCount) {this.columnCount = lColumnCount; }

    public Integer getMinesCount() { return minesCount; }
    public void setMinesCount(Integer lMinesCount) { this.minesCount = lMinesCount; }

    public Integer getFieldSize() { return fieldSize; }
    public void setFieldSize(Integer lFieldSize) { this.fieldSize = lFieldSize; }

    public GameAreaBuilder(GameArea gameArea)
    {
        this.gameArea = gameArea;
    }

    public void createNewGame()
    {
        // First it's necessary to remove an old game area and its elements
        // Delete old fields:
        if ( fields != null )
        {
            for ( Field fieldRow[] : fields )
            {
                for ( Field field : fieldRow )
                {
                    if ( field != null )
                    {
                        field.getLabel().dispose();
                        field.getFieldButton().dispose();
                    }
                }
            }
        }

        // Delete an old grid layout:
        if ( gridLayout != null ) gridLayout.dispose();

        // Create an array for fields:
        fields = new Field[rowCount][columnCount];

        // Create a new grid layout:
        gridLayout = new QGridLayout();

        gridLayout.setHorizontalSpacing(0);
        gridLayout.setVerticalSpacing(0);

        gameAreaActionsResolver = new GameAreaActionsResolver(gameArea, gridLayout, fields);

        // Fill a game area with fields
        for ( int row = 0; row < rowCount; row++ )
        {
            for ( int column = 0; column < columnCount; column++ )
            {
                var field = new Field(gameArea);

                field.getLabel().setMinimumSize( new QSize(fieldSize, fieldSize ) );
                field.getLabel().setMaximumSize( new QSize(fieldSize, fieldSize) );
                field.getLabel().setAlignment(Qt.AlignmentFlag.AlignCenter);
                field.getLabel().setFrameShape(QFrame.Shape.Box);
                field.getLabel().setObjectName(Stylesheets.M_OBJECT);
                field.getLabel().setStyleSheet(Stylesheets.EMPTY_UNCOVERED_FIELD);
                gridLayout.addWidget( field.getLabel(), row, column );

                field.getFieldButton().setMinimumSize(new QSize(fieldSize, fieldSize));
                field.getFieldButton().setMaximumSize(new QSize(fieldSize, fieldSize));
                field.getFieldButton().clicked.connect( gameAreaActionsResolver, "handleMouseClicked()" );
                field.getFieldButton().rightClicked.connect( gameAreaActionsResolver, "handleMouseRightClicked()" );
                gridLayout.addWidget( field.getFieldButton(), row, column );

                fields[row][column] = field;
            }
        }

        if ( gameArea.getTimer() != null ) gameArea.getTimer().disconnect();

        timeCounter = new TimeCounter(gameArea);
        timeCounter.countTime();

        placeMinesRandomly();

        adjacentFieldResolver = new AdjacentFieldResolver(fields);
        surroundMinesWithMarkers();

        gameArea.setLayout( gridLayout );

        gameArea.getMainWindow().updateStatusBarMessage(tr("Mines: ") + minesCount);

        // - - - Setting a size of main window and make it fixed - - -
        Main mainWindow = gameArea.getMainWindow();
        QSize mainWindowSize;

        // for Linux
        if ( System.getProperty("os.name").equals("Linux") )
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscParams.LINUX_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscParams.LINUX_MAIN_WINDOW_HEIGHT_EXCESS  );
        }

        // for Windows-like
        if ( System.getProperty("os.name").contains("Win") )
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscParams.WINDOWS_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscParams.WINDOWS_MAIN_WINDOW_HEIGHT_EXCESS  );
        }
        else // other os
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscParams.LINUX_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscParams.LINUX_MAIN_WINDOW_HEIGHT_EXCESS  );
        }

        mainWindow.setFixedSize( mainWindowSize );
        mainWindow.statusBar.showMessage("");
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
        Field adjacentField = adjacentFieldResolver.adjacentField(adjacentFieldRelativePos, baseFieldRow, baseFieldColumn);
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
