package bot.core;

import bot.core.gabes_framework.core.database.Database;
import bot.core.gabes_framework.core.Utils;
import bot.modules.gabe.util.point_stats.PointStats;
import bot.modules.gabe.text.rand.Roll;
import bot.modules.gabe.util.Sylwester;
import bot.modules.gabe.image.KartaPulapka;
import bot.modules.gabe.text.rand.EightBall;
import bot.modules.gabe.search.*;
import bot.modules.gabe.util.*;
import bot.modules.gabe.image.Think;
import bot.core.gabes_framework.core.api.Module;
import bot.core.hollandjake_api.helper.misc.Human;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.web_controller.WebController;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.modules.gabe.image.Popcorn;
import bot.modules.gabe.image.rand.RandomGroupPhoto;
import bot.modules.gabe.image.rand.RandomKwejk;
import bot.modules.gabe.text.rand.JebacLeze;
import bot.modules.gabe.text.rand.LezeSpam;
import bot.modules.gabe.util.info.Commands;
import bot.modules.gabe.util.info.Info;
import bot.modules.gabe.twitchemotes.TwitchEmotes;
import bot.modules.gabe.work_in_progress.Mp3Tube;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Chatbot {
    private final String version = "v0.3301";
    protected final HashMap<String, Module> modules = new HashMap<>();
    protected final WebController webController;
    private final ArrayList<Message> messageLog = new ArrayList<>();
    private final ArrayList<Human> people = new ArrayList<>();
    private final String shutdownCode = Integer.toString(new Random().nextInt(99999));
    private final LocalDateTime startupTime = LocalDateTime.now();
    private final Duration messageTimeout = Duration.ofMinutes(1);
    private final long refreshRate = 100;
    private Database database;

    private boolean running = true;
    private String threadId;
    private Human me;
    private int modulesOnline;
    private int totalModules;

    protected void loadModules() {
        modules.put("Commands", new Commands(this, List.of("cmd", "help", "regexList")));
        modules.put("Info", new Info(this));
        modules.put("Shutdown", new Shutdown(this));
        modules.put("Sylwester", new Sylwester(this, "piosenki.txt"));
        modules.put("FeatureSuggest", new FeatureSuggest(this, "sugestie.txt")); // TODO add info

        modules.put("MultiTorrentSearch", new MultiTorrentSearch(this));
        modules.put("WikipediaSearch", new WikipediaSearch(this, List.of("wiki")));
        modules.put("YoutubeSearch", new YoutubeSearch(this, List.of("youtube", "yt")));
        modules.put("GoogleSearch", new GoogleSearch(this));
        modules.put("AllegroSearch", new AllegroSearch(this, List.of("allegro")));
        modules.put("PyszneSearch", new PyszneSearch(this));

        modules.put("RandomGroupPhoto", new RandomGroupPhoto(this));
        modules.put("SimpleWeather", new SimpleWeather(this, List.of("pogoda", "p")));
        modules.put("Popcorn", new Popcorn(this, List.of("popcorn", "rajza")));
        modules.put("KartaPulapka", new KartaPulapka(this, List.of("karta", "kartapulapka", "myk"), "kartapulapka.jpg"));
//        modules.put("Inspire", new Inspire(this));
        modules.put("Roll", new Roll(this));
        modules.put("Think", new Think(this));
        modules.put("EightBall", new EightBall(this, "responses.txt"));
        modules.put("JebacLeze", new JebacLeze(this,
                "responses.txt"));
        modules.put("LezeSpam", new LezeSpam(this, List.of("spam", "kurwa"),
                "responses.txt"));
        modules.put("RandomKwejk", new RandomKwejk(this));
        modules.put("TwitchEmotes", new TwitchEmotes(this));
        modules.put("PointStats", new PointStats(this, database));
        modules.put("Mp3Tube", new Mp3Tube(this));
        modules.put("B", new B(this));
    }

    public void reloadModules() {
        loadModules();
    }


    public Chatbot(String username, String password, String threadId, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        database = new Database();
        webController = new WebController(this, debugMessages, headless, maximised, database);
        run(username, password, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, String threadId, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        database = new Database();
        webController = new WebController(this, debugMessages, headless, maximised, database);
        runFromConfigWithThreadId(configName, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        database = new Database();
        webController = new WebController(this, debugMessages, headless, maximised, database);
        runFromConfig(configName, debugMode, silentMode);
    }

    public Chatbot() {
        database = new Database();
        webController = new WebController(this, false, false, false, database);
        runFromConfig("config", false, false);
    }

    private void runFromConfig(String configName, boolean debugMode, boolean silentMode) {
        ResourceBundle config = ResourceBundle.getBundle(configName);
        String threadId = config.getString("threadId");

        runFromConfigWithThreadId(configName, threadId, debugMode, silentMode);
    }

    private void runFromConfigWithThreadId(String configName, String threadId, boolean debugMode, boolean silentMode) {
        ResourceBundle config = ResourceBundle.getBundle(configName);
        String username = config.getString("username");
        String password = config.getString("password");

        run(username, password, threadId, debugMode, silentMode);
    }

    public void reRun(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        run(username, password, threadId, debugMode, silentMode);
    }

    private void init(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        log();
        System.out.println("Initializing...");
        log();
        System.out.println("Loading modules...");
        loadModules();
        log();
        System.out.println("Finished loading modules.");
        log();
        System.out.println("Echo modules...");

        totalModules = modules.size();
        modulesOnline = 0;
        ArrayList<String> modulesOffline = new ArrayList<>();

        for (Module module : modules.values()) {
            if (module.isOnline()) {
                module.echoOnline();
                modulesOnline++;
            } else {
                modulesOffline.add(module.getClass().getSimpleName());
            }
        }

        if (modulesOnline < totalModules) {
            System.out.println("Not all modules have been successfully loaded.");
            System.out.print("Modules unavailable this session: ");
            for (String module : modulesOffline) {
                System.out.print(module.getClass().getSimpleName() + ", ");
            }
            System.out.println(modulesOnline + "/" + totalModules + " (" + (double) (totalModules - (modulesOnline * totalModules)) / 100 + "%)");
        } else {
            System.out.println(modulesOnline + "/" + totalModules);
        }
        System.out.println("-----------------");

        log();
        System.out.println("Initializing platform...");
        log();
        System.out.println("Logging in...");
        webController.login(username, password);
        log();
        System.out.println("Successfully logged in.");
        System.out.println("Target ID: " + threadId);
        System.out.print("Looking for favourites... ");
        String msg = "";
        if (threadId.equals(PcionBot.ID_GRUPKA)) {
            System.out.print("found.\n");
            msg += "Grupka (" + threadId + ")";
        } else if (threadId.equals(PcionBot.ID_GRZAGSOFT)) {
            System.out.print("found.\n");
            msg += "Grzagsoft (" + threadId + ")";
        } else if (threadId.equals(PcionBot.ID_PATRO)) {
            System.out.print("found.\n");
            msg += "Patro (" + threadId + ")";
        } else {
            System.out.print("not found.\n");
            msg += threadId;
        }

        log();
        System.out.println("Switching to " + msg);
        webController.gotoFacebookThread(threadId);
        log();
        System.out.println("Switched to " + msg);
        log();
        System.out.println("Waiting for messages to load...");
        webController.waitForMessagesToLoad();
        log();
        System.out.println("Messages loaded.");
        log();
        System.out.println("Finished loading.");
        log();
        System.out.print("PcionBot successfully loaded, ");
        System.out.print("running from config:\n");
        System.out.println("max wait time  : " + WebController.TIMEOUT_IN_SEC + " sec.");
        System.out.println("poll sleep time: " + getRefreshRate() + " millisec." );
        log(true);
        System.out.println("-----------------");
        System.out.println("PcionBot " + version);
        System.out.println("Shutdown:  " + shutdownCode);
        System.out.println("-----------------");

        //Init message
        if (!silentMode) {
            initMessage();
        }
    }

    private void log() {
        Date dateNow = new Date();
        String timeNow = dateNow.toString();
        int dateLength = timeNow.length();
        final int datePostfix = 9;

        timeNow = timeNow.substring(0, dateLength - datePostfix);
        System.out.print("\n");
        System.out.print("- " + timeNow);
        System.out.print("\n");
    }

    private void log(boolean noFreeSpace) {
        Date dateNow = new Date();
        String timeNow = dateNow.toString();
        int dateLength = timeNow.length();
        final int datePostfix = 9;

        timeNow = timeNow.substring(0, dateLength - datePostfix);
        System.out.print("\n");
        System.out.println(timeNow);
        if (noFreeSpace) {
            System.out.println("  ");
            return;
        } else {
            System.out.println("\n");
        }
    }

    private void run(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        this.threadId = threadId;
        init(username, password, threadId, debugMode, silentMode);

        while (running) {
            try {
                webController.waitForNewMessage();
                Message newMessage = webController.getLatestMessage();
                messageLog.add(newMessage);

                if (debugMode) {
                    System.out.println(newMessage);
                }

                //Handle options
                try {
                    for (Module module : modules.values()) {
                        module.process(newMessage);
                    }
                } catch (MalformedCommandException e) {
                    sendMessage("There seems to be an issue with your command");
                }
            } catch (TimeoutException e) {
                if (debugMode) {
                    System.out.println("No messaged received in the last " + messageTimeout);
                }
            } catch (WebDriverException e) {
                e.printStackTrace();
                System.out.println("Browser was closed, program is ended");
                webController.quit(true);
                System.exit(1);
            }
        }
    }

    public String getVersion() {
        return version;
    }

    protected void initMessage() {
        webController.sendMessage("PcionBot " + getVersion() + " online.\n"
                + "Załadowane moduły:  " + Utils.NEW_BUTTON_EMOJI + " " + modulesOnline + "/" + totalModules
                + "\n!ladder"
                + "\n!mp3 <youtube link> generuje link do pobrania!"
                + "\n!b <tekst>"
                + "\n\nWpisz !cmd aby zobaczyć listę komend.");
    }

    public String getModulesOnline() {
        String string = Integer.toString(modulesOnline) + " / " + Integer.toString(totalModules);
        return string;
    }

    public void sendMessage(String message) {
        webController.sendMessage(message);
    }

    public void sendImageWithMessage(String image, String message) {
        webController.sendImageWithMessage(image, message);
    }

    public void sendImageWithMessage(Image image, String message) {
        webController.sendMessage(new Message(me, message, image));
    }

    public void sendImage(Image image) {
    }

    public void sendLoadedImage(Image image) {
    }

    public void sendImageFromURLWithMessage(String url, String message) {
        webController.sendImageFromURLWithMessage(url, message);
    }

    public void sendMessage(Message message) {
        webController.sendMessage(message);
    }

    public void sendImageUrlWaitToLoad(String imageUrl) {
        webController.sendImageUrlWaitForLoad(imageUrl);
    }

    public String appendRootPath(String path) {
        return "/" + path;
    }

    public ArrayList<Message> getMessageLog() {
        return messageLog;
    }

    public Duration getMessageTimeout() {
        return messageTimeout;
    }

    public Human getMe() {
        return me;
    }

    public void setMe(Human me) {
        this.me = me;
    }

    public ArrayList<Human> getPeople() {
        return people;
    }

    public String getThreadId() {
        return threadId;
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public LocalDateTime getStartupTime() {
        return startupTime;
    }

    public String getShutdownCode() {
        return shutdownCode;
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public boolean containsCommand(Message message) {
        for (Module module : modules.values()) {
            if (!module.getMatch(message).equals("")) {
                return true;
            }
        }
        return false;
    }

    public void screenshot() {
        webController.screenshot();
    }

    public void quit() {
        webController.quit(true);
    }
}
