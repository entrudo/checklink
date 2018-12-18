package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public String readHTMLTag(String htmlDoc) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(htmlDoc));
        StringBuilder stringBuilder = new StringBuilder();
        String content;
        while ((content = reader.readLine()) != null) {
            stringBuilder.append(content);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void printToConsole(String message){
        System.out.println(message);
    }

    public void printToConsole(int message){
        System.out.println(message);
    }

    public List<String> getAllUrlFromTag(String tag) {
        ArrayList<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(ScriptСonstants.REG_EXP_ALL_LINKS);
        Matcher matcher = pattern.matcher(tag);
        String tmp = "";
        while (matcher.find()) {
            tmp = matcher.group().substring(0, matcher.group().length() - 1);
            list.add(tmp);
        }

        return list;
    }

    public String getCreativeNameForES6(String tag) {
        Pattern pattern = Pattern.compile(ScriptСonstants.REG_EXP_CREATIVE_NAME);
        Matcher matcher = pattern.matcher(tag);

        if (matcher.find()) {
            return matcher.group().substring(15, matcher.group().length() - 1);
        } else {
            return Messages.CREATIVE_NAME_NOT_FOUND_IN_HTML;
        }
    }

    public void saveTag(String nameFile, String text) throws IOException {
        File fileDelete = new File(nameFile);
        fileDelete.delete();
        File file2 = new File("New File");
        file2.mkdir();

        String nameOfTagForWrite = Arrays.asList(nameFile.split("\\.")).get(0);
        File file = new File("New File/" + nameOfTagForWrite + ".txt");
        file.createNewFile();

        PrintWriter print = new PrintWriter(file.getAbsoluteFile());
        print.write(text);
        print.close();
    }

    public String replaceLoopMeLinks(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        String tmp = regExp.replace("http", "https");
        if (matcher.find()) {
            content = matcher.replaceAll(tmp);
            System.out.println(ScriptСonstants.ANSI_BOLD +"\n\nLink ----> " + regExp + " <---- " +
                    "was detected!\n" + "This link will be replaced for " + tmp + " \n" + ScriptСonstants.ANSI_RESET);
        }
        return content;
    }

    public String getCreativeType(String content) {
        Pattern pattern = Pattern.compile(ScriptСonstants.REG_EXP_EVENT);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String[] arrayOfParameters = matcher.group().split("\\(");
            return arrayOfParameters[3].split(",\"")[0];

            } else if (content.contains("gwd-responsive-landscape") || content.contains("gwd-responsive") ||
                content.contains("gwd-vertical-video-template")) {
                  pattern = Pattern.compile(ScriptСonstants.REG_EXP_EVENT_GWD);
                  matcher = pattern.matcher(content);
                  if (matcher.find()) {
                    String[] arrayOfParameters = matcher.group().split("\\(");
                    return arrayOfParameters[2].split(", \"")[0];
                  }
            }
        return Messages.ERROR_TYPE;
    }
}
