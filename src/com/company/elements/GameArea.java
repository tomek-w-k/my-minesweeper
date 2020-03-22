package com.company.elements;

import com.company.Main;
import com.company.constants.GameModeAreaParams;
import com.company.dialogs.BestResultsDialog;
import com.company.dialogs.CustomLevelDialog;
import com.company.dialogs.PreferencesDialog;
import com.company.enums.GameModes;
import com.company.managers.SettingsManager;
import com.company.tools.GameAreaBuilderEx;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QWidget;


public class GameArea extends QWidget
{
    private GameAreaBuilderEx gameAreaBuilder;
    private SettingsManager settingsManager;
    private Main mw;
    private GameModes gameMode;
    private Boolean enableTimeCounting;
    private Boolean saveBestResult;

    // A QTimer object for time counting - measures elapsed game time:
    public QTimer timer;

    // A Time object for time counter - elapsed game time measured by the counter:
    public QTime time;


    public GameArea(QObject parent)
    {
        mw = (Main)parent;
        gameAreaBuilder = new GameAreaBuilderEx(this);
        settingsManager = new SettingsManager(this);

        settingsManager.loadSettingsForGameArea();

        // The reason this method is called here is explained in it's definition
        createActionGroups();

        newGameActionTriggered();
    }

    public GameAreaBuilderEx getGameAreaBuilder() { return gameAreaBuilder; }

    public SettingsManager getSettingsManager() { return settingsManager; }

    public Main getMainWindow() { return mw; }

    public GameModes getGameMode() { return gameMode; }
    public void setGameMode(GameModes gameMode) { this.gameMode = gameMode; }

    public Boolean getEnableTimeCounting() { return enableTimeCounting; }
    public void setEnableTimeCounting(Boolean enableTimeCounting) { this.enableTimeCounting = enableTimeCounting; }

    public Boolean getSaveBestResult() { return saveBestResult; }
    public void setSaveBestResult(Boolean saveBestResult) { this.saveBestResult = saveBestResult; }

    public QTimer getTimer() { return timer; }
    public void setTimer(QTimer timer) { this.timer = timer; }

    public QTime getTime() { return time; }
    public void setTime(QTime time) { this.time = time; }


    public void newGameActionTriggered()
    {
        switch ( gameMode )
        {
            case BASIC_MODE:
                basicActionTriggered();
                mw.getBasicAction().setChecked(true);
                break;
            case MEDIUM_MODE:
                mediumActionTriggered();
                mw.getMediumAction().setChecked(true);
                break;
            case ADVANCED_MODE:
                advancedActionTriggered();
                mw.getAdvancedAction().setChecked(true);
                break;
            case EXPERT_MODE:
                expertActionTriggered();
                mw.getExpertAction().setChecked(true);
                break;
            case CUSTOM_MODE:
                customActionTriggered();
                mw.getCustomAction().setChecked(true);
                break;
        }
    }

    public void basicActionTriggered()
    {
        gameMode = GameModes.BASIC_MODE;
        gameAreaBuilder.setRowCount( GameModeAreaParams.BASIC_MODE_ROW_COUNT );
        gameAreaBuilder.setColumnCount( GameModeAreaParams.BASIC_MODE_COLUMN_COUNT );
        gameAreaBuilder.setMinesCount( GameModeAreaParams.BASIC_MODE_MINES_COUNT );
        gameAreaBuilder.createNewGame();
    }

    public void mediumActionTriggered()
    {
        gameMode = GameModes.MEDIUM_MODE;
        gameAreaBuilder.setRowCount( GameModeAreaParams.MEDIUM_MODE_ROW_COUNT );
        gameAreaBuilder.setColumnCount( GameModeAreaParams.MEDIUM_MODE_COLUMN_COUNT );
        gameAreaBuilder.setMinesCount( GameModeAreaParams.MEDIUM_MODE_MINES_COUNT );
        gameAreaBuilder.createNewGame();
    }

    public void advancedActionTriggered()
    {
        gameMode = GameModes.ADVANCED_MODE;
        gameAreaBuilder.setRowCount( GameModeAreaParams.ADVANCED_MODE_ROW_COUNT );
        gameAreaBuilder.setColumnCount( GameModeAreaParams.ADVANCED_MODE_COLUMN_COUNT );
        gameAreaBuilder.setMinesCount( GameModeAreaParams.ADVANCED_MODE_MINES_COUNT );
        gameAreaBuilder.createNewGame();
    }

    public void expertActionTriggered()
    {
        gameMode = GameModes.EXPERT_MODE;
        gameAreaBuilder.setRowCount( GameModeAreaParams.EXPERT_MODE_ROW_COUNT );
        gameAreaBuilder.setColumnCount( GameModeAreaParams.EXPERT_MODE_COLUMN_COUNT );
        gameAreaBuilder.setMinesCount( GameModeAreaParams.EXPERT_MODE_MINES_COUNT );
        gameAreaBuilder.createNewGame();
    }

    public void customActionTriggered()
    {
        gameMode = GameModes.CUSTOM_MODE;
        CustomLevelDialog customLevelDialog = new CustomLevelDialog(mw);

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint gameAreaCenterPoint = new QPoint ( (gameAreaBuilder.getFieldSize()*gameAreaBuilder.getColumnCount())/2+mw.x(),
                (gameAreaBuilder.getFieldSize()*gameAreaBuilder.getRowCount())/2+mw.y() );
        customLevelDialog.setModal(true);
        customLevelDialog.setVisible(true);
        customLevelDialog.adjustSize();
        customLevelDialog.setGeometry( gameAreaCenterPoint.x()-customLevelDialog.width()/2, gameAreaCenterPoint.y()-customLevelDialog.height()/2,
                400, customLevelDialog.geometry().height());
        customLevelDialog.exec();

        gameAreaBuilder.setRowCount( customLevelDialog.getCustomModeRowCount() );
        gameAreaBuilder.setColumnCount( customLevelDialog.getCustomModeColumnCount() );
        gameAreaBuilder.setMinesCount( customLevelDialog.getCustomModeMinesCount() );
        gameAreaBuilder.createNewGame();
    }

    public void preferencesActionTriggered()
    {
        PreferencesDialog preferencesDialog = new PreferencesDialog(this);

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
        BestResultsDialog bestResultsDialog = new BestResultsDialog();

        // Calculate window position coordinates in order to place it center-aligned with reference to main window
        QPoint mainWindowCenterPoint = new QPoint( mw.width()/2 + mw.x(), mw.height()/2 + mw.y() );
        bestResultsDialog.setGeometry( mainWindowCenterPoint.x()-bestResultsDialog.width()/2, mainWindowCenterPoint.y()-bestResultsDialog.height()/2,
                500, 600);

        bestResultsDialog.setModal(true);
        bestResultsDialog.setVisible(true);
        bestResultsDialog.exec();
    }

    /*
        Game mode actions has to be added here to their action group and set checkable.
        It's necessary to do it in MainWidget constructor instead of Main constructor because an initial game mode is being read from settings
        and it has to be set initially on a toolbar as checked. This is important because MainWidget is created
        and it's constructor is called before Main constructor.
    */
    public void createActionGroups()
    {
        // Assign object names to game mode actions - for recognizing checked game mode when best result is being saved
        mw.getBasicAction().setObjectName("basic");
        mw.getMediumAction().setObjectName("medium");
        mw.getAdvancedAction().setObjectName("advanced");
        mw.getExpertAction().setObjectName("expert");
        mw.getCustomAction().setObjectName("custom");

        // Set actions checkable
        mw.getBasicAction().setCheckable(true);
        mw.getMediumAction().setCheckable(true);
        mw.getAdvancedAction().setCheckable(true);
        mw.getExpertAction().setCheckable(true);
        mw.getCustomAction().setCheckable(true);

        // Difficulty level action group:
        mw.difficultyLevelActionGroup.addAction(mw.getBasicAction());
        mw.difficultyLevelActionGroup.addAction(mw.getMediumAction());
        mw.difficultyLevelActionGroup.addAction(mw.getAdvancedAction());
        mw.difficultyLevelActionGroup.addAction(mw.getExpertAction());
        mw.difficultyLevelActionGroup.addAction(mw.getCustomAction());
    }
}
