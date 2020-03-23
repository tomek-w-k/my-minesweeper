package com.company.tools;

import com.company.elements.Field;
import com.company.elements.FieldButton;
import com.company.enums.AdjacentFieldRelativePos;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;

public class AdjacentFieldResolver
{
    private Field[][] fields;

    public AdjacentFieldResolver(Field[][] fields)
    {
        this.fields = fields;
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
                field.setLabel( fields[baseFieldRow - 1][baseFieldColumn].getLabel() );
                field.setFieldButton( fields[baseFieldRow - 1][baseFieldColumn].getFieldButton() );
                break;
            case DOWN:
                field.setLabel( fields[baseFieldRow + 1][baseFieldColumn].getLabel() );
                field.setFieldButton( fields[baseFieldRow + 1][baseFieldColumn].getFieldButton() );
                break;
            case LEFT:
                field.setLabel( fields[baseFieldRow][baseFieldColumn - 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow][baseFieldColumn - 1].getFieldButton() );
                break;
            case RIGHT:
                field.setLabel( fields[baseFieldRow][baseFieldColumn + 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow][baseFieldColumn + 1].getFieldButton() );
                break;
            case UP_LEFT:
                field.setLabel( fields[baseFieldRow - 1][baseFieldColumn - 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow - 1][baseFieldColumn - 1].getFieldButton() );
                break;
            case UP_RIGHT:
                field.setLabel( fields[baseFieldRow - 1][baseFieldColumn + 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow - 1][baseFieldColumn + 1].getFieldButton() );
                break;
            case DOWN_LEFT:
                field.setLabel( fields[baseFieldRow + 1][baseFieldColumn - 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow + 1][baseFieldColumn - 1].getFieldButton() );
                break;
            case DOWN_RIGHT:
                field.setLabel( fields[baseFieldRow + 1][baseFieldColumn + 1].getLabel() );
                field.setFieldButton( fields[baseFieldRow + 1][baseFieldColumn + 1].getFieldButton() );
                break;
        }
        return field;
    }
}
