package com.company;

import com.company.constants.GameModeAreaParams;
import com.company.constants.Stylesheets;
import com.trolltech.qt.gui.*;

public class CustomLevelDialog extends QDialog
{
    public static final int ROWS_LABEL_LAYOUT_ROW = 0;
    public static final int ROWS_LABEL_LAYOUT_COLUMN = 0;
    public static final int COLUMNS_LABEL_LAYOUT_ROW = 1;
    public static final int COLUMNS_LABEL_LAYOUT_COLUMN = 0;
    public static final int MINES_LABEL_LAYOUT_ROW = 2;
    public static final int MINES_LABEL_LAYOUT_COLUMN = 0;
    public static final int ROWS_LINE_EDIT_LAYOUT_ROW = 0;
    public static final int ROWS_LINE_EDIT_LAYOUT_COLUMN = 1;
    public static final int COLUMNS_LINE_EDIT_LAYOUT_ROW = 1;
    public static final int COLUMNS_LINE_EDIT_LAYOUT_COLUMN = 1;
    public static final int MINES_LINE_EDIT_LAYOUT_ROW = 2;
    public static final int MINES_LINE_EDIT_LAYOUT_COLUMN = 1;

    private static Integer customModeRowCount = GameModeAreaParams.BASIC_MODE_ROW_COUNT;
    private static Integer customModeColumnCount = GameModeAreaParams.BASIC_MODE_COLUMN_COUNT;
    private static Integer customModeMinesCount = GameModeAreaParams.BASIC_MODE_MINES_COUNT;

    QVBoxLayout contentLayout = new QVBoxLayout(this);
    QGridLayout parametersGridLayout = new QGridLayout(this);
    QHBoxLayout okCancelButtonsLayout = new QHBoxLayout(this);

    QLabel customModeSettingsLabel = new QLabel(tr("Custom mode settings"));
    QLabel rowsLabel = new QLabel(tr("Rows"), this);
    QLineEdit rowsLineEdit = new QLineEdit(this);
    QLabel columnsLabel = new QLabel(tr("Columns"), this);
    QLineEdit columnsLineEdit = new QLineEdit(this);
    QLabel minesLabel = new QLabel(tr("Mines"), this);
    QLineEdit minesLineEdit = new QLineEdit(this);
    QPushButton okButton = new QPushButton(tr("&Ok"), this);
    QPushButton cancelButton = new QPushButton(tr("&Cancel"), this);

    public CustomLevelDialog(QWidget parent)
    {
        this.setWindowTitle(tr("Custom mode - MyMinesweeper"));

        packOnLayouts();
        connectToSlots();
    }

    private void packOnLayouts()
    {
        // "Custom mode settings" label
        QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        byte b = 0;
        sizePolicy.setHorizontalStretch(b);
        sizePolicy.setVerticalStretch(b);
        sizePolicy.setHeightForWidth(customModeSettingsLabel.sizePolicy().hasHeightForWidth());
        customModeSettingsLabel.setSizePolicy(sizePolicy);
        customModeSettingsLabel.setStyleSheet(Stylesheets.DIALOG_DECORATION_LABEL);
        contentLayout.addWidget(customModeSettingsLabel);

        rowsLineEdit.setText( customModeRowCount.toString() );
        columnsLineEdit.setText( customModeColumnCount.toString() );
        minesLineEdit.setText( customModeMinesCount.toString() );

        parametersGridLayout.addWidget(rowsLabel, ROWS_LABEL_LAYOUT_ROW, ROWS_LABEL_LAYOUT_COLUMN);
        parametersGridLayout.addWidget(columnsLabel, COLUMNS_LABEL_LAYOUT_ROW, COLUMNS_LABEL_LAYOUT_COLUMN);
        parametersGridLayout.addWidget(minesLabel, MINES_LABEL_LAYOUT_ROW, MINES_LABEL_LAYOUT_COLUMN);
        parametersGridLayout.addWidget(rowsLineEdit, ROWS_LINE_EDIT_LAYOUT_ROW, ROWS_LINE_EDIT_LAYOUT_COLUMN);
        parametersGridLayout.addWidget(columnsLineEdit, COLUMNS_LINE_EDIT_LAYOUT_ROW, COLUMNS_LINE_EDIT_LAYOUT_COLUMN);
        parametersGridLayout.addWidget(minesLineEdit, MINES_LINE_EDIT_LAYOUT_ROW, MINES_LINE_EDIT_LAYOUT_COLUMN);
        contentLayout.addLayout(parametersGridLayout);

        QFrame horizontalLine = new QFrame(this);
        horizontalLine.setFrameShape(QFrame.Shape.HLine);
        horizontalLine.setFrameShadow(QFrame.Shadow.Plain);
        contentLayout.addWidget(horizontalLine);

        okCancelButtonsLayout.addStretch();
        okCancelButtonsLayout.addWidget(okButton);
        okCancelButtonsLayout.addWidget(cancelButton);
        contentLayout.addLayout(okCancelButtonsLayout);

        this.setLayout(contentLayout);
    }

    private void connectToSlots()
    {
        okButton.clicked.connect(this, "okButtonClicked()");
        cancelButton.clicked.connect(this, "cancelButtonClicked()");
    }

    public int getCustomModeRowCount()
    {
        return customModeRowCount;
    }

    public int getCustomModeColumnCount()
    {
        return customModeColumnCount;
    }

    public int getCustomModeMinesCount()
    {
        return customModeMinesCount;
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

    // slots
    private void okButtonClicked()
    {
        int lRowCount, lColumnCount, lMinesCount;
        String messageString = tr("Entered values have to be an integer numbers. \n") +
                tr("\nA game area cannot be smaller than 10 x 10 and greather \n") +
                tr("than 30 x 50.") +
                tr("Number of mines cannot be smaller than 1 and bigger than a number of rows and columns product.\n") +
                tr("\nPlease correct entered values.");


        if ( isInteger( rowsLineEdit.text() ) && isInteger( columnsLineEdit.text() ) && isInteger( minesLineEdit.text() )  )
        {
            lRowCount = Integer.parseInt( rowsLineEdit.text() );
            lColumnCount = Integer.parseInt( columnsLineEdit.text() );
            lMinesCount = Integer.parseInt( minesLineEdit.text() );

            if ( !(lRowCount < 10 || lRowCount > 30 ||
                    lColumnCount < 10 || lColumnCount > 50 ||
                    lMinesCount < 1 || lMinesCount > (lRowCount*lColumnCount)) )
            {
                customModeRowCount = Integer.parseInt( rowsLineEdit.text() );
                customModeColumnCount = Integer.parseInt( columnsLineEdit.text() );
                customModeMinesCount = Integer.parseInt( minesLineEdit.text() );
                this.close();
            }
            else
            {
                QMessageBox.critical(this, tr("Custom mode - MyMinesweeper"), messageString );
            }
        }
        else
        {
            QMessageBox.critical(this, tr("Custom mode - MyMinesweeper"), messageString );
        }
    }

    private void cancelButtonClicked()
    {
        customModeRowCount = GameModeAreaParams.BASIC_MODE_ROW_COUNT;
        customModeColumnCount = GameModeAreaParams.BASIC_MODE_COLUMN_COUNT;
        customModeMinesCount = GameModeAreaParams.BASIC_MODE_MINES_COUNT;
        close();
    }
}
