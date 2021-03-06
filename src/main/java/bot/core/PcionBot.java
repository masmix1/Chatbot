package bot.core;

public class PcionBot {
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String DATABASE_USERNAME = "";
    public static final String DATABASE_PASSWORD = "";

    public static final String IMGUR_SECRET = "";
    public static final String IMGUR_ID = "";
    public static final String YANDEX_API_KEY = ".";
    public static final String NEWS_API_KEY = "";

    public static final String KARTAPULAPKA_IMG_URL = "https://res.cloudinary.com/drpmvrlik/image/upload/v1547334869/assets/kartapulapka/kartapulapka.jpg";

    public static final String GRUPKA_ID = "1158615960915822";
    public static final String PCIONBOT_MAIN_ID = "2388008607938113";
    public static final String PATRO_ID = "2275107775897967";

    public static final boolean LOG_MODE = false;    // doesn't respond to commands - updates database only.
    public static final boolean DEBUG_MODE = false; // adds bot name before it's message and " : " after.
    public static final boolean DEBUG_MESSAGES = false; // debugs messages to console
    public static final boolean HEADLESS = false;
    public static final boolean MAXIMIZED = true;

    public static final boolean SILENT_MODE = true; // does not greet itself or send msg when exception occurs
    public static final String THREAD = GRUPKA_ID;

    // TODO reading username, password, thread id and everything else from a file
    // TODO database on a different thread - will allow for multiple database connections! (thread lock - synchronized)
    // TODO check search modules for bugs
    // TODO add !gimg - returns top 3 images in google image search
    // TODO grammar nazi - spacja po znaku interpunkcyjnym, itp.
    // TODO !remind 6h / jutro / 2 dni (.*)
    // TODO ogarnac czas w klasie Chatbot - np. kick na 5 minut
    // !vn - VAPE NATION + zdjecie ethana z palcami VN
    // FIXME ogarnac referencje w PointSystem - lub pamietac zeby sprawdzac przez getName a nie po refach!!!
    // przy kazdej nowej wiadomosci tej samej osoby referencja zmienia sie!

    // Backlog:
    //      - !co - co? wyruszamy natychmiast!
    public static void main(String[] args) {
        Chatbot pcionbot;
        System.out.println(args.toString());
        pcionbot = newBot();
    }

    public static Chatbot newBot() {
        return new Chatbot(USERNAME, PASSWORD, THREAD,
                DEBUG_MODE,
                SILENT_MODE,
                DEBUG_MESSAGES,
                HEADLESS,
                MAXIMIZED,
                LOG_MODE);
    }
}
