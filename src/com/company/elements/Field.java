package com.company.elements;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

public class Field
{
    private QWidget parent;

    private QLabel label;
    private FieldButton fieldButton;

    public Field() {}

    public Field(QWidget parent)
    {
        this.parent = parent;

        this.label = new QLabel("");
        this.fieldButton = new FieldButton("", parent);
    }

    public Field(QLabel label, FieldButton fieldButton)
    {
        this.label = label;
        this.fieldButton = fieldButton;
    }

    public QLabel getLabel()
    {
        return this.label;
    }

    public void setLabel(QLabel label)
    {
        this.label = label;
    }

    public FieldButton getFieldButton()
    {
        return fieldButton;
    }

    public void setFieldButton(FieldButton fieldButton) { this.fieldButton = fieldButton; }
}
