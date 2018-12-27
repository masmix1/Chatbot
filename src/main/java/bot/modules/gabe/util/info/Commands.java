package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.message.SingleMessageModule;

import java.util.List;

import static bot.core.gabes_framework.core.Utils.*;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        message = new StringBuilder() // instead of making an instance and assigning build String to message.
                .append("Dostępne komendy:")
                .append("\n")
                .append("===============")
                .append("\n")

                .append("\u2935")
                .append(NEW_BUTTON_EMOJI).append("!mp3 <youtube link>").append("\n")

                .append(B_EMOJI)
                .append(NEW_BUTTON_EMOJI).append("!b <tekst>").append("\n")

                .append("\uD83D\uDD22")
                .append(NEW_BUTTON_EMOJI).append("!ladder").append("\n")

                .append(INFRMATION_EMOJI).append("!stats").append("\n")
                .append("\uD83D\uDE03").append("!emotes").append("\n")
//                .append(INFRMATION_EMOJI).append("!sylwester").append("\n")
                .append(PENCIL_EMOJI).append("!suggest  !pomysl").append("\n")
                .append(MAGNIFYING_EMOJI).append("!torrent").append("\n")
                .append(MAGNIFYING_EMOJI).append("!wiki").append("\n")
                .append(MAGNIFYING_EMOJI).append("!youtube !yt ").append("\n")
                .append(MAGNIFYING_EMOJI).append("!google   !g ").append("\n")
                .append(MAGNIFYING_EMOJI).append("!allegro").append("\n")
                .append(MAGNIFYING_EMOJI).append("!pyszne").append("\n")
                .append("\uD83C\uDF10").append("!pyszne mariano").append("\n")
                .append("\uD83C\uDF10").append("!pyszne football").append("\n")
                .append("\uD83C\uDF10").append("!pyszne haianh").append("\n")

                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("\uD83D\uDE02").append("!kwejk !kw").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append("\u2754").append("!8ball !ask !8 ").append("\n")
                .append(HOURGLASS_EMOJI).append("!roll !roll <max>").append("\n")
                .append(THINK_EMOJI).append("!think !think <liczba>").append("\n")
                .append(CAMERA_EMOJI).append("!karta !kartapulapka !myk").append("\n")
                .append(POPCORN_EMOJI).append("!popcorn").append("\n")
                .append(EXCLAM_MRK_EMOJI).append("!g leze").append("\n")
                .append(EXCLAM_MRK_EMOJI).append("!jebacleze !leze !spam").append("\n")

                .append(INFRMATION_EMOJI).append("!info").append("\n")
                .append(INFRMATION_EMOJI).append("!stats").append("\n")
                .append(INFRMATION_EMOJI).append("!uptime").append("\n")
                .append(INFRMATION_EMOJI).append("!g help ").append("\n")

                .toString();
        }
}