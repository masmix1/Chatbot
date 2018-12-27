package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiTorrentSearch extends ModuleBase {
    private final String TORRENT_REGEX = Utils.TO_REGEX("torrent (.*)");
    private final String T_REGEX = Utils.TO_REGEX("t (.*)");

    private static final String BITLY_ACCESS_TOKEN = "ccbb8945fa671a57a48645c181466d9ad5619749";

    private static final String X1337_URL = "1337x.to/search/";
    private static final String X1337_POSTFIX = "/1/";

    private static final String TORRENTZ_URL = "https://torrentz2.eu/search?f=";

    public MultiTorrentSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (match.equals(TORRENT_REGEX) || match.equals(T_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find()) {
                addPoints(message, 2);
                String userQuery = matcher.group(1).replaceAll("\\s+", "+");
                String messageToSend = X1337_URL + userQuery + X1337_POSTFIX
                            + "\n\n" + TORRENTZ_URL + userQuery;
                chatbot.sendMessage(messageToSend);
                return true;
            } else {
                chatbot.sendMessage("Coś żeś pojebał");
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(TORRENT_REGEX)) {
            return TORRENT_REGEX;
        } else if (messageBody.matches(T_REGEX)) {
            return T_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(TORRENT_REGEX));
        commands.add(Utils.TO_COMMAND(T_REGEX));
        return commands;
    }
}
