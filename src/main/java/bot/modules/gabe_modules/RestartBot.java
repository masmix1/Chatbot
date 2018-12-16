package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.PcionBot.PcionBot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RestartBot extends SingleMessageModule {
    public RestartBot(Chatbot chatbot, List<String> commands, String message) {
        super(chatbot, commands, message);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                try {
                    restartApplication();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(PcionBot.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        if(!currentJar.getName().endsWith(".jar"))
            return;

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
}
