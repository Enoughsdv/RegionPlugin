package ml.enoughsdv.region.commands;

import java.util.UUID;
import lombok.AllArgsConstructor;
import me.vaperion.blade.annotation.argument.Name;
import me.vaperion.blade.annotation.argument.Optional;
import me.vaperion.blade.annotation.command.Command;
import me.vaperion.blade.annotation.argument.Sender;
import me.vaperion.blade.annotation.argument.Text;
import me.vaperion.blade.annotation.command.Permission;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menus.GeneralRegionsMenu;
import ml.enoughsdv.region.menus.RegionMenu;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class RegionCommands {

    private final RegionPlugin plugin;
    private final FileConfiguration config;

    @Command(value = "region")
    @Permission(value = "region.menu")
    public void regionCommand(@Sender Player player, @Optional @Name("region") Region region) {
        if (region == null) {
            GeneralRegionsMenu.INVENTORY.open(player);
            player.sendMessage(MessageUtil.translate(config
                    .getString("messages.region.menu.general")));

            return;
        }

        player.sendMessage(MessageUtil.translate(config
                .getString("messages.region.menu.region")
                .replace("%region_name%", region.getName())));

        new RegionMenu(region).getInventory().open(player);
    }

    @Command(value = "region create")
    @Permission(value = "region.create")
    public void regionCreateCommand(@Sender Player player, @Name("name") String name,
            @Name("display name") @Text String displayName) {
        if (plugin.getRegionHandler().getRegion(name) != null) {
            player.sendMessage(MessageUtil.translate(config
                    .getString("messages.region.exists")));
            return;
        }

        Region region = new Region(name);
        region.getPlayers().add(player.getUniqueId());
        region.setDisplayName(displayName);
        region.save(true);

        player.sendMessage(MessageUtil.translate(config
                .getString("messages.region.created").replace("%region_name%", name)));
    }

    @Command(value = "region delete")
    @Permission(value = "region.delete")
    public void regionDeleteCommand(@Sender CommandSender sender, @Name("region") Region region) {
        sender.sendMessage(MessageUtil.translate(config
                .getString("messages.region.deleted").replace("%region_name%", region.getName())));

        region.delete(true);
    }

    @Command(value = {"region wand", "region claim"})
    @Permission(value = "region.create")
    public void regionWandCommand(@Sender Player player, @Name("region") Region region) {
        plugin.getClaimHandler().claimToggle(player, region);
    }

    @Command(value = "region whitelist add")
    @Permission(value = "region.add")
    public void regionAddCommand(@Sender CommandSender sender, @Name("region") Region region, @Name("target") OfflinePlayer target) {
        if (region.getPlayers().contains(target.getUniqueId())) {
            sender.sendMessage(MessageUtil.translate(config
                    .getString("messages.region.whitelisted.exists")));
            return;
        }

        region.getPlayers().add(target.getUniqueId());
        sender.sendMessage(MessageUtil.translate(config
                .getString("messages.region.whitelisted.added").replace("%target_name%", target.getName())));
    }

    @Command(value = "region whitelist remove")
    @Permission(value = "region.remove")
    public void regionRemoveCommand(@Sender CommandSender sender, @Name("region") Region region, @Name("target") OfflinePlayer target) {
        if (!region.getPlayers().contains(target.getUniqueId())) {
            sender.sendMessage(MessageUtil.translate(config
                    .getString("messages.region.whitelisted.does_not_exist")));
            return;
        }

        region.getPlayers().remove(target.getUniqueId());
        sender.sendMessage(MessageUtil.translate(config
                .getString("messages.region.whitelisted.removed").replace("%target_name%", target.getName())));
    }

    @Command(value = "region whitelist")
    @Permission(value = "region.whitelist")
    public void regionWhitelistCommand(@Sender CommandSender sender, @Name("region") Region region) {
        config.getStringList("messages.region.whitelisted.show_list.header").stream()
                .map(MessageUtil::translate).forEach(string -> {
            sender.sendMessage(string);
        });

        for (UUID uuids : region.getPlayers()) {
            if (uuids == null) continue;
            OfflinePlayer players = Bukkit.getOfflinePlayer(uuids);
            sender.sendMessage(MessageUtil.translate(config
                    .getString("messages.region.whitelisted.show_list.body")
                    .replace("%player_name%", players.getName())));
        }

        config.getStringList("messages.region.whitelisted.show_list.footer").stream()
                .map(MessageUtil::translate).forEach(string -> {
            sender.sendMessage(string);
        });
    }

}
