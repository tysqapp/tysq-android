package jerrEditor.processor;

import java.util.ArrayList;

/**
 * author       : frog
 * time         : 2019-07-09 16:03
 * desc         : 输出处理器
 * version      : 1.3.0
 */

public class OutputProcessor {

    private static final ArrayList<String> ORIGINAL_LIST = new ArrayList<>();
    private static final ArrayList<String> HANDLED_LIST = new ArrayList<>();

    public static void addType(String original, String handled) {
        ORIGINAL_LIST.add(original);
        HANDLED_LIST.add(handled);
    }

    static {

        // 去除 <br></p><p><xxx> ---> </p><p><xxx> start
        addType("<br></p><p><video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video>",
                "</p><p><video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video>");

        addType("<br></p><p><audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio>",
                "</p><p><audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio>");

        addType("<br></p><p><img src=\"{{img}}\"/>",
                "</p><p><img src=\"{{img}}\"/>");

        addType("<br></p><p><hr />",
                "</p><p><hr />");
        // 去除 <br></p><p><xxx> ---> </p><p><xxx> end

        // 去除 <xxx></p><p><br> ---> <xxx></p><p> start
        addType("<video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video></p><p><br>",
                "<video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video></p><p>");

        addType("<audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio></p><p><br>",
                "<audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio></p><p>");

        addType("<img src=\"{{img}}\"/></p><p><br>",
                "<img src=\"{{img}}\"/></p><p>");

        addType("<hr /></p><p><br>",
                "<hr /></p><p>");
        // 去除 <xxx></p><p><br> ---> <xxx></p><p> end

        // <p><xxx> ---> <xxx>
        addType("<p><h1>", "<h1>");
        addType("<p><h2>", "<h2>");
        addType("<p><h3>", "<h3>");
        addType("<p><h4>", "<h4>");

        // </xxx></p> ---> </xxx>
        addType("</h1></p>", "</h1>");
        addType("</h2></p>", "</h2>");
        addType("</h3></p>", "</h3>");
        addType("</h4></p>", "</h4>");

        // </xxx><br> ---> </xxx>
        addType("</h1><br>", "</h1>");
        addType("</h2><br>", "</h2>");
        addType("</h3><br>", "</h3>");
        addType("</h4><br>", "</h4>");

        // <p></p><p><xxx> ---> <p><xxx>
        addType("<p></p><p><video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video>",
                "<p><video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video>");

        addType("<p></p><p><audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio>",
                "<p><audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio>");

        addType("<p></p><p><img src=\"{{img}}\"/>",
                "<p><img src=\"{{img}}\"/>");

        addType("</blockquote><p></p>","</blockquote>");
        addType("<p></p></blockquote>","</blockquote>");

        // <xxx></p><p></p> ---> <p><xxx></p>
        addType("<video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video></p><p></p>",
                "<video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video></p>");

        addType("<audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio></p><p></p>",
                "<audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio></p>");

        addType("<img src=\"{{img}}\"/></p><p></p>",
                "<img src=\"{{img}}\"/></p>");

        // <xxx></p><p></p> ---> <xxx>
        addType("<hr /></p><p></p>",
                "<hr />");

        // <p></p><p><xxx> ---> <xxx>
        addType("<p></p><p><hr />",
                "<hr />");

        // <p><xxx> ---> <xxx>
        addType("<p><hr />",
                "<hr />");

        // <xxx></p> ---> <xxx>
        addType("<hr /></p>",
                "<hr />");

        // <p></p> ---> <br>
        addType("<p></p>", "<br>");

        // <xxx> --->
        addType("<strong></strong>", "");
        addType("<em></em>", "");
        addType("<span style=\"text-decoration:line-through;\"></span>", "");
        addType("<h1></h1>", "");
        addType("<h2></h2>", "");
        addType("<h3></h3>", "");
        addType("<h4></h4>", "");

    }

    public static String handle(String html) {
        for (int i = 0; i < ORIGINAL_LIST.size(); i++) {

            String oldString = ORIGINAL_LIST.get(i);

            String newString = HANDLED_LIST.get(i);

            html = html.replace(oldString, newString);
        }

        return html;
    }

}
