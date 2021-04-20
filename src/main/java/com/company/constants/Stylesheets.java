package com.company.constants;

public class Stylesheets
{
    private Stylesheets() {}

    /*
        Object names
     */
    public static final String M_BUTTON = "mButton";
    public static final String M_OBJECT = "mObject";

    /*
        Dialog windows decoration purple label
     */
    public static final String DIALOG_DECORATION_LABEL = "background-color: rgb(175, 191, 250); padding: 3px;";

    /*
        Status bar
     */
    public static final String STATUS_BAR = "QStatusBar::item{border: 0px; padding: 20px}";

    /*
        Exploded mine field (red)
     */
    public static final String EXPLODED_MINE_FIELD = "#" + M_OBJECT + " { border: 1px solid black; background-color: red; }";

    /*
        Incorrectly marked field (yellow)
     */
    public static final String INCORRECT_MARKED_FIELD = "#" + M_BUTTON + " { font-weight: bold; color: black; background-color: yellow; }";

    /*
        Flagged field
     */
    public static final String FLAGGED_FIELD = "#" + M_BUTTON + " { font-weight: bold; color: black; }";

    /*
        Empty uncovered field
     */
    public static final String EMPTY_UNCOVERED_FIELD = "#" + M_OBJECT + " { border: 1px solid lightgray; }";

    /*
        Mined field
     */
    public static final String MINED_FIELD = "#" + M_OBJECT + " { font-weight: bold; color: black; border: 1px solid lightgray; }";
}
