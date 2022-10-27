package ml.enoughsdv.region.menus;

import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menu.ClickableItem;
import ml.enoughsdv.region.menu.SmartInventory;
import ml.enoughsdv.region.menu.content.InventoryContents;
import ml.enoughsdv.region.menu.content.InventoryProvider;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.ItemBuilder;
import ml.enoughsdv.region.utils.MessageUtil;
import ml.enoughsdv.region.utils.CountdownUtil;
import ml.enoughsdv.region.utils.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RegionMenu implements InventoryProvider {

    private final FileConfiguration config = RegionPlugin.getInstance().getConfig();

    private final Region region;

    public RegionMenu(Region region) {
        this.region = region;
    }

    public SmartInventory getInventory() {
        return SmartInventory.builder()
                .id("regionMenu")
                .provider(new RegionMenu(region))
                .size(3, 9)
                .title(MessageUtil.translate("&eRegion Settings"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                .getString("menus.region_settings.item.rename.material")))
                .title(config.getString("menus.region_settings.item.rename.title"))
                .lores(config.getStringList("menus.region_settings.item.rename.lores"))
                .build(), e -> {
                    if (!e.isLeftClick()) {
                        return;
                    }

                    CountdownUtil.start(player, getInventory(), region);
                }));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                .getString("menus.region_settings.item.redefine.material")))
                .title(config.getString("menus.region_settings.item.redefine.title"))
                .lores(config.getStringList("menus.region_settings.item.redefine.lores"))
                .build(), e -> {
                    if (!e.isLeftClick()) {
                        return;
                    }

                    player.closeInventory();
                    player.performCommand("region wand " + region.getName());
                }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                .getString("menus.region_settings.item.add_player.material")))
                .title(config.getString("menus.region_settings.item.add_player.title"))
                .lores(config.getStringList("menus.region_settings.item.add_player.lores"))
                .build(), e -> {
                    if (!e.isLeftClick()) {
                        return;
                    }

                    new PlayersOnlineMenu(region).getInventory().open(player);

                }));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                .getString("menus.region_settings.item.remove_player.material")))
                .title(config.getString("menus.region_settings.item.remove_player.title"))
                .lores(config.getStringList("menus.region_settings.item.remove_player.lores"))
                .build(), e -> {
                    if (!e.isLeftClick()) {
                        return;
                    }
                    new PlayersListMenu(region).getInventory().open(player);
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}
