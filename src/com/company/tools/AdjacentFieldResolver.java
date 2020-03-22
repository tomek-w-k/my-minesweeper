package com.company.tools;

import com.company.elements.Field;
import com.company.elements.FieldButton;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;

public class AdjacentFieldResolver
{
    private FieldButton[][] fieldButtons;
    private QLabel[][] fieldLabels;

    public AdjacentFieldResolver(FieldButton[][] fieldButtons, QLabel[][] fieldLabels)
    {
        this.fieldButtons = fieldButtons;
        this.fieldLabels = fieldLabels;
    }

    /*
        Returns a specified adjacent field to the given base field described by its coordinates (base row, base column)
     */
    public Field adjacentField(AdjacentFieldRelativePos adjacentFieldRelativePos, Integer baseFieldRow, Integer baseFieldColumn)
    {
        Field field = new Field();

        switch (adjacentFieldRelativePos)
        {
            case UP:
                field.setLabel( fieldLabels[baseFieldRow - 1][baseFieldColumn] );
                field.setPushButton( fieldButtons[baseFieldRow - 1][baseFieldColumn] );
                break;
            case DOWN:
                field.setLabel( fieldLabels[baseFieldRow + 1][baseFieldColumn] );
                field.setPushButton( fieldButtons[baseFieldRow + 1][baseFieldColumn] );
                break;
            case LEFT:
                field.setLabel( fieldLabels[baseFieldRow][baseFieldColumn - 1] );
                field.setPushButton( fieldButtons[baseFieldRow][baseFieldColumn - 1] );
                break;
            case RIGHT:
                field.setLabel( fieldLabels[baseFieldRow][baseFieldColumn + 1] );
                field.setPushButton( fieldButtons[baseFieldRow][baseFieldColumn + 1] );
                break;
            case UP_LEFT:
                field.setLabel( fieldLabels[baseFieldRow - 1][baseFieldColumn - 1] );
                field.setPushButton( fieldButtons[baseFieldRow - 1][baseFieldColumn - 1] );
                break;
            case UP_RIGHT:
                field.setLabel( fieldLabels[baseFieldRow - 1][baseFieldColumn + 1] );
                field.setPushButton( fieldButtons[baseFieldRow - 1][baseFieldColumn + 1] );
                break;
            case DOWN_LEFT:
                field.setLabel( fieldLabels[baseFieldRow + 1][baseFieldColumn - 1] );
                field.setPushButton( fieldButtons[baseFieldRow + 1][baseFieldColumn - 1] );
                break;
            case DOWN_RIGHT:
                field.setLabel( fieldLabels[baseFieldRow + 1][baseFieldColumn + 1] );
                field.setPushButton( fieldButtons[baseFieldRow + 1][baseFieldColumn + 1] );
                break;
        }
        return field;
    }
}
