import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsoleVersion {
    static Integer count = 0;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public static void main(String[] args) throws IOException, InterruptedException {
        String content = "";
        if (args.length == 0 || args.length > 4) {
            System.out.println(ANSI_RED + ANSI_BOLD + "Please write only the name of file at the end of the line." + ANSI_RESET);
            return;
        }
        String[] readArgs = args;
//        try {
//            content = readFile(readArgs[0]);
//        }catch (Exception e){
//            System.out.println(ANSI_BOLD + ANSI_RED + "Please check the file" + ANSI_RESET);
//            return;
//        }
        if (readArgs[0].contains("-h")) {
            System.out.println("If you want to check up one file you should use the script in such a way: java -jar checklinks.jar file.txt \n");
            System.out.println("If you want to check up to 4 files at once you should to use the script in such a way: java -jar checklinks.jar file.txt file2.txt file3.txt file4.txt\n");

            return;
        }

        for (int i = 0; i < args.length; i++) {
            try {
                content = readFile(readArgs[i]);
                System.out.println(ANSI_BOLD + "\n \nNow checking this file: " + readArgs[i] + "\n \n" + ANSI_RESET);
                startCheck(readArgs[i], content);
            } catch (Exception e) {
                System.out.println(ANSI_BOLD + ANSI_RED + "Please check the file" + ANSI_RESET);
                return;
            }
        }

//        String content = MyFrame();
//        String regExp1 = "http://i.loopme.me";
//        String regExp2 = "http://loopme.me";
//        String regExp3 = "http://www.w3.org";
//        String regExp4 = "[a-z]+:\\/\\/.*?[&?',\"]";
//        String regExp5 = "http+:\\/\\/.*?\"";
//        content = checkLinksILoopme(content,regExp1);
//        content = checkLinksLoopme(content,regExp2);
//        content = checkLinksW3(content, regExp3);
//        checkOtherLinks(content, regExp5);
//        System.out.println("\n----------- >> CHECK ALL REFERENCES TO EFFICIENCY << -----------\n");
//        for (String s : getLinks(content, regExp4)){
//            try {
//                checkURL(s);
//            }catch (RuntimeException e){
//                System.out.println(ANSI_BOLD + ANSI_RED + "----------------->> ERROR <<----------------------" + ANSI_RESET);
//                System.out.println("Something wrong with this link. Please check it: " + s + "\n");
//                count++;
//            }
//        }
//        if (count == 0) {
//            System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All links are valid." + ANSI_RESET);
//        }
////        writeFile(readArgs[0], content);
//        System.out.println("\nFile was written the following folder \"New File\"");
    }

    public static void startCheck(String fileName, String content) throws IOException {

        String regExp1 = "http://i.loopme.me";
        String regExp2 = "http://loopme.me";
        String regExp3 = "http://www.w3.org";
        String regExp4 = "[a-z]+:\\/\\/.*?[&?',\"]";
        String regExp5 = "http+:\\/\\/.*?\"";
        String regExp6 = "data-version=\\\".....?\\\"";
        String regExp7 = "data-template-name=\\\".*[a-z]\\\"";
        String regExp8 = "data-template-version=\\\".*[0-9]\\\"";
        String regExp9 = "image-player-component.min.js. data-version=\".*?\"";

        writeVersionComponent(content, regExp6);
        writeVersionOfTemplate(content, regExp7, regExp8);
        writeVersionOfVideoTemplate(content, regExp9);
        content = checkLinksILoopme(content, regExp1);
        content = checkLinksLoopme(content, regExp2);
        content = checkLinksW3(content, regExp3);
        checkOtherLinks(content, regExp5);
        content = changeCommentLinks(content);

        System.out.println(ANSI_BOLD + "\n----------- >> CHECK SIZE OF FILE << -----------\n" +
                ANSI_RESET);
        checkSizeOfAssets(content);

        System.out.println(ANSI_BOLD + "\n----------- >> CHECK ALL REFERENCES TO EFFICIENCY << -----------\n" + ANSI_RESET);
        for (String s : getLinks(content, regExp4)) {
            try {
                checkURL(s);
            } catch (RuntimeException e) {
                System.out.println("Something wrong with this link. Please check it: " + s + "\n");
                count++;
            }
        }

        if (count == 0) {
            System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All links are valid." + ANSI_RESET);
        }

        writeFile(fileName, content);
        System.out.println(ANSI_BOLD + "\nFile was written the following folder \"New File\"" + ANSI_RESET);
    }

    public static String changeCommentLinks(String content) {
        String link = "http://polymer";
        String replaceLink = "https://polymer";

        return content.replaceAll(link, replaceLink);
    }

    public static void checkURL(String URL) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet responce = new HttpGet(URL);
        HttpResponse resp = null;
        try {
            resp = client.execute(responce);
        } catch (Exception e) {
            System.out.println("Something wrong with this link. Please check it: " + URL + "\n");
            count++;
            return;
        }
        int code = resp.getStatusLine().getStatusCode();
        String st = resp.getStatusLine().getReasonPhrase();
        if (code != 200) {
            System.out.println("This links is not validation: " + responce.getURI() + " Code response of page is: " + Integer.toString(code) + ", Page is: " + st + "\n");
            count++;
        }

        if (URL.contains("%20")) {
            System.out.println("This links has invalid symbol, please check and delete incorrect symbol: " + URL + "\n");
        }

    }

    public static void checkOtherLinks(String content, String regExp) {
        ArrayList<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            list.add(matcher.group().substring(0, matcher.group().length() - 1));
        }
        if (list.size() > 0) {
            System.out.println(ANSI_BOLD + "\nLink (s) ----> http:// other links <---- was detected! \n" + ANSI_RESET);
        }

        for (String s : list) {
            System.out.println("Please, check if this link: " + s + " is third party.\n");
        }
    }

    public static String checkLinksW3(String content, String regExp) {
        if (content.indexOf("dropbox") > 0) {
            System.err.println("\n\nPLEASE CHECK ASSETS!!!  THERE IS DROPBOX LINKS!!!!\n\n");
        }
        if (content.indexOf("CACHEBUSTER") > 0) {
            System.err.println("\n\nPLEASE CHECK ASSETS!!!  THERE IS CACHEBUSTER IN LINKS!!!!\n\n");
        }
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        String tmp = "https://www.w3.org";
        if (matcher.find() == false) {
        } else {
            content = matcher.replaceAll(tmp);
//            System.out.println("\nLink ----> http://www.w3.org <---- was detected!\n" + "This link will be replaced for https://www.w3.org \n");
        }
        return content;
    }

    public static String checkLinksLoopme(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        System.out.println(ANSI_BOLD + "\nCHEKING loopme.me LINKS...\n" + ANSI_RESET);
        String tmp = "https://loopme.me";
        if (matcher.find() == false) {
            System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All loopme.me links are https://" + ANSI_RESET);
        } else {
            content = matcher.replaceAll(tmp);
            System.out.println("Link ----> http://loopme.me <---- was detected!\n" + "This link will be replaced for https://loopme.me \n");
        }
        return content;
    }

    public static String checkLinksILoopme(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        System.out.println(ANSI_BOLD + "\nCHEKING i.loopme.me LINKS...\n" + ANSI_RESET);
        String tmp = "https://i.loopme.me";
        if (matcher.find() == false) {
            System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All i.loopme.me links are https://" + ANSI_RESET);
        } else {
            content = matcher.replaceAll(tmp);
            System.out.println("Link ----> http://i.loopme.me <---- was detected!\n" + "This link will be replaced for https://i.loopme.me \n");
        }
        return content;
    }

    public static ArrayList<String> getLinks(String content, String regExp) {
        ArrayList<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        String tmp = "";
        while (matcher.find()) {
            tmp = matcher.group().substring(0, matcher.group().length() - 1);
            list.add(tmp);
        }

        return list;
    }

    public static String readFile(String nameFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nameFile));
        String content = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((content = reader.readLine()) != null) {
            stringBuilder.append(content);
            stringBuilder.append("\n");
        }
        content = stringBuilder.toString();
        return content;
    }

    public static void writeFile(String nameFile, String text) throws IOException {
        File fileDelete = new File(nameFile);
        fileDelete.delete();
        File file2 = new File("New File");
        file2.mkdir();
        File file = new File("New File/" + nameFile);
        file.createNewFile();
        PrintWriter print = new PrintWriter(file.getAbsoluteFile());
        print.write(text);
        print.close();
    }

    public static void writeVersionComponent(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find() == true) {
            System.out.println(ANSI_BOLD + "Version of LoopMe Base component: " + ANSI_RESET + matcher.group().substring(14, matcher.group().length() - 1) + "\n");
        }
    }

    public static void writeVersionOfTemplate(String content, String regExp, String regExp2) {
        Pattern pattern = Pattern.compile(regExp);
        Pattern pattern2 = Pattern.compile(regExp2);
        Matcher matcher = pattern.matcher(content);
        Matcher matcher2 = pattern2.matcher(content);

        if (matcher.find() == true && matcher2.find() == true) {
            System.out.println(ANSI_BOLD + "Version of LoopMe GWD templates: " + ANSI_RESET +
                    matcher.group().substring(20, matcher.group().length() - 1) + " " +
                    matcher2.group().substring(23, matcher2.group().length() - 1) + "\n");
        }
    }

    public static void writeVersionOfVideoTemplate(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find() == true) {
            System.out.println(ANSI_BOLD + "Version of LoopMe Image Player: " + ANSI_RESET +
                    matcher.group().substring(45, matcher.group().length() - 1));
        }
    }

    public static void checkSizeOfAssets(String content) throws IOException {
        Pattern pattern = Pattern.compile("[a-z]+:\\/\\/i.loopme.me.*?[&?',\"]");
        Matcher matcher = pattern.matcher(content);
        List<String> list = new ArrayList<String>();

        while (matcher.find()){
            String temp = matcher.group().substring(0, matcher.group().length() - 1);
            list.add(temp);
        }

        for (int i = 0; i < list.size(); i++){
            String[] array = list.get(i).split("/");
            URL url = new URL(list.get(i));
            URLConnection urlConnection = url.openConnection();
            double sizeFile = (double) urlConnection.getContentLength() / 1024;
            if (sizeFile > 50) {
                System.out.println("Size of file: " + array[array.length - 1] + " - "
                        + String.format("%.2f", sizeFile) + "kb");
            }
        }
    }
}