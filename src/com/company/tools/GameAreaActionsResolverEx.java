package com.company.tools;

import com.company.Main;
import com.company.constants.FieldMarkers;
import com.company.constants.Stylesheets;
import com.company.elements.Field;
import com.company.elements.FieldButton;
import com.company.elements.GameArea;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;

import java.util.ArrayList;
import java.util.List;

import static com.company.converters.NumberParser.isInteger;


public class GameAreaActionsResolverEx extends QObject
{
    private Integer clickedFieldButtonRow;
    private Integer clickedFieldButtonColumn;

    private QLabel clickedFieldLabel;

    private QGridLayout gridLayout;
    private FieldButton[][] fieldButtons;
    private QLabel[][] fieldLabels;

    private Integer markedMinesCounter = 0;
    private Integer properlyMarkedMinesCounter = 0;

    private AdjacentFieldResolver adjacentFieldResolver;

    private Integer uncoveredFieldTempNumber;

    private List<QLabel> tempFieldLabelList;

    // Reference to the game area
    private GameArea gameArea;

    private BestResultSaver bestResultSaver;


    public GameAreaActionsResolverEx(GameArea gameArea, QGridLayout gridLayout, FieldButton[][] fieldButtons, QLabel[][] fieldLabels)
    {
        this.gameArea = gameArea;
        this.gridLayout = gridLayout;

        this.fieldButtons = fieldButtons;
        this.fieldLabels = fieldLabels;

        adjacentFieldResolver = new AdjacentFieldResolver(fieldButtons, fieldLabels);

        tempFieldLabelList = new ArrayList<QLabel>();
    }


    /*
        This function checks if the given adjacent field is empty or has a digit marker. If so - uncovers the field.
     */
    private void uncover(AdjacentFieldRelativePos adjacentFieldRelativePos)
    {
        Field adjacentField = adjacentFieldResolver.adjacentField(adjacentFieldRelativePos, clickedFieldButtonRow, clickedFieldButtonColumn);
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
        if ( clickedFieldButtonColumn < gridLayout.columnCount()-1 ) // right-side boundary condition
        {
            uncover(AdjacentFieldRelativePos.RIGHT);
        }

        // Up-adjacent three fields:
        if ( clickedFieldButtonRow > 0) // up-side boundary condition
        {
            // check up-left-adjacent field label:
            if ( clickedFieldButtonColumn > 0 ) // up-left-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.UP_LEFT);
            }

            // check up-adjacent field label:
            uncover(AdjacentFieldRelativePos.UP);

            // check up-right-adjacent field label:
            if ( clickedFieldButtonColumn < gridLayout.columnCount()-1 ) // up-right-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.UP_RIGHT);
            }
        }

        // down-adjacent three fields:
        if ( clickedFieldButtonRow < gridLayout.rowCount()-1 ) // down-side boundary condition
        {
            // check down-left-adjacent field label:
            if ( clickedFieldButtonColumn > 0 ) // down-left-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.DOWN_LEFT);
            }

            // check down-adjacent field label:
            uncover(AdjacentFieldRelativePos.DOWN);

            // check down-right-adjacent field label:
            if ( clickedFieldButtonColumn < gridLayout.columnCount()-1 ) // down-right-side boundary condition
            {
                uncover(AdjacentFieldRelativePos.DOWN_RIGHT);
            }
        }

        if ( uncoveredFieldTempNumber < tempFieldLabelList.size() )
        {
            QLabel label = tempFieldLabelList.get(uncoveredFieldTempNumber);
            clickedFieldButtonRow = gridLayout.getItemPosition( gridLayout.indexOf( label ) ).row;
            clickedFieldButtonColumn = gridLayout.getItemPosition( gridLayout.indexOf( label ) ).column;
            uncoveredFieldTempNumber++;

            uncoverAdjacentFields();
        }
    }

    public void handleMouseClicked()
    {
        uncoveredFieldTempNumber = 0;

        // Clicked field button:
        var clickedFieldButton = (FieldButton) signalSender();

        // Clicked field button coordinates on a gridlayout:
        clickedFieldButtonRow = gridLayout.getItemPosition( gridLayout.indexOf( clickedFieldButton ) ).row;
        clickedFieldButtonColumn = gridLayout.getItemPosition( gridLayout.indexOf( clickedFieldButton ) ).column;

        // Label under clicked button:
        clickedFieldLabel = (QLabel) gridLayout.itemAtPosition(clickedFieldButtonRow, clickedFieldButtonColumn).widget();

        // If clicked field is marked then do nothing:
        if ( clickedFieldButton.text().equals(FieldMarkers.FLAG) )
        {
            return;
        }

        // If clicked field label is an empty field:
        if ( clickedFieldLabel.text().contentEquals(""))
        {
            clickedFieldButton.hide();
            uncoverAdjacentFields();

            tempFieldLabelList.clear();
            //System.out.println("Iterations: " + i);
        }

        // If clicked field label is marked by a number:
        if ( isInteger( clickedFieldLabel.text() ) )
        {
            clickedFieldButton.hide();
        }

        // If clicked field label contains a mine ( GAME OVER ):
        if ( clickedFieldLabel.text().contentEquals( FieldMarkers.MINE ) )
        {
            // Uncover a clicked mine
            clickedFieldButton.hide();
            // Mark a clicked field with mine as red
            clickedFieldLabel.setStyleSheet(Stylesheets.EXPLODED_MINE_FIELD);

            // Stop the time counter
            if ( gameArea.getEnableTimeCounting() )
            {
                gameArea.getTimer().stop();
                gameArea.getTimer().disconnect();
            }

            // Uncover all mines and make rest of field buttons disabled
            for ( int row = 0; row < gridLayout.columnCount(); row++ )
            {
                for ( int column = 0; column < gridLayout.columnCount(); column++ )
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
            gameArea.getMainWindow().statusBar.showMessage(tr("GAME OVER"));
        }
    }

    public void handleMouseRightClicked()
    {
        var button = (FieldButton) signalSender();

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
        if ( properlyMarkedMinesCounter == gameArea.getGameAreaBuilder().getMinesCount() )
        {
            // Stop the time counter
            System.out.println(gameArea.getEnableTimeCounting());
            if ( gameArea.getEnableTimeCounting() )
            {
                gameArea.getTimer().stop();
                gameArea.getTimer().disconnect();
            }

            for ( int row = 0; row < gridLayout.rowCount(); row++ )
            {
                for ( int column = 0; column < gridLayout.columnCount(); column++ )
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

            gameArea.getMainWindow().statusBar.showMessage(tr("GAME COMPLETED SUCCESSFULLY"));

            if ( gameArea.getSaveBestResult() )
            {
                bestResultSaver = new BestResultSaver(gameArea);
                System.out.println(gameArea.getTime());
                bestResultSaver.saveBestResult();
            }
        }
    }
}
