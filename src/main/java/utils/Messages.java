package utils;

public class Messages {
    public static final String HELP_INFORMATION = "If you want to check up one file you should use " +
            "the script in such a way: java -jar checklinks.jar file.txt \n If you want to check up " +
            "to 4 files at once you should to use the script in such a way: java -jar checklinks" +
            ".jar file.txt file2.txt file3.txt file4.txt\n";
    public static final String ERROR_READ_FILES = "Please write only the name of file at the end of " +
            "the line.";
    public static final String NAME_CURRENT_FILE = "Now checking the file: ";
    public static final String CREATIVE_NAME_NOT_FOUND_IN_HTML = "Creative name not found in HTML";
    public static final String CREATIVE_NAME_IS = "Creative name is: ";
    public static final String TEMPLATE_VERSION = "Template version is: ";
    public static final String LOOPME_BASE_VERSION = "LoopMe Base component version is: ";
    public static final String SIZE_OF_LOADING_PAGE = "Loading page has size: ";
    public static final String INCORRECT_SYMBOL = "This link has invalid symbol, please check and delete incorrect symbol: ";
    public static final String SIZE_OF_FILE = "Size of file: ";
    public static final String TOTAL_SIZE_OF_ASSETS = "Total size of assets is: ";
    public static final String DROPBOX = "\n\nPLEASE CHECK ASSETS!!!  THERE IS DROPBOX " + "LINKS!!!!\n\n";
    public static final String CACHEBUSTER = "\n\nPLEASE CHECK ASSETS!!!  THERE IS CACHEBUSTER IN LINKS!!!!\n\n";
    public static final String FILE_NOT_FOUND = "Please check the file: ";
    public static final String FILE_CHECKED = "\nHTML tag was checked, file was written in the following folder \"New File\"";
    public static final String ORTB_ATTRIBUTES =
            "1 - if video has audio on when it start auto-play\n"
          + "4 - expandable banner\n"
          + "6 - RM embedded auto play video \n"
          + "7 - RM embedded click to play\n"
          + "11 - survey creative (good to auto detects as well, can be easily added to survey builder)\n"
          + "13 - RM ads that allow user to browse around/play with it. e.g. gallery etc templates\n"
          + "15 - audio button on video\n";
    public static final String ERROR_ATTRIBUTES = "There is no comment with creative attributes";
    public static final String ERROR_TYPE = "There are no types of creative";
    public static final String TYPE_CREATIVE = "Types of creative are: ";
    public static final String TYPES_OF_CREATIVE =
          "EV - Embedded video\n"
          + "PV - portrait video\n"
          + "GALV - gallery video\n"
          + "GALI - gallery images\n"
          + "CAL- add to calendar\n"
          + "TILT - Tilt or shake\n"
          + "MAP - Store locator\n"
          + "PLAX - Parallax\n"
          + "MIC - use of microphone\n"
          + "HAP - vibration / haptic feedback\n"
          + "WIP - wipe to reveal or drawing /colouring\n"
          + "CAM - use of camera\n"
          + "GAME - Gamification\n"
          + "360 - videos, images objects\n"
          + "FORM - Any form fills. including tick boxes";
}
