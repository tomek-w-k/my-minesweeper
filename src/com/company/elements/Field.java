package com.company.elements;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;

public class Field
{
    private QLabel label;
    private QPushButton pushButton;

    public Field() {}

    public Field(QLabel label, QPushButton pushButton)
    {
        this.label = label;
        this.pushButton = pushButton;
    }

    public QLabel getLabel()
    {
        return this.label;
    }

    public void setLabel(QLabel label)
    {
        this.label = label;
    }

    public QPushButton getPushButton()
    {
        return pushButton;
    }

    public void setPushButton(QPushButton pushButton)
    {
        this.pushButton = pushButton;
    }
}
