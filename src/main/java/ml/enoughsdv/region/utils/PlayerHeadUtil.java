package ml.enoughsdv.region.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class PlayerHeadUtil {

    private String title;
    private String owner = "steve";
    private List<String> lores;

    public PlayerHeadUtil title(String title) {
        this.title = title;
        return this;
    }

    public PlayerHeadUtil owner(String owner) {
        this.owner = owner;
        return this;
    }

    public PlayerHeadUtil lores(List<String> lores) {
        this.lores = lores;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        if (this.title != null) {
            meta.setDisplayName(MessageUtil.translate("&r" + this.title));
        }

        if (this.lores != null && !this.lores.isEmpty()) {
            meta.setLore(MessageUtil.translate(this.lores));
        }

        meta.setOwner(owner);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
