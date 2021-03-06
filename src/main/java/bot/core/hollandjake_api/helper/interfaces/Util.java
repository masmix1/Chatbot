package bot.core.hollandjake_api.helper.interfaces;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * @author hollandjake
 */
public interface Util {
    //region Keyboard operations
    Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    String COPY = Keys.chord(Keys.CONTROL, "c");
    String PASTE = Keys.chord(Keys.CONTROL, "v");
    //endregion

    //region Date formats
    SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yy");
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
    DateTimeFormatter ERROR_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
    //endregion

    Random RANDOM = new Random();

    static String ACTIONIFY(String arg) {
        return "(?i)^!\\s*" + arg + "$";
    }

    static String ACTIONIFY_CASE(String arg) {
        return "^!\\s*" + arg + "$";
    }

    static String DEACTIONIFY(String regex) {
        return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static String DEACTIONIFY_CASE(String regex) {
        return regex.replaceAll("\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static <T> T GET_RANDOM(List<T> list) {
        int random = RANDOM.nextInt(list.size());

        return list.get(random);
    }

    static String GET_PAGE_SOURCE(String url) {
        try {
            return Unirest.get(url).header("Users-agent", "Dogbot Reborn").asString().getBody();
        } catch (UnirestException e) {
            System.out.println("Page doesn't exist");
            e.printStackTrace();
            return "";
        }
    }
}
