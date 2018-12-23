package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.*;

public class RandomKwejk extends ModuleBase {
    private String currentImageUrl;
    private Document doc;
    private Elements els;

    private static final String KWEJK_URL = "https://kwejk.pl/losowy";

    private final String KWEJK_REGEX = ACTIONIFY("kwejk");
    private final String KW_REGEX = ACTIONIFY("kw");

    public RandomKwejk(Chatbot chatbot) {
        super(chatbot);
        getNextMeme();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOrOther(KWEJK_REGEX, KW_REGEX)) {
            chatbot.sendImageUrlWaitToLoad(currentImageUrl);
            getNextMeme();
            return true;
        }
        return false;
    }

    private void getNextMeme() {
        getDocument(KWEJK_URL);
        els = doc.getElementsByClass("figure");
        currentImageUrl = getUrl(els.toString());
    }

    private String getUrl(String string) {
        Pattern pattern = Pattern.compile("src=\".*?\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            String imgUrl = matcher.group();
            imgUrl = imgUrl.substring(5, imgUrl.length() - 1);
            return imgUrl;
        }
        return "";
    }

    private void getDocument(String url) {
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, KWEJK_REGEX, KW_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.regexToList(KWEJK_REGEX, KW_REGEX);
    }
}
