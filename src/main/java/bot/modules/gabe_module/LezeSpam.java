package bot.modules.gabe_module;

import bot.core.Chatbot;
import bot.utils.gabe_modules.module_library.resource.ResourceModule;

import java.util.List;

public class LezeSpam extends ResourceModule {
    public LezeSpam(Chatbot chatbot, List<String> commands, String resourceName) {
        super(chatbot, commands, resourceName);
    }
}