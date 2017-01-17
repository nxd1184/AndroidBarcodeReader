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
            case "Blue":
                return 0xFF0000FF;
            case "Purple":
                return 0xFF990099;
            case "Red":
                return 0xFFFF6666;
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
            case 0xFF0000FF:
                return "Blue";
            case 0xFF990099:
                return "Purple";
            case 0xFFFF6666:
                return "Red";
            default:
                return "Black";
        }
    }
}
