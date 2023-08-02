package cn.daogecmd.emojink;

import cn.daogecmd.emojink.api.EmojiAPI;
import cn.daogecmd.emojink.command.EmojiCommand;
import cn.daogecmd.emojink.listeners.PlayerListener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;

public class Loader extends PluginBase {
    public static Loader INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {

        //save emoji-phrases.yml
        saveResource("emoji-phrases.yml", false);

        //save emoji.yml
        saveResource("emoji.yml");
        var emojiList = new HashMap<String, String>();
        for (var entry : new Config(this
                .getDataFolder()
                .toPath()
                .resolve("emoji.yml")
                .toFile())
                .getAll().entrySet()) {
            emojiList.put(entry.getKey(), (String) entry.getValue());
        }

        //init API
        EmojiAPI.initAPI(emojiList);
        EmojiAPI.setAutoEmoji(getConfig().getBoolean("autoEmoji"));

        getServer().getCommandMap().register("", new EmojiCommand());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
}
