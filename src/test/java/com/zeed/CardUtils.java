package com.zeed;

import org.springframework.util.StringUtils;

public class CardUtils {
    public CardUtils() {
    }

    public static String maskPanToLastFourDigits(String pan) {
        if (StringUtils.isEmpty(pan)) {
            return pan;
        } else {
            return pan.length() <= 4 ? pan : "************" + pan.substring(pan.length() - 4, pan.length());
        }
    }

    public static String maskPan(String pan) {
        if (StringUtils.isEmpty(pan)) {
            return pan;
        } else {
            return pan.length() <= 4 ? pan : pan.substring(0, 6) + "******" + pan.substring(pan.length() - 4, pan.length());
        }
    }

    public static String maskTrack2(String track2) {
        String delimeter = "D";
        String[] track2Data;
        if (track2.contains("D")) {
            delimeter = "D";
            track2Data = track2.split("D");
        } else {
            if (!track2.contains("=")) {
                if (track2.length() <= 4) {
                    return track2;
                }

                return track2.substring(0, 6) + "***************";
            }

            delimeter = "=";
            track2Data = track2.split("=");
        }

        return maskPan(track2Data[0]) + delimeter + track2Data[1].substring(0, 4) + "*************";
    }
}
