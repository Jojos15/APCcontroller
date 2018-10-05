public class Variables {

    public static int playbacks_offset = 6;

    public static String[] playbacks = {
            "64", "x",
            "65", "c",
            "66", "v",
            "67", "b",
            "68", "n",
            "69", "m",
            "70", ",",
            "71", ".",
            "88", "z",
            "89", "\\"
    };
    public static int stops_offset = 0;
    public static String[] stops = {
            "0", "d",
            "1", "f",
            "2", "g",
            "3", "h",
            "4", "j",
            "5", "k",
            "6", "l",
            "7", ";"
    };
    public static int taps_offset = 2;
    public static String[] taps = {
            "8", "3",
            "9", "4",
            "10", "5",
            "11", "6",
            "12", "7",
            "13", "8",
            "14", "9",
            "15", "0"
    };

    public static int macros_offset = 4;
    public static String[] macros = {
            "56", "q", "TRUE",
            "57", "w", "TRUE",
            "58", "e", "TRUE",
            "59", "r", "TRUE",
            "60", "t", "TRUE",
            "61", "y", "TRUE",
            "62", "u", "TRUE",
            "63", "i", "TRUE"
    };

    public static int faders_offset = 2;
    public static int resolutionY = 768;
    public static int faders_lower_position = 685; //  1366x768
    public static int faders_higher_position = 593; //
    public static int fadersX[] = {  //1366x768
            48, 321,
            49, 418,
            50, 515,
            51, 612,
            52, 709,
            53, 806,
            54, 903,
            55, 1000,
            56, 20,
            54, 127,
            55, 224
    };
    /*public static int resolutionY = 900;
    public static int faders_lower_position = 816; // 1600x900
    public static int faders_higher_position = 725; //
    public static int fadersX[] = { //1600x900
            48, 380,
            49, 500,
            50, 620,
            51, 740,
            52, 860,
            53, 980,
            54, 1100,
            55, 1220,
            56, 20,
            54, 140,
            55, 260
    };*/
    public static int testPixelsY = 448; //1366x768
    public static int testPixelsX[] = { //1366x768
            317, 414, 511, 608, 705, 802, 899, 1002, 123, 220
    };

    /*public static int testPixelsY = 580; // 1600x900
    public static int testPixelsX[] = { //1600x900
            363, 483, 603, 723, 843, 963, 1083, 1203, 123, 243
    };*/

    public static Integer digitPixelsWhite[][] = { //1366x768
            //0
            {1, 0, 1, 7},
            //1
            {1, 1, 1, 7, 2, 7, 3, 7},
            //2
            {1, 0, 2, 0, 0, 7, 1, 7, 2, 7, 3, 7},
            //3
            {1, 0, 2, 0, 0, 7, 1, 7, 2, 7},
            //4
            {0, 4, 1, 4, 2, 4, 3, 4},
            //5
            {1, 0, 2, 0, 1, 3, 2, 3, 1, 7, 2, 7},
            //6
            {1, 0, 2, 0, 0, 3, 1, 3, 2, 3, 1, 7},
            //7
            {0, 0, 1, 0, 2, 0, 3, 0},
            //8
            {1, 0, 1, 3, 1, 7},
            //9
            {1, 0, 1, 4, 2, 4, 0, 7, 1, 7}
    };
    public static int OFF = 0;
    public static int GREEN = 1;
    public static int GREEN_FLASH = 2;
    public static int RED = 3;
    public static int RED_FLASH = 4;
    public static int YELLOW = 5;
    public static int YELLOW_FLASH = 6;


}
