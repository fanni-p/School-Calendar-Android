package com.finalproject.schoolcalendar.helpers;

import com.finalproject.schoolcalendar.enums.ColorEnum;

/**
 * Created by Fani on 11/15/13.
 */
public class ColorConverter {
    private static String mColor;

    public static String ParseColor(String color){
        switch (ColorEnum.valueOf(color)) {
            case LightCoral:
                mColor = "#FFF08080";
                break;
            case Crimson:
                mColor = "#FFDC143C";
                break;
            case LightPink:
                mColor = "#FFFFB6C1";
                break;
            case PaleVioletRed:
                mColor = "#FFDB7093";
                break;
            case LightSalmon:
                mColor = "#FFFFA07A";
                break;
            case Orange:
                mColor = "#FFFFA500";
                break;
            case LightYellow:
                mColor = "#FFFFFFE0";
                break;
            case PaleGoldenrod:
                mColor = "#FFEEE8AA";
                break;
            case Khaki:
                mColor = "#FFF0E68C";
                break;
            case Lavender:
                mColor = "#FFE6E6FA";
                break;
            case MediumOrchid:
                mColor = "#FFBA55D3";
                break;
            case MediumSlateBlue:
                mColor = "#FF7B68EE";
                break;
            case GreenYellow:
                mColor = "#FFADFF2F";
                break;
            case YellowGreen:
                mColor = "#FF9ACD32";
                break;
            case LightGreen:
                mColor = "#FF90EE90";
                break;
            case MediumAquamarine:
                mColor = "#FF66CDAA";
                break;
            case CadetBlue:
                mColor = "#FF5F9EA0";
                break;
            case SkyBlue:
                mColor = "#FF87CEEB";
                break;
            case RoyalBlue:
                mColor = "#FF4169E1";
                break;
            case NavajoWhite:
                mColor = "#FFFFDEAD";
                break;
            case Wheat:
                mColor = "#FFF5DEB3";
                break;
            case MediumBlue:
                mColor = "#FF0000CD";
                break;
            case MistyRose:
                mColor = "#FFFFE4E1";
                break;
            case DarkGray:
                mColor = "#FFA9A9A9";
                break;
            case Purple:
                mColor = "#FF800080";
                break;
            case Green:
                mColor = "#FF008000";
                break;
        }

        return mColor;
    }
}
