package com.company.elements;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

public class FieldButton extends QPushButton
{
    public Signal0 rightClicked = new Signal0();

    public FieldButton(String text, QWidget parent)
    {
        this.setText(text);
        this.setFocusPolicy(Qt.FocusPolicy.NoFocus);
    }

    @Override
    protected void mousePressEvent(QMouseEvent e)
    {
        super.mousePressEvent(e);

        if ( e.button() == Qt.MouseButton.RightButton )
        {
            rightClicked.emit();
            this.setDown(true);
        }
    }

    @Override
    protected void mouseReleaseEvent(QMouseEvent e)
    {
        super.mouseReleaseEvent(e);

        if ( e.button() == Qt.MouseButton.RightButton )
        {
            this.setDown(false);
        }
    }
}
