import htmlPage.HtmlTag;
import httpServer.HttpServer;
import utils.ScriptСonstants;
import utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Messages.*;
import static utils.ScriptСonstants.*;

public class HtmlScript {
    private Utils utils = new Utils();
    private HtmlTag htmlTag;
    private HttpServer server;
    private List<String> urlList;
    private int totalSizeOfAssets;
    private Map<String, Integer> fileSizes = new HashMap<String, Integer>();
    private int count;

    public static void main(String[] args) {
        HtmlScript htmlScript = new HtmlScript();
        htmlScript.runApp(args);
    }

    private void runApp(String[] args) {
        if (args.length == 0 || args.length > 4) {
            utils.printToConsole(ANSI_BOLD + ERROR_READ_FILES + ANSI_RESET);
            return;
        }

        if (args[0].contains("-h")) {
            utils.printToConsole(ANSI_BOLD + HELP_INFORMATION + ANSI_RESET);
            return;
        }

        for (int i = 0; i < args.length; i++) {
            totalSizeOfAssets = 0;
            utils.printToConsole(ANSI_BOLD + "\n" + NAME_CURRENT_FILE + ANSI_RESET + args[i] + "\n");

            String htmlDocument = null;
            try {
                htmlDocument = utils.readHTMLTag(args[i]);
            } catch (IOException e) {
                utils.printToConsole(ANSI_BOLD + FILE_NOT_FOUND + args[i] + ANSI_RESET);
            }

            if (htmlDocument.indexOf("dropbox") > 0) {
                utils.printToConsole(ANSI_BOLD + DROPBOX + ANSI_RESET);
            }

            if (htmlDocument.indexOf("CACHEBUSTER") > 0) {
                utils.printToConsole(ANSI_BOLD + CACHEBUSTER + ANSI_RESET);
            }

            htmlTag = new HtmlTag(htmlDocument);

            if (!htmlTag.getFirstComment().trim().startsWith("<!-- Creative attributes:")) {
                utils.printToConsole(ERROR_ATTRIBUTES);
                utils.printToConsole("");
            } else {
                utils.printToConsole(ANSI_BOLD + htmlTag.getFirstComment() + ANSI_RESET);
                utils.printToConsole(ORTB_ATTRIBUTES);
            }

//            if (!utils.getCreativeType(htmlDocument).equals(ERROR_TYPE)) {
//                utils.printToConsole(ANSI_BOLD + TYPE_CREATIVE + utils.getCreativeType(htmlDocument) + ANSI_RESET);
//                utils.printToConsole(TYPES_OF_CREATIVE);
//            }else {
//                utils.printToConsole(ERROR_TYPE);
//            }


            if (htmlTag.getTitle().length() != 0) {
                checkES6TemplateCreative(htmlDocument);
            } else {
                checkGWDTemplateCreative();
            }

            htmlDocument = utils.replaceLoopMeLinks(htmlDocument, ScriptСonstants.REG_EXP_ILOOPMEME);
            htmlDocument = utils.replaceLoopMeLinks(htmlDocument, ScriptСonstants.REG_EXP_LOOPMEME);
            htmlDocument = utils.replaceLoopMeLinks(htmlDocument, ScriptСonstants.REG_EXP_LOOPMECOM);
            htmlDocument = utils.replaceLoopMeLinks(htmlDocument, ScriptСonstants.REG_EXP_W3ORG);
            htmlDocument = htmlDocument.replaceAll("http://polymer", "https://polymer");

            urlList = utils.getAllUrlFromTag(htmlDocument);

            for (String str : urlList) {
                if (str.contains("%")) {
                    if (!str.startsWith("https://sb.voicefive.com")) {
                        utils.printToConsole(ANSI_BOLD + INCORRECT_SYMBOL + ANSI_RESET + str);
                    }
                    count++;
                }
            }
            if (count > 0) {
                utils.printToConsole("");
                count = 0;
            }

            for (String str : urlList) {
                server = new HttpServer();
                server.makeGetRequest(str);
                if (server.getResponseCode() != RESPONSE_CODE) {
                    utils.printToConsole(ANSI_BOLD + "This link did not pass validation: " + str +
                            " Code response of page is: " + server.getResponseCode() + ", " +
                            "Response message: " + server.getResponseLine() + "\n" + ANSI_RESET);
                    count++;
                }
                if (str.startsWith("https://i.loopme.me")) {
                    totalSizeOfAssets += server.getSizeOfAsset();
                    fileSizes.put(server.getName(), server.getSizeOfAsset());
                }
            }
            if (count > 0) {
                utils.printToConsole("");
                count = 0;
            }

            for (String str : urlList) {
                if (Arrays.asList(str.split(":")).get(0).equals("http")) {
                    utils.printToConsole(ANSI_BOLD +"Please, check if this link: " + str + " is " +
                            "third party." + ANSI_RESET);
                    count++;
                }
            }
            if (count > 0) {
                utils.printToConsole("");
                count = 0;
            }


            for (Map.Entry<String, Integer> map : fileSizes.entrySet()) {
                if (map.getValue() > 100) {
                    utils.printToConsole(ANSI_BOLD + SIZE_OF_FILE + ANSI_RESET +
                            map.getKey() + " - " + map.getValue() + "kb");
                }
            }

            utils.printToConsole(ANSI_BOLD + TOTAL_SIZE_OF_ASSETS + ANSI_RESET +
                    totalSizeOfAssets + "kb");


            try {
                utils.saveTag(args[i], htmlDocument);
                urlList.clear();
                fileSizes.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            utils.printToConsole(ANSI_BOLD + ANSI_GREEN + FILE_CHECKED + ANSI_RESET);
        }
    }

    private void checkGWDTemplateCreative() {
        utils.printToConsole("");
        utils.printToConsole(ANSI_BOLD + TEMPLATE_VERSION + ANSI_RESET + htmlTag.getVersionTemplateFromGWD());
        utils.printToConsole(ANSI_BOLD + LOOPME_BASE_VERSION + ANSI_RESET + htmlTag.getComponentVersionFromGWD());
        utils.printToConsole(ANSI_BOLD + CREATIVE_NAME_IS + ANSI_RESET + htmlTag.getCreativeNameFromGWD());
        utils.printToConsole(ANSI_BOLD + SIZE_OF_LOADING_PAGE + ANSI_RESET + htmlTag.getSizeOfLoadingPageFromGWD());
        utils.printToConsole("");
    }

    private void checkES6TemplateCreative(String htmlDocument) {
        utils.printToConsole("");
        utils.printToConsole(ANSI_BOLD + TEMPLATE_VERSION + ANSI_RESET + htmlTag.getTitle());
        utils.printToConsole(ANSI_BOLD + CREATIVE_NAME_IS + ANSI_RESET + utils.getCreativeNameForES6
                (htmlDocument));
        utils.printToConsole("");
    }

}
