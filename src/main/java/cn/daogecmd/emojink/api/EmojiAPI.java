package cn.daogecmd.emojink.api;

import cn.daogecmd.emojink.Loader;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.DimensionEnum;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class EmojiAPI {
    private static final Map<String, double[]> EMOJI_POS = new HashMap<>();
    private final static Map<String, List<String>> EMOJI_PHRASES = new HashMap<>();
    private final static Config EMOJI_PHRASES_CONFIG = new Config(Loader.INSTANCE.getDataFolder().toPath().resolve("emoji-phrases.yml").toString(), Config.YAML);

    static {
        EMOJI_POS.put("smiley", new double[]{0, 0});
        EMOJI_POS.put("grimacing", new double[]{0.01, 0});
        EMOJI_POS.put("grin", new double[]{0.02, 0});
        EMOJI_POS.put("joy", new double[]{0.03, 0});
        EMOJI_POS.put("smile", new double[]{0.04, 0});
        EMOJI_POS.put("sweat_smile", new double[]{0.05, 0});
        EMOJI_POS.put("laughing", new double[]{0.06, 0});
        EMOJI_POS.put("innocent", new double[]{0.07, 0});
        EMOJI_POS.put("wink", new double[]{0, 0.01});
        EMOJI_POS.put("blush", new double[]{0.01, 0.01});
        EMOJI_POS.put("slight_smile", new double[]{0.02, 0.01});
        EMOJI_POS.put("upside_down", new double[]{0.03, 0.01});
        EMOJI_POS.put("relaxed", new double[]{0.04, 0.01});
        EMOJI_POS.put("yum", new double[]{0.05, 0.01});
        EMOJI_POS.put("relieved", new double[]{0.06, 0.01});
        EMOJI_POS.put("heart_eyes", new double[]{0.07, 0.01});
        EMOJI_POS.put("kissing_heart", new double[]{0, 0.02});
        EMOJI_POS.put("kissing", new double[]{0.01, 0.02});
        EMOJI_POS.put("kissing_smiling_eyes", new double[]{0.02, 0.02});
        EMOJI_POS.put("kissing_closed_eyes", new double[]{0.03, 0.02});
        EMOJI_POS.put("stuck_out_tongue_winking_eye", new double[]{0.04, 0.02});
        EMOJI_POS.put("stuck_out_tongue_closed_eyes", new double[]{0.05, 0.02});
        EMOJI_POS.put("stuck_out_tongue", new double[]{0.06, 0.02});
        EMOJI_POS.put("money_mouth", new double[]{0.07, 0.02});
        EMOJI_POS.put("sunglasses", new double[]{0, 0.03});
        EMOJI_POS.put("smirk", new double[]{0.01, 0.03});
        EMOJI_POS.put("no_mouth", new double[]{0.02, 0.03});
        EMOJI_POS.put("neutral_face", new double[]{0.03, 0.03});
        EMOJI_POS.put("expressionless", new double[]{0.04, 0.03});
        EMOJI_POS.put("unamused", new double[]{0.05, 0.03});
        EMOJI_POS.put("rolling_eyes", new double[]{0.06, 0.03});
        EMOJI_POS.put("flushed", new double[]{0.07, 0.03});
        EMOJI_POS.put("disappointed", new double[]{0, 0.04});
        EMOJI_POS.put("worried", new double[]{0.01, 0.04});
        EMOJI_POS.put("angry", new double[]{0.02, 0.04});
        EMOJI_POS.put("rage", new double[]{0.03, 0.04});
        EMOJI_POS.put("pensive", new double[]{0.04, 0.04});
        EMOJI_POS.put("confused", new double[]{0.05, 0.04});
        EMOJI_POS.put("slight_frown", new double[]{0.06, 0.04});
        EMOJI_POS.put("frowning2", new double[]{0.07, 0.04});
    }

    @Getter
    private static EmojiAPI API;
    //key: emoji_id, value: emoji_name
    @Getter
    private final Map<String, String> emojiList;
    private final Server server;
    @Getter @Setter
    private static boolean isAutoEmoji = true;

    private EmojiAPI(Map<String, String> emojiList) {
        API = this;
        for (var id : EMOJI_POS.keySet()) {
            var name = emojiList.getOrDefault(id, id);
            emojiList.put(id, name);
        }
        this.emojiList = emojiList;
        server = Server.getInstance();
    }

    public static void initAPI(Map<String, String> emojiList) {
        if (API == null) {
            API = new EmojiAPI(emojiList);
            for (String emojiId : emojiList.keySet()) {
                EMOJI_PHRASES.put(emojiId, EMOJI_PHRASES_CONFIG.getStringList(emojiId));
            }
        }
    }

    public void sendEmoji(Entity entity, String emojiId) {
        if (!EMOJI_POS.containsKey(emojiId))
            return;
        var viewers = new HashSet<>(entity.getViewers().values());
        if (entity instanceof Player own)
            viewers.add(own);
        if (viewers.isEmpty())
            return;
        double[] pos = EMOJI_POS.get(emojiId);
        var pk = new SpawnParticleEffectPacket();
        pk.dimensionId = DimensionEnum.OVERWORLD.getDimensionData().getDimensionId();
        pk.uniqueEntityId = -1;
        pk.position = entity.getPosition().add(0, entity.getHeight() + 1, 0).asVector3f();
        pk.identifier = "emoji:emoji";
        pk.molangVariablesJson = new StringBuilder()
                .append("[{\"name\":\"variable.ix\",\"value\":{\"type\":\"float\",\"value\":")
                .append(pos[0])
                .append("}},{\"name\":\"variable.iy\",\"value\":{\"type\":\"float\",\"value\":")
                .append(pos[1])
                .append("}}]").toString().describeConstable();
        Server.broadcastPacket(viewers, pk);
    }

    /**
     * @return the emoji id or Null if there's no emoji phrase in the given phrase
     */
    @Nullable
    public String getPhraseEmoji(String phrase) {
        phrase = phrase.toLowerCase();
        for (Map.Entry<String, List<String>> entry : EMOJI_PHRASES.entrySet()) {
            for (String emojiPhrase : entry.getValue()) {
                if (phrase.contains(emojiPhrase)) return entry.getKey();
            }
        }
        return null;
    }
}
