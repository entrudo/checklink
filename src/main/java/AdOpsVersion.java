import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdOpsVersion extends JFrame {

        static String nameFile = "";
        static Integer count = 0;
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_RED = "\u001B[41m";
        public static final String ANSI_GREEN = "\u001B[42m";
        public static final String ANSI_BOLD = "\u001B[1m";
        public static void main(String[] args) throws IOException, InterruptedException {
//        String content = "";
//        if (args.length == 0 || args.length > 1){
//            System.err.println("Please write only the name of file at the end of the line.");
//            return;
//        }
//        String[] readArgs = args;
//        try {
//            content = readFile(readArgs[0]);
//        }catch (Exception e){
//            System.err.println("Please check the file");
//            return;
//        }
            String content = MyFrame();
            String regExp1 = "http://i.loopme.me";
            String regExp2 = "http://loopme.me";
            String regExp3 = "http://www.w3.org";
            String regExp4 = "[a-z]+:\\/\\/.*?['&?,\"]";
            String regExp5 = "http+:\\/\\/.*?\"";
            content = checkLinksILoopme(content,regExp1);
            content = checkLinksLoopme(content,regExp2);
            content = checkLinksW3(content, regExp3);
            checkOtherLinks(content, regExp5);
            System.out.println("\n----------- >> CHECK ALL REFERENCES TO EFFICIENCY << -----------\n");
            for (String s : getLinks(content, regExp4)){
                try {
                    checkURL(s);
                }catch (RuntimeException e){
                    System.err.println(ANSI_BOLD + ANSI_RED + "----------------->> ERROR <<----------------------" + ANSI_RESET);
                    System.err.println("Something wrong with this link. Please check it: " + s + "\n");
                    count++;
                }
            }
            if (count == 0) {
                System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All links are valid." + ANSI_RESET);
            }
            writeFile(nameFile, content);
            System.out.println("\nFile was written the following folder \"New File\"");
        }

        public static void checkURL (String URL){
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet responce = new HttpGet(URL);
            HttpResponse resp = null;
            try {
                resp = client.execute(responce);
            } catch (Exception e){
                System.err.println(ANSI_BOLD + ANSI_RED + "----------------->> ERROR <<----------------------" + ANSI_RESET);
                System.err.println("Something wrong with this link. Please check it: " + URL + "\n");
                count++;
                return;
            }
            int code = resp.getStatusLine().getStatusCode();
            String st = resp.getStatusLine().getReasonPhrase();
            if (code != 200){
                System.out.println("This links is not validation: " + responce.getURI() + " Code response of page is: " + Integer.toString(code) + ", Page is: "  + st + "\n");
                count++;
            }
        }

        public static void checkOtherLinks(String content, String regExp){
            ArrayList<String> list = new ArrayList<String>();
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()){
                list.add(matcher.group().substring(0, matcher.group().length()-1));
            }
            if (list.size() > 0){
                System.out.println("Link (s) ----> http:// other links <---- was detected! \n");
            }

            for (String s : list){
                System.out.println("Please, check if this link: " + s + " is third party.\n");
            }
        }

        public static String checkLinksW3(String content, String regExp){
            if (content.indexOf("dropbox") > 0){
                System.err.println(ANSI_BOLD + ANSI_RED + "\n\nPLEASE CHECK ASSETS!!!  THERE IS DROPBOX LINKS!!!!\n\n" + ANSI_RESET);
            }
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(content);
            String tmp = "https://www.w3.org";
            if (matcher.find() == false){
            }else {
                content = matcher.replaceAll(tmp);
//            System.out.println("\nLink ----> http://www.w3.org <---- was detected!\n" + "This link will be replaced for https://www.w3.org \n");
            }
            return content;
        }

        public static String checkLinksLoopme(String content, String regExp){
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(content);
            System.out.println("\nCHEKING loopme.me LINKS...\n");
            String tmp = "https://loopme.me";
            if (matcher.find() == false){
                System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All loopme.me links are https://" + ANSI_RESET);
            }else {
                content = matcher.replaceAll(tmp);
                System.out.println("Link ----> http://loopme.me <---- was detected!\n" + "This link will be replaced for https://loopme.me \n");
            }
            return content;
        }

        public static String checkLinksILoopme(String content, String regExp){
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(content);
            System.out.println("\nCHEKING i.loopme.me LINKS...\n");
            String tmp = "https://i.loopme.me";
            if (matcher.find() == false){
                System.out.println(ANSI_BOLD + ANSI_GREEN + "There is everything ok. All i.loopme.me links are https://" + ANSI_RESET);
            }else {
                content = matcher.replaceAll(tmp);
                System.out.println("Link ----> http://i.loopme.me <---- was detected!\n" + "This link will be replaced for https://i.loopme.me \n");
            }
            return content;
        }

        public static ArrayList<String> getLinks(String content, String regExp){
            ArrayList<String> list = new ArrayList<String>();
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(content);
            String tmp = "";
            while (matcher.find()){
                tmp = matcher.group().substring(0, matcher.group().length()-1);
                list.add(tmp);
            }

            return list;
        }

        public static String readFile(File nameFile) throws IOException{
            BufferedReader reader = new BufferedReader(new FileReader(nameFile));
            String content = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ( (content = reader.readLine()) != null){
                stringBuilder.append(content);
                stringBuilder.append("\n");
            }
            content = stringBuilder.toString();
            return content;
        }

        public static void writeFile (String nameFile, String text) throws IOException {
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

        public static String MyFrame() throws IOException {
            JFileChooser fc = new JFileChooser();
            String content = "";
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                nameFile = file.getName();
                content = AdOpsVersion.readFile(file);
                fc.setVisible(false);
                return content;
            }else {
                System.out.println(ANSI_BOLD + "You closed program.\nIf you want run it again!" + ANSI_RESET);
                System.exit(0);
                return null;
            }
        }
}
