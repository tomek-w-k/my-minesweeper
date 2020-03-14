package com.company;

import com.company.constants.*;
import com.company.elements.Field;
import com.company.elements.MButton;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import java.util.*;

public class MainWidget extends QWidget
{
    private static final int TIME_INDEX = 1;

    // - - - APPLICATION GUI ELEMENTS - - -


    // - - - CLIENT AREA ELEMENTS - - -
    public Signal1<QObject> clicked = new Signal1<QObject>();
    private QSignalMapper signalMapper;
    QGridLayout gridLayout;
    MButton fieldButton;
    QLabel fieldLabel;
    private MButton[][] fieldButtons;
    private QLabel[][] fieldLabels;

    // Clicked field coordinates on a gridlayout:
    int clickedFieldButtonRow;
    int clickedFieldButtonColumn;

    // Label under clicked field button:
    QLabel clickedFieldLabel;

    // A temporary container with empty fields ( used to uncover empty areas ):
    private ArrayList<QLabel> tempFieldLabelList = new ArrayList<QLabel>();

    // Clicked field button:
    MButton button;

    // Number of rows, columns and mines for entire area:
    int rowCount, columnCount, minesCount;

    // Size of fields:
    Integer fieldSize = 0;

    // Marked mines counter:
    Integer markedMinesCounter = 0;

    // Properly marked mines counter:
    Integer properlyMarkedMinesCounter = 0;

    static int i = 0;

    // A QTimer object for time counting - measures elapsed game time:
    QTimer timer = null;

    // A Time object for time counter - elapsed game time measured by the counter:
    QTime time = null;

    // Settings - enable time counter and save best result
    Boolean enableTimeCounting = true;
    Boolean saveBestResult = true;

    // Initial game mode - read from settings and stores a game mode to start at program startup
    //Integer initialGameMode = 0;
    MiscGameParams.GameModes initialGameMode;

    QObject parent;
    Main mw;


    public MainWidget(QObject lParent)
    {
        this.parent = lParent;
        mw = (Main) this.parent;

        loadSettings();
        newGameActionTriggered();
    }

    public void loadSettings()
    {
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);
        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Set field size
        String fieldSize = settings.value(SettingsKeys.FIELD_SIZE, MiscGameParams.DEFAULT_FIELD_SIZE).toString();
        setFieldSize( Integer.parseInt(fieldSize) );

        // Time counting and save best result
        String enableTimeCounting = settings.value(SettingsKeys.ENABLE_TIME_COUNTING, true).toString();
        String saveBestResult = settings.value(SettingsKeys.SAVE_BEST_RESULT, true).toString();
        this.enableTimeCounting = Boolean.parseBoolean( enableTimeCounting );
        this.saveBestResult = Boolean.parseBoolean( saveBestResult );

        // Game mode (default: BASIC_MODE)
        MiscGameParams.GameModes gameMode = (MiscGameParams.GameModes) settings.value(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.BASIC_MODE);

        // The reason this function is called here is explained in it's description below
        createActionGroups();

        switch ( gameMode )
        {
            case BASIC_MODE:
                initialGameMode = MiscGameParams.GameModes.BASIC_MODE;
                break;
            case MEDIUM_MODE:
                initialGameMode = MiscGameParams.GameModes.MEDIUM_MODE;
                break;
            case ADVANCED_MODE:
                initialGameMode = MiscGameParams.GameModes.ADVANCED_MODE;
                break;
            case EXPERT_MODE:
                initialGameMode = MiscGameParams.GameModes.EXPERT_MODE;
                break;
            case CUSTOM_MODE:
                initialGameMode = MiscGameParams.GameModes.CUSTOM_MODE;
                break;
            default:
                initialGameMode = MiscGameParams.GameModes.BASIC_MODE;
                break;
        }
        setRowCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_ROW_COUNT, "10").toString() ) );
        setColumnCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_COLUMN_COUNT, "10").toString() ) );
        setMinesCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_MINES_COUNT, "25").toString() ) );

        // TEST - language settings
        String language = settings.value(SettingsKeys.LANGUAGE, MiscGameParams.Languages.ENGLISH).toString();
        mw.changeTranslator(language);

        settings.endGroup();
    }

    /*
        Game mode actions has to be added here to their action group and set checkable.
        It's necessary to do it in MainWidget constructor instead of Main constructor because an initial game mode is being read from settings
        and it has to be set initially on a toolbar as checked. This is important because MainWidget is created
        and it's constructor is called before Main constructor.
    */
    private void createActionGroups()
    {
        // Assign object names to game mode actions - for recognizing checked game mode when best result is being saved
        mw.basicAction.setObjectName("basic");
        mw.mediumAction.setObjectName("medium");
        mw.advancedAction.setObjectName("advanced");
        mw.expertAction.setObjectName("expert");
        mw.customAction.setObjectName("custom");

        // Set actions checkable
        mw.basicAction.setCheckable(true);
        mw.mediumAction.setCheckable(true);
        mw.advancedAction.setCheckable(true);
        mw.expertAction.setCheckable(true);
        mw.customAction.setCheckable(true);

        // Difficulty level action group:
        mw.difficultyLevelActionGroup.addAction(mw.basicAction);
        mw.difficultyLevelActionGroup.addAction(mw.mediumAction);
        mw.difficultyLevelActionGroup.addAction(mw.advancedAction);
        mw.difficultyLevelActionGroup.addAction(mw.expertAction);
        mw.difficultyLevelActionGroup.addAction(mw.customAction);
    }

    private boolean isInteger( String lStr )
    {
        try
        {
            Integer.parseInt( lStr );
            return true;
        }
        catch ( NumberFormatException e )
        {
            return false;
        }
    }

    private void timeCounter()
    {
        if ( timer != null )
        {
            timer.dispose();
        }
        if ( time != null )
        {
            time.dispose();
        }

        if ( enableTimeCounting )
        {
            timer  = new QTimer(this);
            timer.timeout.connect(this, "updateTimeCounter()");
            timer.start(1000);
            time = new QTime(0, 0, 0);
        }
        else
        {
            mw.updateTime("DISABLED");
        }
    }

    private void updateTimeCounter()
    {
        time = time.addSecs(1);
        String currentTime = time.toString("hh:mm:ss");
        mw.updateTime(currentTime);
    }

    /*
        A slot called when a field button has been clicked.
     */
    public void clicked()
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
            if ( enableTimeCounting )
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
            Main mw = (Main) this.parent;
            mw.statusBar.showMessage(tr("GAME OVER"));
        }
    }

    public void rightClicked()
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
            if ( enableTimeCounting )
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

            Main mw = (Main) this.parent;
            mw.statusBar.showMessage(tr("GAME COMPLETED SUCCESSFULLY"));

            if ( saveBestResult )
            {
                saveBestResult();
            }
        }
        //System.out.println(properlyMarkedMinesCounter);
    }

    private void saveBestResult()
    {
        // At first, it's necessary to check if obtained time is the smallest time from all results saved for this game mode
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        // Get all results for this game mode - keys to them are stored in "keys" List
        settings.beginGroup(SettingsKeys.BEST_RESULTS_GROUP + "/" + mw.difficultyLevelActionGroup.checkedAction().objectName());
        List<String> keys = settings.childKeys();

        // Sort an array of keys above from smaller time to bigger - bubble sort
        if ( keys.size() > 1 )
        {
            boolean swapped = true;
            while ( swapped )
            {
                swapped = false;
                for ( int i = 0; i < keys.size()-1; i++ )
                {
                    // read times from current and next result
                    ArrayList<String> resultArray = (ArrayList<String>) settings.value(keys.get(i));
                    ArrayList<String> nextResultArray = (ArrayList<String>) settings.value(keys.get(i+1));
                    QTime resultTime = QTime.fromString(resultArray.get(TIME_INDEX));
                    QTime nextResultTime = QTime.fromString(nextResultArray.get(TIME_INDEX));
                    String tempKey;

                    // if resultTime is bigger than nextResultTime...
                    if ( resultTime.secsTo(nextResultTime) < 0 )
                    {
                        //...then swap two keys from keys array where these times are come from
                        tempKey = keys.get(i);
                        keys.set(i, keys.get(i+1));
                        keys.set(i+1, tempKey);
                        tempKey = null;
                        swapped = true;
                    }
                }
            }
        }

        // check if obtained time is smaller than the smallest already saved time (first element of sorted keys array)
        // but first check if keys array has at least one key - if it has,
        if ( keys.size() > 0 )
        {
            // then get the smallest time from keys array
            ArrayList<String> resultArray = (ArrayList<String>) settings.value(keys.get(0));
            QTime smallestSavedTime = QTime.fromString(resultArray.get(TIME_INDEX));

            // if the smallest saved time is bigger than obtained time...
            if ( time.secsTo(smallestSavedTime) > 0 )
            {
                //...then save obtained time - but the program has to save up to seven best results for each game mode, so:
                // if number of results is not greater to seven, then
                if ( keys.size() < 7 )
                {
                    // just save an obtained time
                    pSaveBestResult();
                }
                // but if it's equal to seven, then
                if ( keys.size() == 7 )
                {
                    // first remove the biggest time from the list of saved times (last element of sorted keys array)
                    settings.remove( keys.get(6) );

                    // and after that - save obtained time
                    pSaveBestResult();
                }
            }
        }
        // if it hasn't any result,
        else
        {
            // then just save any obtained result
            pSaveBestResult();
        }
        settings.endGroup();
    }

    /*
        This method has to save an obtained game result - private method for use in saveBestResult() method
     */
    private void pSaveBestResult()
    {
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);
        settings.beginGroup(SettingsKeys.BEST_RESULTS_GROUP + "/" + mw.difficultyLevelActionGroup.checkedAction().objectName());

        // Get player's name - open a dialog to get it
        SaveBestResultDialog saveBestResultDialog = new SaveBestResultDialog();

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint mainWindowCenterPoint = new QPoint( mw.width()/2 + mw.x(), mw.height()/2 + mw.y() );
        saveBestResultDialog.setGeometry( mainWindowCenterPoint.x()-saveBestResultDialog.width()/2, mainWindowCenterPoint.y()-saveBestResultDialog.height()/2,
                500, 380);

        saveBestResultDialog.setModal(true);
        saveBestResultDialog.setVisible(true);
        saveBestResultDialog.adjustSize();
        saveBestResultDialog.exec();

        // Prepare the result details - wrap them with an array
        ArrayList<String> resultDetails = new ArrayList<String>();
        resultDetails.add(saveBestResultDialog.getPlayerName());
        resultDetails.add(time.toString());

        // and then save the result
        settings.setValue(QDateTime.currentDateTime().toString(), resultDetails);
        settings.endGroup();

        // Show all best results
        bestResultsActionTriggered();
    }

    /*
        Returns a specified adjacent field to the given base field described by its coordinates (base row, base column)
     */
    private Field adjacentField(AdjacentFieldRelativePos adjacentFieldRelativePos, int baseFieldRow, int baseFieldColumn)
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
    private void uncoverAdjacentFields()
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

    private void placeMinesRandomly()
    {
        Random random = new Random(400);
        boolean gameArea[][] = new boolean[rowCount][columnCount];
        LinkedList<Boolean> tempList = new LinkedList<Boolean>();
        boolean minePlaced = false;
        int randomRow, randomColumn;
        QLabel fieldLabel;

        // preliminary draw of mined places
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

            // clear tempList
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

            // clear tempList
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
        Field adjacentField = adjacentField(adjacentFieldRelativePos, baseFieldRow, baseFieldColumn);
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

    private void surroundMinesWithMarkers()
    {
        QLabel fieldLabel;

        for ( int row = 0; row < rowCount; row++ )
        {
            for ( int column = 0; column < columnCount; column++ )
            {
                fieldLabel = (QLabel) gridLayout.itemAtPosition(row, column).widget();
                // If given field contains a mine...
                if ( fieldLabel.text().contentEquals(FieldMarkers.MINE) )
                {
                    // ...then surround it with markers as follows:
                    // Left-adjacent field:
                    if ( column > 0 )
                    {
                        surround(AdjacentFieldRelativePos.LEFT, row, column);
                    }

                    // Right-adjacent field:
                    if ( column < columnCount-1 )
                    {
                        surround(AdjacentFieldRelativePos.RIGHT, row, column);
                    }

                    // Up-adjacent three fields:
                    if ( row > 0)
                    {
                        // Up-left adjacent field:
                        if ( column > 0 )
                        {
                            surround(AdjacentFieldRelativePos.UP_LEFT, row, column);
                        }

                        // Up-adjacent field:
                        surround(AdjacentFieldRelativePos.UP, row, column);

                        // Up-right adjacent field:
                        if ( column < columnCount-1 )
                        {
                            surround(AdjacentFieldRelativePos.UP_RIGHT, row, column);
                        }
                    }

                    // Down-adjacent three fields:
                    if ( row < rowCount-1 )
                    {
                        // Down-left adjacent field:
                        if ( column > 0 )
                        {
                            surround(AdjacentFieldRelativePos.DOWN_LEFT, row, column);
                        }

                        // Down-adjacent field:
                        surround(AdjacentFieldRelativePos.DOWN, row, column);

                        // Down-right adjacent field:
                        if ( column < columnCount-1 )
                        {
                            surround(AdjacentFieldRelativePos.DOWN_RIGHT, row, column);
                        }
                    }
                }
            }
        }
    }

    public void createNewGame()
    {
        // First it's necessary to remove some elements of an old client area
        // Delete old buttons:
        if ( fieldButtons != null )
        {
            for ( QPushButton buttons[] : fieldButtons )
            {
                for ( QPushButton button : buttons )
                {
                    if ( button != null )
                    {
                        button.dispose();
                    }
                }
            }
        }

        // Delete old labels:
        if ( fieldLabels != null )
        {
            for ( QLabel labels[] : fieldLabels )
            {
                for ( QLabel label : labels )
                {
                    if ( label != null )
                    {
                        label.dispose();
                    }
                }
            }
        }

        // Delete an old grid layout:
        if ( gridLayout != null )
        {
            gridLayout.dispose();
        }

        // Delete an old signal mapper:
        if ( signalMapper != null )
        {
            signalMapper.dispose();
        }

        // Reset mine counters:
        markedMinesCounter = 0;
        properlyMarkedMinesCounter = 0;

        // Create new arrays for field buttons and labels:
        fieldButtons = new MButton[rowCount][columnCount];
        fieldLabels = new QLabel[rowCount][columnCount];

        // Create a new grid layout:
        gridLayout = new QGridLayout();

        // Create a new signal mapper for buttons:
        signalMapper = new QSignalMapper(this);

        gridLayout.setHorizontalSpacing(0);
        gridLayout.setVerticalSpacing(0);

        // Fill board with labels
        for ( int row = 0; row < rowCount; row++ )
        {
            for ( int column = 0; column < columnCount; column++ )
            {
                fieldLabel = new QLabel("");
                fieldLabel.setMinimumSize( new QSize(fieldSize, fieldSize ) );
                fieldLabel.setMaximumSize( new QSize(fieldSize, fieldSize) );
                fieldLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
                fieldLabel.setFrameShape(QFrame.Shape.Box);
                fieldLabel.setObjectName(Stylesheets.M_OBJECT);
                fieldLabel.setStyleSheet(Stylesheets.EMPTY_UNCOVERED_FIELD);
                gridLayout.addWidget( fieldLabel, row, column );
                fieldLabels[row][column] = fieldLabel;
            }
        }

        placeMinesRandomly();
        surroundMinesWithMarkers();

        // Fill board with buttons
        for ( int row = 0; row < rowCount; row++ )
        {
            for ( int column = 0; column < columnCount; column++ )
            {
                fieldButton = new MButton( "", this );
                fieldButton.setMinimumSize(new QSize(fieldSize, fieldSize));
                fieldButton.setMaximumSize(new QSize(fieldSize, fieldSize));
                fieldButton.clicked.connect( this, "clicked()" );
                fieldButton.rightClicked.connect( this, "rightClicked()" );
                gridLayout.addWidget( fieldButton, row, column );
                fieldButtons[row][column] = fieldButton;
            }
        }

        this.setLayout( gridLayout );

        // - - - Setting a size of main window and make it fixed - - -
        Main mainWindow = (Main) this.parent;
        QSize mainWindowSize;

        // for Linux
        if ( System.getProperty("os.name").equals("Linux") )
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscGameParams.LINUX_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscGameParams.LINUX_MAIN_WINDOW_HEIGHT_EXCESS  );
        }

        // for Windows-like
        if ( System.getProperty("os.name").contains("Win") )
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscGameParams.WINDOWS_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscGameParams.WINDOWS_MAIN_WINDOW_HEIGHT_EXCESS  );
        }
        else // other os
        {
            mainWindowSize = new QSize( fieldSize * columnCount + MiscGameParams.LINUX_MAIN_WINDOW_WIDTH_EXCESS,
                    fieldSize * rowCount + MiscGameParams.LINUX_MAIN_WINDOW_HEIGHT_EXCESS  );
        }

        mainWindow.setFixedSize( mainWindowSize );
        mainWindow.statusBar.showMessage("");

        // Create a time counter (if enabled - look inside to this function)
        timeCounter();
    }

    public void setRowCount(int lRowCount)
    {
        this.rowCount = lRowCount;
    }

    public void setColumnCount(int lColumnCount)
    {
        this.columnCount = lColumnCount;
    }

    public void setMinesCount(int lMinesCount)
    {
        this.minesCount = lMinesCount;
    }

    public void setFieldSize( int lFieldSize )
    {
        this.fieldSize = lFieldSize;
    }

    public void newGameActionTriggered()
    {
        switch ( initialGameMode )
        {
            case BASIC_MODE:
                basicActionTriggered();
                mw.basicAction.setChecked(true);
                break;
            case MEDIUM_MODE:
                mediumActionTriggered();
                mw.mediumAction.setChecked(true);
                break;
            case ADVANCED_MODE:
                advancedActionTriggered();
                mw.advancedAction.setChecked(true);
                break;
            case EXPERT_MODE:
                expertActionTriggered();
                mw.expertAction.setChecked(true);
                break;
            case CUSTOM_MODE:
                customActionTriggered();
                mw.customAction.setChecked(true);
                break;
            default:
                basicActionTriggered();
                mw.basicAction.setChecked(true);
                break;
        }
    }

    public void basicActionTriggered()
    {
        initialGameMode = MiscGameParams.GameModes.BASIC_MODE;
        setRowCount( GameModeAreaParams.BASIC_MODE_ROW_COUNT );
        setColumnCount( GameModeAreaParams.BASIC_MODE_COLUMN_COUNT );
        setMinesCount( GameModeAreaParams.BASIC_MODE_MINES_COUNT );
        createNewGame();
    }

    public void mediumActionTriggered()
    {
        initialGameMode = MiscGameParams.GameModes.MEDIUM_MODE;
        setRowCount( GameModeAreaParams.MEDIUM_MODE_ROW_COUNT );
        setColumnCount( GameModeAreaParams.MEDIUM_MODE_COLUMN_COUNT );
        setMinesCount( GameModeAreaParams.MEDIUM_MODE_MINES_COUNT );
        createNewGame();
    }

    public void advancedActionTriggered()
    {
        initialGameMode = MiscGameParams.GameModes.ADVANCED_MODE;
        setRowCount( GameModeAreaParams.ADVANCED_MODE_ROW_COUNT );
        setColumnCount( GameModeAreaParams.ADVANCED_MODE_COLUMN_COUNT );
        setMinesCount( GameModeAreaParams.ADVANCED_MODE_MINES_COUNT );
        createNewGame();
    }

    public void expertActionTriggered()
    {
        initialGameMode = MiscGameParams.GameModes.EXPERT_MODE;
        setRowCount( GameModeAreaParams.EXPERT_MODE_ROW_COUNT );
        setColumnCount( GameModeAreaParams.EXPERT_MODE_COLUMN_COUNT );
        setMinesCount( GameModeAreaParams.EXPERT_MODE_MINES_COUNT );
        createNewGame();
    }

    public void customActionTriggered()
    {
        initialGameMode = MiscGameParams.GameModes.CUSTOM_MODE;
        Main mw = (Main) parent;
        CustomLevelDialog customLevelDialog = new CustomLevelDialog(mw);

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint gameAreaCenterPoint = new QPoint ( (fieldSize*columnCount)/2+mw.x(), (fieldSize*rowCount)/2+mw.y() );
        customLevelDialog.setModal(true);
        customLevelDialog.setVisible(true);
        customLevelDialog.adjustSize();
        customLevelDialog.setGeometry( gameAreaCenterPoint.x()-customLevelDialog.width()/2, gameAreaCenterPoint.y()-customLevelDialog.height()/2,
                400, customLevelDialog.geometry().height());
        customLevelDialog.exec();

        setRowCount( customLevelDialog.getCustomModeRowCount() );
        setColumnCount( customLevelDialog.getCustomModeColumnCount() );
        setMinesCount( customLevelDialog.getCustomModeMinesCount() );
        createNewGame();
    }

    public void preferencesActionTriggered()
    {
        Main mw = (Main) this.parent;
        PreferencesDialog preferencesDialog = new PreferencesDialog(this);
        preferencesDialog.setParentWidget(this);

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint mainWindowCenterPoint = new QPoint( mw.width()/2 + mw.x(), mw.height()/2 + mw.y() );
        preferencesDialog.setModal(true);
        preferencesDialog.setVisible(true);
        preferencesDialog.adjustSize();
        preferencesDialog.setGeometry( mainWindowCenterPoint.x()-preferencesDialog.width()/2, mainWindowCenterPoint.y()-preferencesDialog.height()/2,
                640, preferencesDialog.geometry().height());
        preferencesDialog.exec();
    }

   public void bestResultsActionTriggered()
   {
       Main mw = (Main) this.parent;
       BestResultsDialog bestResultsDialog = new BestResultsDialog();

       // Calculate window position coordinates in order to place it center-aligned with reference to main window
       QPoint mainWindowCenterPoint = new QPoint( mw.width()/2 + mw.x(), mw.height()/2 + mw.y() );
       bestResultsDialog.setGeometry( mainWindowCenterPoint.x()-bestResultsDialog.width()/2, mainWindowCenterPoint.y()-bestResultsDialog.height()/2,
               500, 600);

       bestResultsDialog.setModal(true);
       bestResultsDialog.setVisible(true);
       bestResultsDialog.exec();
   }

}
