package com.note.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    private static final Safelist SAFELIST = createSafelist();

    private static Safelist createSafelist() {
        Safelist safelist = Safelist.relaxed();

        safelist.addTags("input", "label", "span");
        safelist.addAttributes("input", "type", "checked", "disabled");
        safelist.addAttributes("label", "for");
        safelist.addAttributes("ul", "data-type");
        safelist.addAttributes("li", "data-type", "data-checked");
        safelist.addAttributes(":all", "class", "style");

        return safelist;
    }

    public static String sanitize(String htmlContent) {
        if (htmlContent == null || htmlContent.isBlank()) {
            return htmlContent;
        }

        return Jsoup.clean(htmlContent, SAFELIST);
    }
}