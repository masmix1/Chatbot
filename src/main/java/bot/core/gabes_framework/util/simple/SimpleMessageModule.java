package bot.core.gabes_framework.util.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class SimpleMessageModule extends SimpleModule {
    private String message;

    public SimpleMessageModule(Chatbot chatbot, List<String> regexList, String message) {
        super(chatbot, regexList);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendMessage(this.message);
                return true;
            }
        }
        return false;
    }
}