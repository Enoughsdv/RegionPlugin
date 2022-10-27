package ml.enoughsdv.region.menus;

import java.util.UUID;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menu.ClickableItem;
import ml.enoughsdv.region.menu.SmartInventory;
import ml.enoughsdv.region.menu.content.InventoryContents;
import ml.enoughsdv.region.menu.content.InventoryProvider;
import ml.enoughsdv.region.menu.content.Pagination;
import ml.enoughsdv.region.menu.content.SlotIterator;
import static ml.enoughsdv.region.menus.GeneralRegionsMenu.INVENTORY;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.ItemBuilder;
import ml.enoughsdv.region.utils.MessageUtil;
import ml.enoughsdv.region.utils.PlayerHeadUtil;
import ml.enoughsdv.region.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayersListMenu implements InventoryProvider {

    private final FileConfiguration config = RegionPlugin.getInstance().getConfig();
    
    private final Region region;

    public PlayersListMenu(Region region) {
        this.region = region;
    }

    public SmartInventory getInventory() {
        return SmartInventory.builder()
                .id("playersListMenu")
                .provider(new PlayersListMenu(region))
                .size(5, 9)
                .title(MessageUtil.translate("&ePlayers list of this region"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[region.getPlayers().size()];
        int i = 0;

        for (UUID uuid : region.getPlayers()) {
            if (uuid == null) continue;

            OfflinePlayer players = Bukkit.getOfflinePlayer(uuid);

            items[i] = ClickableItem.of(new PlayerHeadUtil()
                    .owner(players.getName())
                    .title(config.getString("menus.players_list.item.title").replace("%player_name%", players.getName()))
                    .lores(config.getStringList("menus.players_list.item.lores"))
                    .build(), e -> {
                        if (!e.isLeftClick()) {
                            return;
                        }

                        region.getPlayers().remove(uuid);

                    });
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(3, 5, ClickableItem
                .of(new ItemBuilder(XMaterial.valueOf(config
                        .getString("menus.general.next.material")))
                        .title(config.getString("menus.general.next.title")).build(),
                        e -> INVENTORY.open(player, pagination.previous().getPage())));

        contents.set(3, 3, ClickableItem
                .of(new ItemBuilder(XMaterial.valueOf(config
                        .getString("menus.general.previous.material")))
                        .title(config.getString("menus.general.previous.title")).build(),
                        e -> INVENTORY.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[region.getPlayers().size()];
        int i = 0;

        for (UUID uuid : region.getPlayers()) {
            if (uuid == null) continue;

            OfflinePlayer players = Bukkit.getOfflinePlayer(uuid);

            items[i] = ClickableItem.of(new PlayerHeadUtil()
                    .owner(players.getName())
                    .title(config.getString("menus.players_list.item.title").replace("%player_name%", players.getName()))
                    .lores(config.getStringList("menus.players_list.item.lores"))
                    .build(), e -> {
                        if (!e.isLeftClick()) {
                            return;
                        }

                        region.getPlayers().remove(uuid);

                    });
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        
        contents.set(4, 5, ClickableItem
                .of(new ItemBuilder(XMaterial.valueOf(config
                        .getString("menus.general.next.material")))
                        .title(config.getString("menus.general.next.title")).build(),
                        e -> INVENTORY.open(player, pagination.previous().getPage())));

        contents.set(4, 3, ClickableItem
                .of(new ItemBuilder(XMaterial.valueOf(config
                        .getString("menus.general.previous.material")))
                        .title(config.getString("menus.general.previous.title")).build(),
                        e -> INVENTORY.open(player, pagination.next().getPage())));
    }

}
