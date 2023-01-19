package cn.daogecmd.emojink;

import cn.daogecmd.emojink.api.EmojiAPI;
import cn.daogecmd.emojink.command.EmojiCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;
import java.util.UUID;

public class Loader extends PluginBase {

    private static final UUID PACK_UUID = UUID.fromString("eef1262f-003b-41bd-94f0-b0b61e34b1f6");
    private static final String VERSION = "1.0.0";
    //unused
    private static final String PACK_NAME = "resources_pack/EmojiParticle.mcpack";

    @Override
    public void onEnable() {
        var resourcePackManager = this.getServer().getResourcePackManager();
        //lack resource pack
        if (resourcePackManager.getPackById(PACK_UUID) == null) {
            this.getLogger().warning("Specific resource pack is missing!");
        }
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
        getServer().getCommandMap().register("", new EmojiCommand());
    }
}
