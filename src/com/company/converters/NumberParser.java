package com.company.converters;

public class NumberParser
{
    public static boolean isInteger( String lStr )
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
}
