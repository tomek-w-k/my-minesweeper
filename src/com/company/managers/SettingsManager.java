package com.company.managers;

import com.company.MainWidget;
import com.company.dialogs.PreferencesDialog;
import com.company.constants.FieldSizes;
import com.company.constants.SettingsKeys;
import com.company.enums.GameModes;
import com.company.enums.Languages;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.gui.QWidget;

public class SettingsManager
{
    private QWidget targetWidget;

    public SettingsManager(QWidget targetWidget)
    {
        this.targetWidget = targetWidget;
    }

    // Reads data from QSettings class and set them on dialog widgets
    public void loadSettingsForPreferencesDialog()
    {
        PreferencesDialog preferencesDialog = (PreferencesDialog)targetWidget;

        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Game mode (default: BASIC_MODE)
        GameModes gameMode = (GameModes) settings.value(SettingsKeys.GAME_MODE, GameModes.BASIC_MODE);

        switch ( gameMode )
        {
            case BASIC_MODE:
                preferencesDialog.basicModeRadioButton.setChecked(true);
                break;
            case MEDIUM_MODE:
                preferencesDialog.mediumModeRadioButton.setChecked(true);
                break;
            case ADVANCED_MODE:
                preferencesDialog.advancedModeRadioButton.setChecked(true);
                break;
            case EXPERT_MODE:
                preferencesDialog.expertModeRadioButton.setChecked(true);
                break;
            case CUSTOM_MODE:
                preferencesDialog.customModeRadioButton.setChecked(true);
                break;
        }
        preferencesDialog.customRowsSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_ROW_COUNT, "10").toString() ) );
        preferencesDialog.customColumnsSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_COLUMN_COUNT, "10").toString() ) );
        preferencesDialog.customMinesSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_MINES_COUNT, "25").toString() ) );

        // Language (default: ENGLISH)
        Languages language = (Languages) settings.value(SettingsKeys.LANGUAGE, Languages.ENGLISH);

        switch ( language )
        {
            case ENGLISH:
                preferencesDialog.englishLanguageRadioButton.setChecked(true);
                break;
            case POLISH:
                preferencesDialog.polishLanguageRadioButton.setChecked(true);
                break;
            case GERMAN:
                preferencesDialog.germanLanguageRadioButton.setChecked(true);
                break;
        }

        // Field size (default: DEFAULT_FIELD_SIZE)
        String fieldSize = settings.value(SettingsKeys.FIELD_SIZE, FieldSizes.DEFAULT_FIELD_SIZE).toString();
        preferencesDialog.fieldSizeSpinBox.setValue( Integer.parseInt(fieldSize) );

        // Time counting and save best result (default: true)
        String enableTimeCounting = settings.value(SettingsKeys.ENABLE_TIME_COUNTING, true).toString();
        String saveBestResult = settings.value(SettingsKeys.SAVE_BEST_RESULT, true).toString();

        preferencesDialog.enableTimeCountingCheckBox.setChecked( Boolean.parseBoolean(enableTimeCounting) );
        preferencesDialog.saveBestResultCheckBox.setChecked( Boolean.parseBoolean(saveBestResult) );

        settings.endGroup();
    }

    // Collects data from dialog widgets and saves using QSettings class
    public void saveSettingsForPreferencesDialog()
    {
        PreferencesDialog preferencesDialog = (PreferencesDialog)targetWidget;

        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Game mode
        if ( preferencesDialog.basicModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, GameModes.BASIC_MODE);
        }
        if ( preferencesDialog.mediumModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, GameModes.MEDIUM_MODE);
        }
        if ( preferencesDialog.advancedModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, GameModes.ADVANCED_MODE);
        }
        if ( preferencesDialog.expertModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, GameModes.EXPERT_MODE);
        }
        if ( preferencesDialog.customModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, GameModes.CUSTOM_MODE);
            settings.setValue(SettingsKeys.CUSTOM_ROW_COUNT, preferencesDialog.customRowsSpinBox.value());
            settings.setValue(SettingsKeys.CUSTOM_COLUMN_COUNT, preferencesDialog.customColumnsSpinBox.value());
            settings.setValue(SettingsKeys.CUSTOM_MINES_COUNT, preferencesDialog.customMinesSpinBox.value());
        }

        // Language
        if ( preferencesDialog.englishLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, Languages.ENGLISH);
        }
        if ( preferencesDialog.polishLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, Languages.POLISH);
        }
        if ( preferencesDialog.germanLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, Languages.GERMAN);
        }

        // Field size
        settings.setValue(SettingsKeys.FIELD_SIZE, preferencesDialog.fieldSizeSpinBox.value());

        // Time counting and save best result
        if ( preferencesDialog.enableTimeCountingCheckBox.isChecked() )
        {
            settings.setValue(SettingsKeys.ENABLE_TIME_COUNTING, true);
            settings.setValue(SettingsKeys.SAVE_BEST_RESULT, preferencesDialog.saveBestResultCheckBox.isChecked());
        }
        else
        {
            settings.setValue(SettingsKeys.ENABLE_TIME_COUNTING, false);
            settings.setValue(SettingsKeys.SAVE_BEST_RESULT, false);
        }

        settings.endGroup();
    }

    public void loadSettingsForMainWidget()
    {
        MainWidget mainWidget = (MainWidget)targetWidget;

        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);
        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Set field size
        String fieldSize = settings.value(SettingsKeys.FIELD_SIZE, FieldSizes.DEFAULT_FIELD_SIZE).toString();
        mainWidget.setFieldSize( Integer.parseInt(fieldSize) );

        // Time counting and save best result
        String enableTimeCounting = settings.value(SettingsKeys.ENABLE_TIME_COUNTING, true).toString();
        String saveBestResult = settings.value(SettingsKeys.SAVE_BEST_RESULT, true).toString();
        mainWidget.enableTimeCounting = Boolean.parseBoolean( enableTimeCounting );
        mainWidget.saveBestResult = Boolean.parseBoolean( saveBestResult );

        // Game mode (default: BASIC_MODE)
        GameModes gameMode = (GameModes) settings.value(SettingsKeys.GAME_MODE, GameModes.BASIC_MODE);

        // The reason this function is called here is explained in it's description below
        mainWidget.createActionGroups();

        switch ( gameMode )
        {
            case BASIC_MODE:
                mainWidget.initialGameMode = GameModes.BASIC_MODE;
                break;
            case MEDIUM_MODE:
                mainWidget.initialGameMode = GameModes.MEDIUM_MODE;
                break;
            case ADVANCED_MODE:
                mainWidget.initialGameMode = GameModes.ADVANCED_MODE;
                break;
            case EXPERT_MODE:
                mainWidget.initialGameMode = GameModes.EXPERT_MODE;
                break;
            case CUSTOM_MODE:
                mainWidget.initialGameMode = GameModes.CUSTOM_MODE;
                break;
        }
        mainWidget.setRowCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_ROW_COUNT, "10").toString() ) );
        mainWidget.setColumnCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_COLUMN_COUNT, "10").toString() ) );
        mainWidget.setMinesCount( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_MINES_COUNT, "25").toString() ) );

        // TEST - language settings
        String language = settings.value(SettingsKeys.LANGUAGE, Languages.ENGLISH).toString();
        mainWidget.mw.changeTranslator(language);

        settings.endGroup();
    }
}
