package ml.enoughsdv.region.menus;

import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menu.ClickableItem;
import ml.enoughsdv.region.menu.SmartInventory;
import ml.enoughsdv.region.menu.content.InventoryContents;
import ml.enoughsdv.region.menu.content.InventoryProvider;
import ml.enoughsdv.region.menu.content.Pagination;
import ml.enoughsdv.region.menu.content.SlotIterator;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.ItemBuilder;
import ml.enoughsdv.region.utils.MessageUtil;
import ml.enoughsdv.region.utils.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GeneralRegionsMenu implements InventoryProvider {

    private final FileConfiguration config = RegionPlugin.getInstance().getConfig();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("generalRegionsMenu")
            .provider(new GeneralRegionsMenu())
            .size(4, 9)
            .title(MessageUtil.translate("&eList of general regions"))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[RegionPlugin.getInstance().getRegionHandler().getRegions().size()];

        int i = 0;

        for (Region regions : RegionPlugin.getInstance().getRegionHandler().getRegions()) {
            if (regions == null) {
                continue;
            }
            items[i] = ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                    .getString("menus.general_regions.item.material")))
                    .title(config.getString("menus.general_regions.item.title")
                            .replace("%region_display_name%", regions.getDisplayName()))
                    .lores(config.getStringList("menus.general_regions.item.lores"))
                    .build(), e -> {
                        if (e.isLeftClick()) {
                            new RegionMenu(regions).getInventory().open(player);
                            return;
                        }

                        if (regions.getPositionOne() == null || regions.getPositionTwo() == null) {
                            player.sendMessage(MessageUtil.translate(config.getString("messages.region.claim.location_not_found")
                                    .replace("%region_display_name%", regions.getDisplayName())));
                            return;
                        }

                        player.teleport(regions.getCuboid().getCenter());
                        player.sendMessage(MessageUtil.translate(config.getString("messages.region.claim.teleported")
                                .replace("%region_display_name%", regions.getDisplayName())));
                    });

            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

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
        ClickableItem[] items = new ClickableItem[RegionPlugin.getInstance().getRegionHandler().getRegions().size()];

        int i = 0;

        for (Region regions : RegionPlugin.getInstance().getRegionHandler().getRegions()) {
            if (regions == null) {
                continue;
            }

            items[i] = ClickableItem.of(new ItemBuilder(XMaterial.valueOf(config
                    .getString("menus.general_regions.item.material")))
                    .title(config.getString("menus.general_regions.item.title")
                            .replace("%region_display_name%", regions.getDisplayName()))
                    .lores(config.getStringList("menus.general_regions.item.lores"))
                    .build(), e -> {
                        if (e.isLeftClick()) {
                            new RegionMenu(regions).getInventory().open(player);
                            return;
                        }

                        if (regions.getPositionOne() == null || regions.getPositionTwo() == null) {
                            player.sendMessage(MessageUtil.translate(config.getString("messages.region.claim.location_not_found")
                                    .replace("%region_display_name%", regions.getDisplayName())));
                            return;
                        }

                        player.teleport(regions.getCuboid().getCenter());
                        player.sendMessage(MessageUtil.translate(config.getString("messages.region.claim.teleported")
                                .replace("%region_display_name%", regions.getDisplayName())));
                    });

            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

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

}
