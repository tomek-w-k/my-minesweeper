package com.company.dialogs;

import com.company.constants.Stylesheets;
import com.trolltech.qt.gui.*;

public class SaveBestResultDialog extends QDialog
{
    private static final int ACCEPTED = 1;

    // By passing this QDialog as a parent for this layout, the layout is automatically set as a main layout for this dialog window ( don't need to call setLayout() )
    QVBoxLayout contentLayout = new QVBoxLayout(this);

    QHBoxLayout playerNameLayout = new QHBoxLayout();
    QHBoxLayout saveButtonLayout = new QHBoxLayout();

    QLabel saveBestResultLabel = new QLabel(tr("Save best result"));
    String informationText = tr("You have achieved the best time in this game mode. Your result will be saved.\nPlease enter your name below.\n\nIf you don't want to save your result just leave field below blank and hit Enter or close this window. ");
    QLabel informationLabel = new QLabel(informationText, this);
    QLabel playerNameLabel = new QLabel(tr("Name:"), this);
    QLineEdit playerNameLineEdit = new QLineEdit(this);

    QPushButton savePushButton = new QPushButton(tr("Save"), this);

    private static String playerName;

    public SaveBestResultDialog()
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
        if ( !playerNameLineEdit.text().isBlank() )
        {
            playerName = playerNameLineEdit.text();
            this.accept();
        }
        else
        {
            int ret = QMessageBox.question(this, tr("Best result"),
                    tr("Do you really want to skip saving your result?"),
                    QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);

            if ( ret == QMessageBox.StandardButton.valueOf("Yes").value() ) this.reject();
        }
    }

}
