package com.company;

import com.company.constants.Stylesheets;
import com.trolltech.qt.gui.*;

public class SaveBestResultDialog extends QDialog
{
    QVBoxLayout contentLayout = new QVBoxLayout(this);

    QHBoxLayout playerNameLayout = new QHBoxLayout(this);
    QHBoxLayout saveButtonLayout = new QHBoxLayout(this);

    QLabel saveBestResultLabel = new QLabel(tr("Save best result"));
    String informationText = tr("You have achieved the best time in this game mode. Your result will be saved. Please enter your name below.");
    QLabel informationLabel = new QLabel(informationText, this);
    QLabel playerNameLabel = new QLabel(tr("Name:"), this);
    QLineEdit playerNameLineEdit = new QLineEdit(this);

    QPushButton savePushButton = new QPushButton(tr("Save"), this);

    private static String playerName;

    SaveBestResultDialog()
    {
        playerName = null;
        setWindowTitle(tr("Best result - MyMinesweeper"));

        packOnLayouts();
        connectToSlots();
    }

    /*
        This method places all widgets on layouts  and sets a layout for a window
    */
    private void packOnLayouts()
    {
        // "Save best result" label
        QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        byte b = 0;
        sizePolicy.setHorizontalStretch(b);
        sizePolicy.setVerticalStretch(b);
        sizePolicy.setHeightForWidth(saveBestResultLabel.sizePolicy().hasHeightForWidth());
        saveBestResultLabel.setSizePolicy(sizePolicy);
        saveBestResultLabel.setStyleSheet(Stylesheets.DIALOG_DECORATION_LABEL);
        contentLayout.addWidget(saveBestResultLabel);

        // Information label
        informationLabel.setSizePolicy(sizePolicy);
        informationLabel.setWordWrap(true);
        contentLayout.addWidget(informationLabel);

        // Layout for name label and name line edit
        playerNameLayout.addWidget(playerNameLabel);
        playerNameLayout.addWidget(playerNameLineEdit);
        contentLayout.addLayout(playerNameLayout);

        // Horizontal line
        QFrame horizontalLine = new QFrame(this);
        horizontalLine.setFrameShape(QFrame.Shape.HLine);
        horizontalLine.setFrameShadow(QFrame.Shadow.Plain);
        contentLayout.addWidget(horizontalLine);

        // Layout for save button
        saveButtonLayout.addStretch();
        saveButtonLayout.addWidget(savePushButton);
        contentLayout.addLayout(saveButtonLayout);

        this.setLayout(contentLayout);
    }

    private void connectToSlots()
    {
        savePushButton.clicked.connect(this, "savePushButtonClicked()");
    }

    public String getPlayerName()
    {
        return playerName;
    }

    // slots
    private void savePushButtonClicked()
    {
        playerName = playerNameLineEdit.text();
        this.close();
    }

}
