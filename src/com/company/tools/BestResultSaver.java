package com.company.tools;

import com.company.constants.SettingsKeys;
import com.company.dialogs.SaveBestResultDialog;
import com.company.elements.GameArea;
import com.trolltech.qt.core.QDateTime;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.QTime;

import java.util.ArrayList;
import java.util.List;

public class BestResultSaver
{
    private static final int ELAPSED_TIME_COLUMN = 1;
    private GameArea gameArea;


    public BestResultSaver(GameArea gameArea)
    {
        this.gameArea = gameArea;
    }

    /*
        This method has to save an obtained game result - private method for use in saveBestResult() method
     */
    private void pSaveBestResult()
    {
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);
        settings.beginGroup(SettingsKeys.BEST_RESULTS_GROUP + "/" + gameArea.getMainWindow().difficultyLevelActionGroup.checkedAction().objectName());

        // Get player's name - open a dialog to get it
        SaveBestResultDialog saveBestResultDialog = new SaveBestResultDialog();

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint mainWindowCenterPoint = new QPoint( gameArea.getMainWindow().width()/2 + gameArea.getMainWindow().x(),
                gameArea.getMainWindow().height()/2 + gameArea.getMainWindow().y() );
        saveBestResultDialog.setGeometry( mainWindowCenterPoint.x()-saveBestResultDialog.width()/2, mainWindowCenterPoint.y()-saveBestResultDialog.height()/2,
                500, 380);

        saveBestResultDialog.setModal(true);
        saveBestResultDialog.setVisible(true);
        saveBestResultDialog.adjustSize();
        saveBestResultDialog.exec();

        // Prepare the result details - wrap them with an array
        ArrayList<String> resultDetails = new ArrayList<String>();
        resultDetails.add(saveBestResultDialog.getPlayerName());
        resultDetails.add(gameArea.getTime().toString());

        // and then save the result
        settings.setValue(QDateTime.currentDateTime().toString(), resultDetails);
        settings.endGroup();

        // Show all best results
        gameArea.bestResultsActionTriggered();
    }

    public void saveBestResult()
    {
        // At first, it's necessary to check if obtained time is the smallest time from all results saved for this game mode
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        // Get all results for this game mode - keys to them are stored in "keys" List
        settings.beginGroup(SettingsKeys.BEST_RESULTS_GROUP + "/" + gameArea.getMainWindow().difficultyLevelActionGroup.checkedAction().objectName());
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
                    QTime resultTime = QTime.fromString(resultArray.get(ELAPSED_TIME_COLUMN));
                    QTime nextResultTime = QTime.fromString(nextResultArray.get(ELAPSED_TIME_COLUMN));
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
            QTime smallestSavedTime = QTime.fromString(resultArray.get(ELAPSED_TIME_COLUMN));

            // if the smallest saved time is bigger than obtained time...
            if ( gameArea.getTime().secsTo(smallestSavedTime) > 0 )
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
}
