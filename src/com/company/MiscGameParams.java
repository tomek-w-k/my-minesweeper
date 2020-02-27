package com.company;

public class MiscGameParams
{
    // SIZE OF FIELDS
    public static final int DEFAULT_FIELD_SIZE = 30;
    public static final int MINIMUM_FIELD_SIZE = 15;
    public static final int MAXIMUM_FIELD_SIZE = 40;

    // GAME MODES
    public enum GameModes
    {
        BASIC_MODE,
        MEDIUM_MODE,
        ADVANCED_MODE,
        EXPERT_MODE,
        CUSTOM_MODE
    }

    // LANGUAGES
    public enum Languages
    {
        ENGLISH,
        POLISH,
        GERMAN
    }

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
