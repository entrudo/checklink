package utils;

public class ScriptСonstants {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String REG_EXP_ALL_LINKS = "[a-z]+:\\/\\/.*?[?',\")]";
    public static final String REG_EXP_CREATIVE_NAME = "CREATIVE_NAME:(\"|').*?(\"|')";
    public static final String REG_EXP_LOOPMEME = "http://loopme.me";
    public static final String REG_EXP_LOOPMECOM = "http://loopme.com";
    public static final String REG_EXP_ILOOPMEME = "http://i.loopme.me";
    public static final String REG_EXP_W3ORG = "http://www.w3.org";
    public static final int RESPONSE_CODE = 200;
    public static final String REG_EXP_EVENT = "\\(\"PAGE_2\"\\),\\(0,.\\.sendCreativeEventOnce\\)(.*?\\))";
    public static final String REG_EXP_EVENT_GWD =
      "\\(\\\"PAGE_2\\\"\\);.*(?:\\r\\n|[\\t\\r\\n]).*sendCreativeEvent(.*?\\))";
}
