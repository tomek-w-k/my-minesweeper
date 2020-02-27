package com.company;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        newGameAction.setShortcut("F2");
        menuBar.addAction(gameMenu.menuAction());
        gameMenu.addAction(newGameAction);
        gameMenu.addSeparator();
        basicAction.setShortcut("F5");
        gameMenu.addAction(basicAction);
        mediumAction.setShortcut("F6");
        gameMenu.addAction(mediumAction);
        advancedAction.setShortcut("F7");
        gameMenu.addAction(advancedAction);
        expertAction.setShortcut("F8");
        gameMenu.addAction(expertAction);
        customAction.setShortcut("F9");
        gameMenu.addAction(customAction);
        gameMenu.addSeparator();
        exitAction.setShortcut("Alt+F4");
        gameMenu.addAction(exitAction);

        menuBar.addAction(optionsMenu.menuAction());
        preferencesAction.setShortcut("Ctrl+P");
        optionsMenu.addAction(preferencesAction);

        menuBar.addAction(menuHelp.menuAction());
        menuHelp.addAction(bestResultsAction);
        menuHelp.addAction(aboutAction);

        this.setMenuBar(menuBar);
    }

    private void createToolBar()
    {
        newGameAction.setIcon( new QIcon( "classpath:resources/icons/new_game.png" ) );
        toolBar.addAction(newGameAction);
        toolBar.addSeparator();
        basicAction.setIcon( new QIcon( "classpath:resources/icons/level_beginner.png" ) );
        toolBar.addAction(basicAction);
        mediumAction.setIcon( new QIcon( "classpath:resources/icons/level_medium.png" ) );
        toolBar.addAction(mediumAction);
        advancedAction.setIcon( new QIcon( "classpath:resources/icons/level_advanced.png" ) );
        toolBar.addAction(advancedAction);
        expertAction.setIcon( new QIcon( "classpath:resources/icons/level_expert.png" ) );
        toolBar.addAction(expertAction);
        toolBar.addAction(customAction);
        customAction.setIcon( new QIcon( "classpath:resources/icons/level_custom.png" ) );
        toolBar.addSeparator();
        preferencesAction.setIcon( new QIcon( "classpath:resources/icons/preferences.png" ) );
        toolBar.addAction(preferencesAction);

        this.addToolBar(toolBar);
    }

    private void createStatusBar()
    {
        statusBar.setSizeGripEnabled(false);
        statusBar.setStyleSheet("QStatusBar::item{border: 0px; padding: 20px}");
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
        polishTranslator.load("classpath:/resources/translations/my_minesweeper_translation_pl.qm");

        QTranslator englishTranslator = new QTranslator();
        englishTranslator.load("classpath:/resources/translations/my_minesweeper_translation_en.qm");

        QTranslator germanTranslator = new QTranslator();
        germanTranslator.load("classpath:/resources/translations/my_minesweeper_translation_de.qm");

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
        setWindowIcon( new QIcon("classpath:/resources/icons/mine.png") );
        connectActionsToSlots();
        createMenuBar();
        createToolBar();
        createStatusBar();
        setCentralWidget( mainWidget );
    }

    public static void main(String[] args)
    {
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
        about.setIconPixmap(new QPixmap("classpath:/resources/icons/mine.png").scaled(88, 88));

        String messageText = "";
        try
        {
            URL res = getClass().getClassLoader().getResource("resources/misc/about.html");
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
