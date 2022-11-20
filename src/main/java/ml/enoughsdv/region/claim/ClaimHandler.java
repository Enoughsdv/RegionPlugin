package ml.enoughsdv.region.claim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.ItemBuilder;
import ml.enoughsdv.region.utils.MessageUtil;
import ml.enoughsdv.region.utils.XMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class ClaimHandler {

    private final RegionPlugin plugin;
    private final Map<UUID, Claim> claimHandlerMap = new HashMap<>();

    public ClaimHandler(@NotNull RegionPlugin plugin) {
        this.plugin = plugin;
    }

    public void claimToggle(@NotNull Player player, @NotNull Region region) {
        if (!claimHandlerMap.containsKey(player.getUniqueId())) {
            PlayerInventory inventory = player.getInventory();
            if (inventory.contains(getClaimWand())) {
                player.getItemInHand().setAmount(0);
                return;
            }

            if (!inventory.addItem(getClaimWand()).isEmpty()) {
                player.sendMessage(MessageUtil.translate(plugin.getConfig().getString("messages.region.claim.inventory_full")));
                return;
            }

            player.sendMessage(MessageUtil.translate(plugin.getConfig().getString("messages.region.claim.item_added")));
            claimHandlerMap.put(player.getUniqueId(), new Claim());
            return;
        }

        Claim claim = claimHandlerMap.get(player.getUniqueId());

        region.setPositionOne(claim.getPosition1());
        region.setPositionTwo(claim.getPosition2());
        region.save(true);
        claimHandlerMap.remove(player.getUniqueId());
        player.getInventory().remove(getClaimWand());
        player.sendMessage(MessageUtil.translate(plugin.getConfig()
                .getString("messages.region.saved")
                .replace("%region_name%", region.getName())));

    }

    public ItemStack getClaimWand() {
        return new ItemBuilder(XMaterial.DIAMOND_HOE)
                .title("&cClaim Wand")
                .lores("&7Left click to set position 1")
                .lores("&7Righ click to set position 2")
                .enchantment(Enchantment.DURABILITY, 9)
                .build();
    }

    @NotNull
    public Map<UUID, Claim> getClaimHandlerMap() {
        return claimHandlerMap;
    }

}
