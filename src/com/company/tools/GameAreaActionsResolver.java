package com.company.tools;

import com.company.Main;
import com.company.MainWidget;
import com.company.constants.FieldMarkers;
import com.company.constants.Stylesheets;
import com.company.elements.Field;
import com.company.elements.MButton;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.core.*;

import java.util.List;

import static com.company.converters.NumberParser.isInteger;


public class GameAreaActionsResolver extends QObject
{
    private MainWidget mainWidget;

    private QGridLayout gridLayout;
    private Integer clickedFieldButtonRow, clickedFieldButtonColumn;
    private Integer rowCount, columnCount, minesCount;
    private MButton[][] fieldButtons;
    private List<QLabel> tempFieldLabelList;
    private int i;
    private QLabel[][] fieldLabels;
    private MButton button;
    private QLabel clickedFieldLabel;
    private QTimer timer;
    private Integer markedMinesCounter = 0;
    private Integer properlyMarkedMinesCounter = 0;
    private Boolean enableTimeCounting;
    public Boolean saveBestResult;


    public GameAreaActionsResolver(QWidget targetWidget)
    {
        this.mainWidget = (MainWidget)targetWidget;

        this.gridLayout = mainWidget.gridLayout;
        this.clickedFieldButtonRow = mainWidget.clickedFieldButtonRow;
        this.clickedFieldButtonColumn = mainWidget.clickedFieldButtonColumn;
        this.rowCount = mainWidget.rowCount;
        this.columnCount = mainWidget.columnCount;
        this.minesCount = mainWidget.minesCount;
        this.fieldButtons = mainWidget.fieldButtons;
        this.tempFieldLabelList = mainWidget.tempFieldLabelList;
        this.i = mainWidget.i;
        this.fieldLabels = mainWidget.fieldLabels;
        this.button = mainWidget.button;
        this.clickedFieldLabel = mainWidget.clickedFieldLabel;
        this.timer = mainWidget.timer;
        this.markedMinesCounter = mainWidget.markedMinesCounter;
        this.properlyMarkedMinesCounter = mainWidget.properlyMarkedMinesCounter;
        this.enableTimeCounting = mainWidget.enableTimeCounting;
        this.saveBestResult = mainWidget.saveBestResult;
    }

    public void setClickedFieldButtonRow(Integer clickedFieldButtonRow)
    {
        this.clickedFieldButtonRow = clickedFieldButtonRow;
    }

    public void setClickedFieldButtonColumn(Integer clickedFieldButtonColumn)
    {
        this.clickedFieldButtonColumn = clickedFieldButtonColumn;
    }

    /*
        Returns a specified adjacent field to the given base field described by its coordinates (base row, base column)
     */
    public Field adjacentField(AdjacentFieldRelativePos adjacentFieldRelativePos, Integer baseFieldRow, Integer baseFieldColumn)
    {
        Field field = new Field();
        QLabel adjacentFieldLabel = null;
        QPushButton adjacentFieldButton = null;

        switch (adjacentFieldRelativePos)
        {
            case UP:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow - 1, baseFieldColumn).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow - 1][baseFieldColumn];
                break;
            case DOWN:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow + 1, baseFieldColumn).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow + 1][baseFieldColumn];
                break;
            case LEFT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow, baseFieldColumn - 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow][baseFieldColumn - 1];
                break;
            case RIGHT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow, baseFieldColumn + 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow][baseFieldColumn + 1];
                break;
            case UP_LEFT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow - 1, baseFieldColumn - 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow - 1][baseFieldColumn - 1];
                break;
            case UP_RIGHT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow - 1, baseFieldColumn + 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow - 1][baseFieldColumn + 1];
                break;
            case DOWN_LEFT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow + 1, baseFieldColumn - 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow + 1][baseFieldColumn - 1];
                break;
            case DOWN_RIGHT:
                adjacentFieldLabel = (QLabel) gridLayout.itemAtPosition(baseFieldRow + 1, baseFieldColumn + 1).widget();
                adjacentFieldButton = fieldButtons[baseFieldRow + 1][baseFieldColumn + 1];
                break;
        }
        field.setLabel( adjacentFieldLabel );
        field.setPushButton( adjacentFieldButton );

        return field;
    }

    /*
        This function checks if the given adjacent field is empty or has a digit marker. If so - uncovers the field.
     */
    private void uncover(AdjacentFieldRelativePos adjacentFieldRelativePos)
    {
        Field adjacentField = adjacentField(adjacentFieldRelativePos, clickedFieldButtonRow, clickedFieldButtonColumn);
        QLabel adjacentFieldLabel = adjacentField.getLabel();
        QPushButton adjacentFieldButton = adjacentField.getPushButton();

        if ( adjacentFieldLabel.text().contentEquals("") || isInteger( adjacentFieldLabel.text() ) )
        {
            if ( !adjacentFieldButton.text().equals(FieldMarkers.FLAG) )
            {
                adjacentFieldButton.hide();
            }
        }
        if ( adjacentFieldLabel.text().contentEquals("") && !tempFieldLabelList.contains( adjacentFieldLabel ))
        {
            tempFieldLabelList.add( adjacentFieldLabel );
        }
    }

    /*
        This function is called when an empty field has been clicked.
        Uncovers all empty and number-marked fields adjacent to clicked empty field.
     */
    public void uncoverAdjacentFields()
    {
        // Statements below check all fields adjacent to a clicked one in search of an empty field or a number-marked field to hide a button covering it
        // Left-adjacent field:
        if ( clickedFieldButtonColumn > 0 ) // left-side boundary condition
        {
            uncover(AdjacentFieldRelativePos.LEFT);
        }

        // Right-adjacent field:
        if ( clickedFieldButtonColumn < columnCount-1 ) // right-side boundary condition
        {
            uncover(AdjacentFieldRelativePos.RIGHT);
        }

        // Up-adjacent three fields:
        if ( clickedFieldButtonRow > 0) // up-side boundary condition
        {
            // check up-left-adjacent field label:
            if ( clickedFieldButtonColumn > 0 ) // up-left-side boundary conditon
            {
                uncover(AdjacentFieldRelativePos.UP_LEFT);
            }

            // check up-adjacent field label:
            uncover(AdjacentFieldRelativePos.UP);

            // check up-right-adjacent field label:
            if ( clickedFieldButtonColumn < columnCount-1 ) // up-right-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.UP_RIGHT);
            }
        }

        // down-adjacent three fields:
        if ( clickedFieldButtonRow < rowCount-1 ) // down-side boundary condition
        {
            // check down-left-adjacent field label:
            if ( clickedFieldButtonColumn > 0 ) // down-left-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.DOWN_LEFT);
            }

            // check down-adjacent field label:
            uncover(AdjacentFieldRelativePos.DOWN);

            // check down-right-adjacent field label:
            if ( clickedFieldButtonColumn < columnCount-1 ) // down-right-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.DOWN_RIGHT);
            }
        }

        if ( i < tempFieldLabelList.size() )
        {
            QLabel label = tempFieldLabelList.get(i);
            clickedFieldButtonRow = gridLayout.getItemPosition( gridLayout.indexOf( label ) ).row;
            clickedFieldButtonColumn = gridLayout.getItemPosition( gridLayout.indexOf( label ) ).column;
            i++;

            uncoverAdjacentFields();
        }
    }

    public void handleMouseClicked()
    {
        i = 0;

        // Clicked field button:
        button = (MButton) signalSender();

        // Clicked field button coordinates on a gridlayout:
        clickedFieldButtonRow = gridLayout.getItemPosition( gridLayout.indexOf( button ) ).row;
        clickedFieldButtonColumn = gridLayout.getItemPosition( gridLayout.indexOf( button ) ).column;

        // Label under clicked button:
        clickedFieldLabel = (QLabel) gridLayout.itemAtPosition(clickedFieldButtonRow, clickedFieldButtonColumn).widget();

        // If clicked field is marked then do nothing:
        if ( button.text().equals(FieldMarkers.FLAG) )
        {
            return;
        }

        // If clicked field label is an empty field:
        if ( clickedFieldLabel.text().contentEquals(""))
        {
            button.hide();
            uncoverAdjacentFields();

            tempFieldLabelList.clear();
            //System.out.println("Iterations: " + i);
        }

        // If clicked field label is marked by a number:
        if ( isInteger( clickedFieldLabel.text() ) )
        {
            button.hide();
        }

        // If clicked field label contains a mine ( GAME OVER ):
        if ( clickedFieldLabel.text().contentEquals( FieldMarkers.MINE ) )
        {
            // Uncover a clicked mine
            button.hide();
            // Mark a clicked field with mine as red
            clickedFieldLabel.setStyleSheet(Stylesheets.EXPLODED_MINE_FIELD);

            // Stop the time counter
            if ( mainWidget.enableTimeCounting )
            {
                timer.stop();
            }

            // Uncover all mines and make rest of field buttons disabled
            for ( int row = 0; row < rowCount; row++ )
            {
                for ( int column = 0; column < columnCount; column++ )
                {
                    // If under a given field button mine is located...
                    if ( fieldLabels[row][column].text().contentEquals(FieldMarkers.MINE) )
                    {
                        // ...then uncover this field,
                        fieldButtons[row][column].hide();
                    }
                    else // if there's something else
                    {
                        // ...then make button inactive to avoid clicking it
                        fieldButtons[row][column].setDisabled(true);
                    }

                    // Fields which has been marked incorrectly as mined fields - sign them with "E" letter
                    if ( fieldButtons[row][column].text().equals(FieldMarkers.FLAG) && !fieldLabels[row][column].text().equals(FieldMarkers.MINE) )
                    {
                        fieldButtons[row][column].setText(FieldMarkers.ERROR);
                        fieldButtons[row][column].setObjectName(Stylesheets.M_BUTTON);
                        fieldButtons[row][column].setStyleSheet(Stylesheets.INCORRECT_MARKED_FIELD);
                    }
                }
            }
            Main mw = (Main) mainWidget.parent;
            mainWidget.mw.statusBar.showMessage(tr("GAME OVER"));
        }
    }

    public void handleMouseRightClicked()
    {
        button = (MButton) signalSender();

        clickedFieldButtonRow = gridLayout.getItemPosition( gridLayout.indexOf( button ) ).row;
        clickedFieldButtonColumn = gridLayout.getItemPosition( gridLayout.indexOf( button ) ).column;

        QLabel clickedFieldLabel = (QLabel) gridLayout.itemAtPosition(clickedFieldButtonRow, clickedFieldButtonColumn).widget();

        if ( button.text().equals("") )
        {
            button.setObjectName(Stylesheets.M_BUTTON);
            button.setStyleSheet(Stylesheets.FLAGGED_FIELD);
            button.setText(FieldMarkers.FLAG);
            markedMinesCounter++;

            if ( clickedFieldLabel.text().equals(FieldMarkers.MINE) )
            {
                properlyMarkedMinesCounter++;
            }
        }
        else
        {
            button.setText("");
            markedMinesCounter--;

            if ( clickedFieldLabel.text().equals(FieldMarkers.MINE) )
            {
                properlyMarkedMinesCounter--;
            }
        }

        // If number of marked mines is equal to actual number of mines - GAME COMPLETED SUCCESSFULLY
        if ( properlyMarkedMinesCounter == minesCount )
        {
            // Stop the time counter
            if ( mainWidget.enableTimeCounting )
            {
                timer.stop();
            }

            for ( int row = 0; row < rowCount; row++ )
            {
                for ( int column = 0; column < columnCount; column++ )
                {
                    // Make all field buttons disabled
                    fieldButtons[row][column].setDisabled(true);

                    // Fields which has been marked incorrectly as mined fields - sign them with "E" letter
                    if ( fieldButtons[row][column].text().equals(FieldMarkers.FLAG) && !fieldLabels[row][column].text().equals(FieldMarkers.MINE) )
                    {
                        fieldButtons[row][column].setText(FieldMarkers.ERROR);
                        //clickedFieldLabel.setStyleSheet("#myObject { border: 1px solid black; background-color: red; }");
                        fieldButtons[row][column].setObjectName(Stylesheets.M_BUTTON);
                        fieldButtons[row][column].setStyleSheet(Stylesheets.INCORRECT_MARKED_FIELD);
                    }

                    // Hide buttons located over empty fields
                    if ( fieldButtons[row][column].text().equals("") )
                    {
                        fieldButtons[row][column].hide();
                    }
                }
            }

            Main mw = (Main) mainWidget.parent;
            mw.statusBar.showMessage(tr("GAME COMPLETED SUCCESSFULLY"));

            if ( saveBestResult )
            {
                mainWidget.saveBestResult();
            }
        }
    }
}
