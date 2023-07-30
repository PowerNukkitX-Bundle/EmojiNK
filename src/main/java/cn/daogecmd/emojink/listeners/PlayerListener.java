package cn.daogecmd.emojink.listeners;

import cn.daogecmd.emojink.api.EmojiAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;

public class PlayerListener implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    public void onChat(PlayerChatEvent e){
        if (!EmojiAPI.isAutoEmoji()) return;

        Player player = e.getPlayer();
        String message = TextFormat.clean(e.getMessage());
        String emojiId = EmojiAPI.getAPI().getPhraseEmoji(message);

        EmojiAPI.getAPI().sendEmoji(player, emojiId);
    }
}
