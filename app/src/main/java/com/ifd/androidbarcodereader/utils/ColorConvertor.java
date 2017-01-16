package com.ifd.androidbarcodereader.utils;

/**
 * Created by LenVo on 1/15/17.
 */

public class ColorConvertor {
    public static int convertStringColorToInt(final String color)
    {
        switch (color)
        {
            case "Black":
                return 0xFF000000;
            default:
                return 0;
        }
    }

    public static String convertIntColorToString(final int color)
    {
        switch (color)
        {
            case 0xFF000000:
                return "Black";
            default:
                return "Black";
        }
    }
}
