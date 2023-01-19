package cn.daogecmd.emojink.command;

import cn.daogecmd.emojink.api.EmojiAPI;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;

public class EmojiCommand extends Command {
    public EmojiCommand() {
        super("emoji", "send emoji!");
        this.setAliases(new String[]{"emj", "ej"});
        this.setPermission("commands.emoji");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender) || !sender.isPlayer()) {
            return false;
        }
        //send form
        var buttons = EmojiAPI.getAPI()
                .getEmojiList()
                .entrySet()
                .stream()
                .map(entry -> {
                    var builder = new StringBuilder("§f");
                    builder.append(entry.getValue());
                    builder.append("§8\n");
                    builder.append(entry.getKey());
                    var text = builder.toString();
                    return new ElementButton(text);
                }).toList();
        var form = new FormWindowSimple("§fEmoji", "§bChoose the emoji you want!", buttons);
        form.addHandler((player, formID) -> {
            var response = form.getResponse();
            if (response == null) return;
            var emojiId = response.getClickedButton().getText().split("\n")[1];
            EmojiAPI.getAPI().sendEmoji(player, emojiId);
        });
        sender.asPlayer().showFormWindow(form);
        return true;
    }
}
