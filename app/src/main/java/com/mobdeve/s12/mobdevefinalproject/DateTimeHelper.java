package com.mobdeve.s12.mobdevefinalproject;

public class DateTimeHelper {

    // Returns a String of the time in the format HH:MM //
    public static String reformatTimeString(String hour, String minutes) {
        String newTime;
        String newHour;
        String newMinutes;

        // Add a leading zero for the given range //
        if (Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) <= 9) {
            newHour = "0" + hour;
        } else {
            newHour = hour;
        }

        if (Integer.parseInt(minutes) >= 0 && Integer.parseInt(minutes) <= 9) {
            newMinutes = "0" + minutes;
        } else {
            newMinutes = minutes;
        }

        newTime = newHour + ":" + newMinutes;
        return newTime;
    }

    // Convert year, month, day to YYYY-MM-DD string //
    public static String convertToDate(String year, String month, String day) {
        String newMonth = month;
        String newDay = day;

        if (month.length() == 1) {
            newMonth = "0" + month;
        }

        if (day.length() == 1) {
            newDay = "0" + day;
        }

        String newDate = year + "-" + newMonth + "-" + newDay;

        return newDate;
    }

    // Return a month as a number string, for example: "January" returns "1" //
    public static String getIntegerMonth(String month) {
        String integerMonth = "";

        switch (month) {
            case "January":
                integerMonth = "1";
                break;
            case "February":
                integerMonth = "2";
                break;
            case "March":
                integerMonth = "3";
                break;
            case "April":
                integerMonth = "4";
                break;
            case "May":
                integerMonth = "5";
                break;
            case "June":
                integerMonth = "6";
                break;
            case "July":
                integerMonth = "7";
                break;
            case "August":
                integerMonth = "8";
                break;
            case "September":
                integerMonth = "9";
                break;
            case "October":
                integerMonth = "10";
                break;
            case "November":
                integerMonth = "11";
                break;
            default:
                integerMonth = "12";
                break;
        }

        return integerMonth;
    }

}
