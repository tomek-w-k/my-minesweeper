package com.company.constants;

public class MiscParams
{
    /*
        Following parameters describe how much a main window dimensions exceeds a game area dimensions.
        Game area consists of fields with given dimensions, main window also contains frames, menu bar and tool bar.
        Used for calculate a current main window size and set the size fixed - different for Linux and Windows
     */
    public static final int LINUX_MAIN_WINDOW_WIDTH_EXCESS = 18;
    public static final int LINUX_MAIN_WINDOW_HEIGHT_EXCESS = 105;

    public static final int WINDOWS_MAIN_WINDOW_WIDTH_EXCESS = 18;
    public static final int WINDOWS_MAIN_WINDOW_HEIGHT_EXCESS = 95;

}
