package com.company;

import com.company.constants.SettingsKeys;
import com.company.constants.Stylesheets;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

public class PreferencesDialog extends QDialog
{
    public static final int ROWS_LABEL_LAYOUT_ROW = 0;
    public static final int ROWS_LABEL_LAYOUT_COLUMN = 0;
    public static final int COLUMNS_LABEL_LAYOUT_ROW = 1;
    public static final int COLUMNS_LABEL_LAYOUT_COLUMN = 0;
    public static final int MINES_LABEL_LAYOUT_ROW = 2;
    public static final int MINES_LABEL_LAYOUT_COLUMN = 0;
    public static final int ROWS_SPIN_BOX_LAYOUT_ROW = 0;
    public static final int ROWS_SPIN_BOX_LAYOUT_COLUMN = 1;
    public static final int COLUMNS_SPIN_BOX_LAYOUT_ROW = 1;
    public static final int COLUMNS_SPIN_BOX_LAYOUT_COLUMN = 1;
    public static final int MINES_SPIN_BOX_LAYOUT_ROW = 2;
    public static final int MINES_SPIN_BOX_LAYOUT_COLUMN = 1;

    // Layouts and widgets
    MainWidget parentWidget;

    QStackedLayout preferencesStackedLayout;

    QVBoxLayout buttonGroupLayout;
    QWidget buttonGroupWidget;

    QWidget preferencesWidget;

    QWidget startupWidget;
    QWidget languageWidget;
    QWidget otherWidget;

    // Controls - buttons to switch between pages
    QButtonGroup preferencesButtonGroup;
    QPushButton startupButton = new QPushButton(tr("&Startup"), this);
    QPushButton languageButton = new QPushButton(tr("&Language"), this);
    QPushButton otherButton = new QPushButton(tr("&Other"), this);

    // Controls - game mode
    QGroupBox gameModeGroupBox = new QGroupBox(tr("Game mode"), this);
    QRadioButton basicModeRadioButton = new QRadioButton(tr("&Basic"), this);
    QRadioButton mediumModeRadioButton = new QRadioButton(tr("&Medium"), this);
    QRadioButton advancedModeRadioButton = new QRadioButton(tr("&Advanced"), this);
    QRadioButton expertModeRadioButton = new QRadioButton(tr("&Expert"), this);
    QRadioButton customModeRadioButton = new QRadioButton(tr("&Custom"), this);
    QLabel customRowsLabel = new QLabel(tr("Rows"), this);
    QLabel customColumnsLabel = new QLabel(tr("Columns"), this);
    QLabel customMinesLabel = new QLabel(tr("Mines"), this);
    QSpinBox customRowsSpinBox = new QSpinBox(this);
    QSpinBox customColumnsSpinBox = new QSpinBox(this);
    QSpinBox customMinesSpinBox = new QSpinBox(this);

    // Controls - language
    QGroupBox languageGroupBox = new QGroupBox(tr("Language"), this);
    QRadioButton englishLanguageRadioButton = new QRadioButton(tr("English"), this);
    QRadioButton polishLanguageRadioButton = new QRadioButton(tr("Polish"), this);
    QRadioButton germanLanguageRadioButton = new QRadioButton(tr("German"), this);

    // Controls - field size
    QGroupBox fieldSizeGroupBox = new QGroupBox(tr("Field size"), this);
    QSlider fieldSizeSlider = new QSlider(Qt.Orientation.Horizontal, this);
    QSpinBox fieldSizeSpinBox =  new QSpinBox(this);

    // Controls - time counting
    QGroupBox timeCountingGroupBox = new QGroupBox(tr("Time counting"), this);
    QCheckBox enableTimeCountingCheckBox = new QCheckBox(tr("Enable &time counting"), this);
    QCheckBox saveBestResultCheckBox = new QCheckBox(tr("&Save best result"), this);

    // Action buttons
    QPushButton okPushButton = new QPushButton(tr("&Ok"), this);
    QPushButton applyPushButton = new QPushButton(tr("&Apply"), this);
    QPushButton cancelPushButton = new QPushButton(tr("&Cancel"), this);

    // Controls - misc
    QLabel startupPreferencesPageContentLabel = new QLabel(tr("Application startup settings"), this);
    QLabel languagePreferencesPageContentLabel = new QLabel(tr("Language settings"), this);
    QLabel otherPreferencesPageContentLabel = new QLabel(tr("Other settings"), this);

    // - - - Constructor - - -
    public PreferencesDialog(QWidget parent)
    {
        this.setWindowTitle(tr("Preferences - MyMinesweeper"));

        createPreferencesButtonGroupWidget();

        createStartupPreferencesPageWidget();
        createLanguagePreferencesPageWidget();
        createOtherPreferencesPageWidget();

        packOnLayouts();
        loadSettings();
    }

    // - - - Translation methods - - -
    private void preformTranslation()
    {
        this.setWindowTitle(tr("Preferences - MyMinesweeper"));

        gameModeGroupBox.setTitle(tr("Game mode"));
        languageGroupBox.setTitle(tr("Language"));
        fieldSizeGroupBox.setTitle(tr("Field size"));
        timeCountingGroupBox.setTitle(tr("Time counting"));

        startupButton.setText(tr("&Startup"));
        languageButton.setText(tr("&Language"));
        otherButton.setText(tr("&Other"));

        basicModeRadioButton.setText(tr("&Basic"));
        mediumModeRadioButton.setText(tr("&Medium"));
        advancedModeRadioButton.setText(tr("&Advanced"));
        expertModeRadioButton.setText(tr("&Expert"));
        customModeRadioButton.setText(tr("&Custom"));
        customRowsLabel.setText(tr("Rows"));
        customColumnsLabel.setText(tr("Columns"));
        customMinesLabel.setText(tr("Mines"));

        englishLanguageRadioButton.setText(tr("English"));
        polishLanguageRadioButton.setText(tr("Polish"));
        germanLanguageRadioButton.setText(tr("German"));

        enableTimeCountingCheckBox.setText(tr("Enable &time counting"));
        saveBestResultCheckBox.setText(tr("&Save best result"));

        okPushButton.setText(tr("&Ok"));
        applyPushButton.setText(tr("&Apply"));
        cancelPushButton.setText(tr("&Cancel"));

        startupPreferencesPageContentLabel.setText(tr("Application startup settings"));
        languagePreferencesPageContentLabel.setText(tr("Language settings"));
        otherPreferencesPageContentLabel.setText(tr("Other settings"));
    }

    @Override
    protected void changeEvent(QEvent e)
    {
        if ( e.type() == QEvent.Type.LanguageChange )
        {
            preformTranslation();
        }
    }

    // - - - Methods - - -
    private void createPreferencesButtonGroupWidget()
    {
        preferencesButtonGroup = new QButtonGroup(this);

        startupButton.clicked.connect(this, "startupButtonClicked()");
        startupButton.setCheckable(true);
        startupButton.setChecked(true);

        languageButton.clicked.connect(this, "languageButtonClicked()");
        languageButton.setCheckable(true);

        otherButton.clicked.connect(this, "otherButtonClicked()");
        otherButton.setCheckable(true);

        preferencesButtonGroup.addButton(startupButton);
        preferencesButtonGroup.addButton(languageButton);
        preferencesButtonGroup.addButton(otherButton);
    }

    private void createStartupPreferencesPageWidget()
    {
        QSizePolicy widgetSizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);

        startupWidget = new QWidget(this);
        startupWidget.setSizePolicy(widgetSizePolicy);
        startupWidget.setLayout(new QVBoxLayout(this));

        startupPreferencesPageContentLabel.setStyleSheet(Stylesheets.DIALOG_DECORATION_LABEL);

        QWidget contentWidget = new QWidget(this);
        contentWidget.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Expanding);
        QVBoxLayout contentWidgetLayout = new QVBoxLayout(this);
        contentWidget.setLayout(contentWidgetLayout);

        customModeRadioButton.toggled.connect(this, "customModeRadioButtonToggled(boolean)");

        QWidget customModeParametersWidget = new QWidget(this);
        QGridLayout customModeParametersLayout = new QGridLayout(this);
        customModeParametersWidget.setLayout(customModeParametersLayout);

        customRowsSpinBox.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        customColumnsSpinBox.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        customMinesSpinBox.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);

        if ( !customModeRadioButton.isChecked() )
        {
            customRowsLabel.setDisabled(true);
            customRowsSpinBox.setDisabled(true);
            customColumnsLabel.setDisabled(true);
            customColumnsSpinBox.setDisabled(true);
            customMinesLabel.setDisabled(true);
            customMinesSpinBox.setDisabled(true);
        }

        // Temporarily disable a custom mode to be set as an initial mode
        customModeRadioButton.setDisabled(true);

        customModeParametersLayout.addWidget(customRowsLabel, ROWS_LABEL_LAYOUT_ROW, ROWS_LABEL_LAYOUT_COLUMN);
        customModeParametersLayout.addWidget(customRowsSpinBox, ROWS_SPIN_BOX_LAYOUT_ROW, ROWS_SPIN_BOX_LAYOUT_COLUMN);
        customModeParametersLayout.addWidget(customColumnsLabel, COLUMNS_LABEL_LAYOUT_ROW, COLUMNS_LABEL_LAYOUT_COLUMN);
        customModeParametersLayout.addWidget(customColumnsSpinBox, COLUMNS_SPIN_BOX_LAYOUT_ROW, COLUMNS_SPIN_BOX_LAYOUT_COLUMN);
        customModeParametersLayout.addWidget(customMinesLabel, MINES_LABEL_LAYOUT_ROW, MINES_LABEL_LAYOUT_COLUMN);
        customModeParametersLayout.addWidget(customMinesSpinBox, MINES_SPIN_BOX_LAYOUT_ROW, MINES_SPIN_BOX_LAYOUT_COLUMN);

        gameModeGroupBox.setLayout(new QVBoxLayout(this));
        gameModeGroupBox.layout().addWidget(basicModeRadioButton);
        gameModeGroupBox.layout().addWidget(mediumModeRadioButton);
        gameModeGroupBox.layout().addWidget(advancedModeRadioButton);
        gameModeGroupBox.layout().addWidget(expertModeRadioButton);
        gameModeGroupBox.layout().addWidget(customModeRadioButton);
        gameModeGroupBox.layout().addWidget(customModeParametersWidget);

        contentWidget.layout().addWidget(gameModeGroupBox);
        contentWidgetLayout.addStretch();

        startupWidget.layout().addWidget(startupPreferencesPageContentLabel);
        startupWidget.layout().addWidget(contentWidget);
    }

    private void createLanguagePreferencesPageWidget()
    {
        QSizePolicy widgetSizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);

        languageWidget = new QWidget(this);
        languageWidget.setSizePolicy(widgetSizePolicy);
        languageWidget.setLayout(new QVBoxLayout(this));

        languagePreferencesPageContentLabel.setStyleSheet(Stylesheets.DIALOG_DECORATION_LABEL);

        QWidget contentWidget = new QWidget(this);
        contentWidget.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Expanding);
        QVBoxLayout contentWidgetLayout = new QVBoxLayout(this);
        contentWidget.setLayout(contentWidgetLayout);

        languageGroupBox.setLayout(new QVBoxLayout(this));
        languageGroupBox.layout().addWidget(englishLanguageRadioButton);
        languageGroupBox.layout().addWidget(polishLanguageRadioButton);
        languageGroupBox.layout().addWidget(germanLanguageRadioButton);

        contentWidget.layout().addWidget(languageGroupBox);
        contentWidgetLayout.addStretch();

        languageWidget.layout().addWidget(languagePreferencesPageContentLabel);
        languageWidget.layout().addWidget(contentWidget);
    }

    private void createOtherPreferencesPageWidget()
    {
        // base widgets, layouts and appearance
        QSizePolicy widgetSizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);

        otherWidget = new QWidget(this);
        otherWidget.setSizePolicy(widgetSizePolicy);
        otherWidget.setLayout(new QVBoxLayout(this));

        otherPreferencesPageContentLabel.setStyleSheet(Stylesheets.DIALOG_DECORATION_LABEL);

        QWidget contentWidget = new QWidget(this);
        contentWidget.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Expanding);
        QVBoxLayout contentWidgetLayout = new QVBoxLayout(this);
        contentWidget.setLayout(contentWidgetLayout);

        // Controls
        fieldSizeGroupBox.setLayout(new QHBoxLayout(this));
        fieldSizeSlider.setMinimum(MiscGameParams.MINIMUM_FIELD_SIZE);
        fieldSizeSlider.setMaximum(MiscGameParams.MAXIMUM_FIELD_SIZE);
        fieldSizeSpinBox.setMinimum(MiscGameParams.MINIMUM_FIELD_SIZE);
        fieldSizeSpinBox.setMaximum(MiscGameParams.MAXIMUM_FIELD_SIZE);
        fieldSizeGroupBox.layout().addWidget(fieldSizeSlider);
        fieldSizeGroupBox.layout().addWidget(fieldSizeSpinBox);
        fieldSizeSlider.valueChanged.connect(this, "fieldSizeSliderValueChanged()");
        fieldSizeSpinBox.valueChanged.connect(this, "fieldSizeSpinBoxValueChanged()");

        timeCountingGroupBox.setLayout(new QVBoxLayout(this));
        enableTimeCountingCheckBox.setTristate(false);
        saveBestResultCheckBox.setTristate(false);

        if ( !enableTimeCountingCheckBox.isChecked() )
        {
            saveBestResultCheckBox.setDisabled(true);
        }

        timeCountingGroupBox.layout().addWidget(enableTimeCountingCheckBox);
        timeCountingGroupBox.layout().addWidget(saveBestResultCheckBox);
        enableTimeCountingCheckBox.stateChanged.connect(this, "enableTimeCountingCheckBoxStateChanged()");

        // pack on layouts
        contentWidgetLayout.addWidget(fieldSizeGroupBox);
        contentWidgetLayout.addWidget(timeCountingGroupBox);
        contentWidgetLayout.addStretch();

        otherWidget.layout().addWidget(otherPreferencesPageContentLabel);
        otherWidget.layout().addWidget(contentWidget);
    }

    // Collects data from dialog widgets and saves using QSettings class
    private void saveSettings()
    {
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Game mode
        if ( basicModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.BASIC_MODE);
        }
        if ( mediumModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.MEDIUM_MODE);
        }
        if ( advancedModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.ADVANCED_MODE);
        }
        if ( expertModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.EXPERT_MODE);
        }
        if ( customModeRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.CUSTOM_MODE);
            settings.setValue(SettingsKeys.CUSTOM_ROW_COUNT, customRowsSpinBox.value());
            settings.setValue(SettingsKeys.CUSTOM_COLUMN_COUNT, customColumnsSpinBox.value());
            settings.setValue(SettingsKeys.CUSTOM_MINES_COUNT, customMinesSpinBox.value());
        }

        // Language
        if ( englishLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, MiscGameParams.Languages.ENGLISH);
        }
        if ( polishLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, MiscGameParams.Languages.POLISH);
        }
        if ( germanLanguageRadioButton.isChecked() )
        {
            settings.setValue(SettingsKeys.LANGUAGE, MiscGameParams.Languages.GERMAN);
        }

        // Field size
        settings.setValue(SettingsKeys.FIELD_SIZE, fieldSizeSpinBox.value());

        // Time counting and save best result
        if ( enableTimeCountingCheckBox.isChecked() )
        {
            settings.setValue(SettingsKeys.ENABLE_TIME_COUNTING, true);
            settings.setValue(SettingsKeys.SAVE_BEST_RESULT, saveBestResultCheckBox.isChecked());
        }
        else
        {
            settings.setValue(SettingsKeys.ENABLE_TIME_COUNTING, false);
            settings.setValue(SettingsKeys.SAVE_BEST_RESULT, false);
        }

        settings.endGroup();
    }

    // Reads data from QSettings class and set them on dialog widgets
    private void loadSettings()
    {
        QSettings settings = new QSettings(SettingsKeys.COMPANY, SettingsKeys.APPLICATION);

        settings.beginGroup(SettingsKeys.GAME_PARAMETERS_GROUP);

        // Game mode (default: BASIC_MODE)
        MiscGameParams.GameModes gameMode = (MiscGameParams.GameModes) settings.value(SettingsKeys.GAME_MODE, MiscGameParams.GameModes.BASIC_MODE);

        switch ( gameMode )
        {
            case BASIC_MODE:
                basicModeRadioButton.setChecked(true);
                break;
            case MEDIUM_MODE:
                mediumModeRadioButton.setChecked(true);
                break;
            case ADVANCED_MODE:
                advancedModeRadioButton.setChecked(true);
                break;
            case EXPERT_MODE:
                expertModeRadioButton.setChecked(true);
                break;
            case CUSTOM_MODE:
                customModeRadioButton.setChecked(true);
                break;
            default:
                basicModeRadioButton.setChecked(true);
                break;
        }
        customRowsSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_ROW_COUNT, "10").toString() ) );
        customColumnsSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_COLUMN_COUNT, "10").toString() ) );
        customMinesSpinBox.setValue( Integer.parseInt( settings.value(SettingsKeys.CUSTOM_MINES_COUNT, "25").toString() ) );

        // Language (default: ENGLISH)
        MiscGameParams.Languages language = (MiscGameParams.Languages) settings.value(SettingsKeys.LANGUAGE, MiscGameParams.Languages.ENGLISH);

        switch ( language )
        {
            case ENGLISH:
                englishLanguageRadioButton.setChecked(true);
                break;
            case POLISH:
                polishLanguageRadioButton.setChecked(true);
                break;
            case GERMAN:
                germanLanguageRadioButton.setChecked(true);
                break;
            default:
                englishLanguageRadioButton.setChecked(true);
                break;
        }

        // Field size (default: DEFAULT_FIELD_SIZE)
        String fieldSize = settings.value(SettingsKeys.FIELD_SIZE, MiscGameParams.DEFAULT_FIELD_SIZE).toString();
        fieldSizeSpinBox.setValue( Integer.parseInt(fieldSize) );

        // Time counting and save best result (default: true)
        String enableTimeCounting = settings.value(SettingsKeys.ENABLE_TIME_COUNTING, true).toString();
        String saveBestResult = settings.value(SettingsKeys.SAVE_BEST_RESULT, true).toString();

        enableTimeCountingCheckBox.setChecked( Boolean.parseBoolean(enableTimeCounting) );
        saveBestResultCheckBox.setChecked( Boolean.parseBoolean(saveBestResult) );

        settings.endGroup();
    }

    private void packOnLayouts()
    {
        // left panel with button group
        buttonGroupLayout = new QVBoxLayout(this);
        buttonGroupLayout.addWidget(startupButton);
        buttonGroupLayout.addWidget(languageButton);
        buttonGroupLayout.addWidget(otherButton);
        buttonGroupLayout.addStretch();
        buttonGroupWidget = new QWidget(this);
        buttonGroupWidget.setLayout(buttonGroupLayout);

        // widget that holds pages on the right
        QSizePolicy widgetSizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        preferencesWidget = new QWidget(this);
        preferencesWidget.setSizePolicy(widgetSizePolicy);

        // a stacked layout that holds pages - on a widget above
        preferencesStackedLayout = new QStackedLayout(this);

        preferencesStackedLayout.addWidget(startupWidget);
        preferencesStackedLayout.addWidget(languageWidget);
        preferencesStackedLayout.addWidget(otherWidget);
        preferencesStackedLayout.setCurrentWidget(startupWidget);
        preferencesWidget.setLayout(preferencesStackedLayout);

        // layout that holds all window content - button group on the left and pages on the right
        QHBoxLayout contentLayout = new QHBoxLayout(this);
        contentLayout.addWidget(buttonGroupWidget);
        contentLayout.addWidget(preferencesWidget);
        QWidget contentWidget = new QWidget(this);
        contentWidget.setLayout(contentLayout);

        // vertical line
        QFrame horizontalLine = new QFrame(this);
        horizontalLine.setFrameShape(QFrame.Shape.HLine);
        horizontalLine.setFrameShadow(QFrame.Shadow.Plain);

        // action buttons
        QHBoxLayout actionButtonsLayout = new QHBoxLayout(this);
        actionButtonsLayout.addStretch();
        actionButtonsLayout.addWidget(okPushButton);
        actionButtonsLayout.addWidget(applyPushButton);
        actionButtonsLayout.addWidget(cancelPushButton);
        QWidget actionButtonsWidget = new QWidget(this);
        actionButtonsWidget.setLayout(actionButtonsLayout);
        okPushButton.clicked.connect(this, "okPushButtonClicked()");
        applyPushButton.clicked.connect(this, "applyPushButtonClicked()");
        cancelPushButton.clicked.connect(this, "cancelPushButtonClicked()");

        // main layout that holds content layout, vertical line and action buttons layout
        QVBoxLayout mainLayout = new QVBoxLayout(this);
        mainLayout.addWidget(contentWidget);
        mainLayout.addWidget(horizontalLine);
        mainLayout.addWidget(actionButtonsWidget);

        this.setLayout(mainLayout);
    }

    public void setParentWidget(MainWidget lMw)
    {
        this.parentWidget = lMw;
    }

    // - - - Slots - - -
    private void customModeRadioButtonToggled(boolean toggled)
    {
        customRowsLabel.setEnabled(toggled);
        customColumnsLabel.setEnabled(toggled);
        customMinesLabel.setEnabled(toggled);
        customRowsSpinBox.setEnabled(toggled);
        customColumnsSpinBox.setEnabled(toggled);
        customMinesSpinBox.setEnabled(toggled);
    }

    private void fieldSizeSliderValueChanged()
    {
        fieldSizeSpinBox.setValue(fieldSizeSlider.value());
    }

    private void fieldSizeSpinBoxValueChanged()
    {
        fieldSizeSlider.setValue(fieldSizeSpinBox.value());
    }

    private void okPushButtonClicked()
    {
        saveSettings();

        parentWidget.loadSettings();
        parentWidget.newGameActionTriggered();
        this.close();
    }

    private void applyPushButtonClicked()
    {
        saveSettings();

        parentWidget.loadSettings();
        parentWidget.newGameActionTriggered();
    }

    private void cancelPushButtonClicked()
    {
        this.close();
    }

    private void enableTimeCountingCheckBoxStateChanged()
    {
        if ( enableTimeCountingCheckBox.isChecked() )
        {
            saveBestResultCheckBox.setChecked(false);
            saveBestResultCheckBox.setEnabled(true);
        }
        else
        {
            saveBestResultCheckBox.setChecked(false);
            saveBestResultCheckBox.setEnabled(false);
        }
    }

    private void startupButtonClicked()
    {
        preferencesStackedLayout.setCurrentWidget(startupWidget);
    }

    private void languageButtonClicked()
    {
        preferencesStackedLayout.setCurrentWidget(languageWidget);
    }

    private void otherButtonClicked()
    {
        preferencesStackedLayout.setCurrentWidget(otherWidget);
    }
}
