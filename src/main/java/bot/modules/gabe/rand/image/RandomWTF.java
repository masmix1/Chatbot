package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomWTF extends ModuleBase {
    private String currUrl;
    private Document doc;

    private ArrayList<String> urls;
    private int index;

    private static final String WTF_URL = "https://ujarani.com/wtf/losowe";

    private final String WTF_REGEX = Utils.TO_REGEX("wtf");

    public RandomWTF(Chatbot chatbot) {
        super(chatbot);
        getDocument(WTF_URL);
        initUrls(doc.getElementsByClass("img-responsive").toString());
        currUrl = getNextMeme();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(WTF_REGEX)) {
            addPoints(message, 2);
            chatbot.sendImageUrlWaitToLoad(currUrl);
            currUrl = getNextMeme();
            return true;
        }
        return false;
    }

    private String getNextMeme() {
        if (index < urls.size()) {
            return urls.get(index++);
        } else {
            getDocument(WTF_URL);
            initUrls(doc.getElementsByClass("img-responsive").toString());
            return urls.get(index++);
        }
    }

    private void initUrls(String els) {
        urls = new ArrayList<>(10);
        index = 0;
        Pattern pattern = Pattern.compile("src=\".*?\"");
        Matcher matcher = pattern.matcher(els);
        String lastUrl = "";

        while (matcher.find()) {
            String url = matcher.group();
            if (url.equals(lastUrl)) {
                continue;
            }
            urls.add(url.substring(5, url.length() - 1));
            lastUrl = url;
        }
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
        return findMatch(message, WTF_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(WTF_REGEX);
    }
}