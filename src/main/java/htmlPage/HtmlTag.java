package htmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlTag {
    private Document htmlTag;
    private String title;
    private List<Node> comments = new ArrayList<Node>();

    public HtmlTag(String html) {
        htmlTag = Jsoup.parse(html);
        title = htmlTag.title();
        findCreativeAttributes();
    }

    public String getTitle() {
        return title;
    }

    public Document getHtmlTag() {
        return htmlTag;
    }

    public String getFirstComment() {

        return comments.size() != 0 ? comments.get(0).toString() : "";
    }

    public List<String> getAllUrl() {
        List<String> links = new ArrayList<String>();
        Elements linksTags = htmlTag.select("[src]");
        for (Element element : linksTags) {
            links.add(element.attr("src"));
        }
        return links;
    }

    public void findCreativeAttributes() {
        for (Element element : htmlTag.getAllElements()) {
            for (Node node : element.childNodes()) {
                if (node instanceof Comment) {
                    comments.add(node);
                }
            }
        }
    }

    public String getVersionTemplateFromGWD() {
        Elements metaTags = htmlTag.select("meta");
        return metaTags.attr("data-template-name") + " " + metaTags.attr("data-template-version");
    }

    public String getCreativeNameFromGWD() {
        return htmlTag.getElementById("loopme-base_1").attr("data-custom-event-name");
    }

    public String getComponentVersionFromGWD() {
        return htmlTag.getElementsByAttributeValue("data-source", "loopme-base.min.js").attr("data-version");
    }

    public String getSizeOfLoadingPageFromGWD() {
        return htmlTag.getElementById("loopme-base_1").attr("data-loading-page-size");
    }

}
