package com.company;

import com.company.constants.PathStrings;
import com.company.constants.Shortcuts;
import com.company.constants.Stylesheets;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import java.beans.Introspector;
import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Main extends QMainWindow
{
    QMenuBar menuBar = new QMenuBar();

    QMenu gameMenu = new QMenu(tr("&Game"));
    QAction newGameAction = new QAction(tr("&New game"), this);
    QAction basicAction = new QAction(tr("&Basic"), this);
    QAction mediumAction = new QAction(tr("&Medium"), this);
    QAction advancedAction = new QAction(tr("&Advanced"), this);
    QAction expertAction = new QAction(tr("&Expert"), this);
    QAction customAction = new QAction(tr("&Custom..."), this);
    QAction exitAction = new QAction(tr("E&xit"), this);

    QMenu optionsMenu = new QMenu(tr("&Options"));
    QAction preferencesAction = new QAction(tr("&Preferences..."), this);

    QMenu menuHelp = new QMenu(tr("&Help"));
    QAction bestResultsAction = new QAction(tr("&Best results..."), this);
    QAction aboutAction = new QAction(tr("&About..."), this);

    public QActionGroup difficultyLevelActionGroup = new QActionGroup(this);

    QToolBar toolBar = new QToolBar(this);

    QStatusBar statusBar = new QStatusBar();

    QLabel timeCounterLabel = new QLabel("00:00:00");

    MainWidget mainWidget = new MainWidget(this);


    private void connectActionsToSlots()
    {
        newGameAction.triggered.connect(mainWidget, "newGameActionTriggered()");
        basicAction.triggered.connect(mainWidget, "basicActionTriggered()");
        mediumAction.triggered.connect(mainWidget, "mediumActionTriggered()");
        advancedAction.triggered.connect(mainWidget, "advancedActionTriggered()");
        expertAction.triggered.connect(mainWidget, "expertActionTriggered()");
        customAction.triggered.connect(mainWidget, "customActionTriggered()");
        preferencesAction.triggered.connect(mainWidget, "preferencesActionTriggered()");
        bestResultsAction.triggered.connect(mainWidget, "bestResultsActionTriggered()");
        aboutAction.triggered.connect(this, "aboutActionTriggered()");
        exitAction.triggered.connect(this, "exitActionTriggered()");
    }

    private void createMenuBar()
    {
        newGameAction.setShortcut(Shortcuts.NEW_GAME);
        menuBar.addAction(gameMenu.menuAction());
        gameMenu.addAction(newGameAction);
        gameMenu.addSeparator();
        basicAction.setShortcut(Shortcuts.BEGINNER_LEVEL);
        gameMenu.addAction(basicAction);
        mediumAction.setShortcut(Shortcuts.MEDIUM_LEVEL);
        gameMenu.addAction(mediumAction);
        advancedAction.setShortcut(Shortcuts.ADVANCED_LEVEL);
        gameMenu.addAction(advancedAction);
        expertAction.setShortcut(Shortcuts.EXPERT_LEVEL);
        gameMenu.addAction(expertAction);
        customAction.setShortcut(Shortcuts.CUSTOM_LEVEL);
        gameMenu.addAction(customAction);
        gameMenu.addSeparator();
        exitAction.setShortcut(Shortcuts.EXIT);
        gameMenu.addAction(exitAction);

        menuBar.addAction(optionsMenu.menuAction());
        preferencesAction.setShortcut(Shortcuts.PREFERENCES);
        optionsMenu.addAction(preferencesAction);

        menuBar.addAction(menuHelp.menuAction());
        menuHelp.addAction(bestResultsAction);
        menuHelp.addAction(aboutAction);

        this.setMenuBar(menuBar);
    }

    private void createToolBar()
    {
        newGameAction.setIcon( new QIcon( PathStrings.Icons.NEW_GAME ) );
        toolBar.addAction(newGameAction);
        toolBar.addSeparator();
        basicAction.setIcon( new QIcon( PathStrings.Icons.BEGINNER_LEVEL ) );
        toolBar.addAction(basicAction);
        mediumAction.setIcon( new QIcon( PathStrings.Icons.MEDIUM_LEVEL ) );
        toolBar.addAction(mediumAction);
        advancedAction.setIcon( new QIcon( PathStrings.Icons.ADVANCED_LEVEL ) );
        toolBar.addAction(advancedAction);
        expertAction.setIcon( new QIcon( PathStrings.Icons.EXPERT_LEVEL ) );
        toolBar.addAction(expertAction);
        customAction.setIcon( new QIcon( PathStrings.Icons.CUSTOM_LEVEL ) );
        toolBar.addAction(customAction);
        toolBar.addSeparator();
        preferencesAction.setIcon( new QIcon( PathStrings.Icons.PREFERENCES ) );
        toolBar.addAction(preferencesAction);

        this.addToolBar(toolBar);
    }

    private void createStatusBar()
    {
        statusBar.setSizeGripEnabled(false);
        statusBar.setStyleSheet(Stylesheets.STATUS_BAR);
        statusBar.setContentsMargins(9, 0, 11, 0);

        statusBar.addPermanentWidget( timeCounterLabel );
        this.setStatusBar(statusBar);
    }

    public void updateTime(String currentTime)
    {
        timeCounterLabel.setText(currentTime);
    }

    @Override
    protected void changeEvent(QEvent e)
    {
        if ( e.type() == QEvent.Type.LanguageChange )
        {
            preformTranslation();
        }
    }

    private void preformTranslation()
    {
        gameMenu.setTitle(tr("&Game"));
        newGameAction.setText(tr("&New game"));
        basicAction.setText(tr("&Basic"));
        mediumAction.setText(tr("&Medium"));
        advancedAction.setText(tr("&Advanced"));
        expertAction.setText(tr("&Expert"));
        customAction.setText(tr("&Custom..."));
        exitAction.setText(tr("E&xit"));

        optionsMenu.setTitle(tr("&Options"));
        preferencesAction.setText(tr("&Preferences..."));

        menuHelp.setTitle(tr("&Help"));
        bestResultsAction.setText(tr("&Best results..."));
        aboutAction.setText(tr("&About..."));
    }

    public void changeTranslator(String lLanguage)
    {
        QTranslator polishTranslator = new QTranslator();
        polishTranslator.load(PathStrings.Translations.POLISH_TRANSLATION_FILE);

        QTranslator englishTranslator = new QTranslator();
        englishTranslator.load(PathStrings.Translations.ENGLISH_TRANSLATION_FILE);

        QTranslator germanTranslator = new QTranslator();
        germanTranslator.load(PathStrings.Translations.GERMAN_TRANSLATION_FILE);

        switch ( lLanguage )
        {
            case "ENGLISH":
                QCoreApplication.instance().removeTranslator(polishTranslator);
                QCoreApplication.instance().removeTranslator(germanTranslator);
                QCoreApplication.instance().installTranslator(englishTranslator);
                break;
            case "POLISH":
                QCoreApplication.instance().removeTranslator(englishTranslator);
                QCoreApplication.instance().removeTranslator(germanTranslator);
                QCoreApplication.instance().installTranslator(polishTranslator);
                break;
            case "GERMAN":
                QCoreApplication.instance().removeTranslator(polishTranslator);
                QCoreApplication.instance().removeTranslator(englishTranslator);
                QCoreApplication.instance().installTranslator(germanTranslator);
                break;
            default:
                QCoreApplication.instance().removeTranslator(englishTranslator);
                break;
        }
    }

    public Main()
    {
        setWindowTitle(tr("MyMinesweeper"));
        setWindowIcon( new QIcon(PathStrings.Icons.MINE) );
        connectActionsToSlots();
        createMenuBar();
        createToolBar();
        createStatusBar();
        setCentralWidget( mainWidget );
    }

    public static void main(String[] args)
    {
        //ClassLoader cl = ClassLoader.getPlatformClassLoader();



        QApplication.initialize(args);

        Main mainWindow = new Main();
        mainWindow.show();
        QApplication.instance().exec();
    }

    // slots
    private void exitActionTriggered()
    {
        close();
    }

    private void aboutActionTriggered()
    {
        QMessageBox about = new QMessageBox();
        about.setDefaultButton(QMessageBox.StandardButton.Ok);
        about.setWindowTitle(tr("About - MyMinesweeper"));
        about.setIconPixmap(new QPixmap(PathStrings.Icons.MINE).scaled(88, 88));

        String messageText = "";
        try
        {
            URL res = getClass().getClassLoader().getResource(PathStrings.Misc.ABOUT);
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            messageText = new String(Files.readAllBytes(Paths.get( absolutePath ) ));
        }
        catch(IOException e)
        {
            System.out.println("Cannot load a message file.");
        }
        catch(URISyntaxException e)
        {
            System.out.println("Cannot load a message file.");
        }

        about.setText(messageText);
        about.exec();
    }
}
