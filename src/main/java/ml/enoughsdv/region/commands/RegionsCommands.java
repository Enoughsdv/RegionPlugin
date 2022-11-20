package ml.enoughsdv.region.commands;

import java.util.UUID;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menus.GeneralRegionsMenu;
import ml.enoughsdv.region.menus.RegionMenu;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.bukkit.core.BukkitActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.help.CommandHelp;

@Command("region")
public class RegionsCommands {

    @Dependency
    private FileConfiguration config;
    private final RegionPlugin plugin;

    public RegionsCommands(@NotNull RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @CommandPermission("region.menu")
    public void regionCommand(Player player, @Optional @Named("region") Region region) {
        if (region == null) {
            GeneralRegionsMenu.getInventory().open(player);
            player.sendMessage(MessageUtil
                    .translate(config.getString("messages.region.menu.general")));
            return;
        }

        player.sendMessage(MessageUtil.translate(config.getString("messages.region.menu.region")
                .replace("%region_name%", region.getName())));

        new RegionMenu(region).getInventory().open(player);
    }

    @Subcommand("help")
    @Description("Show help message")
    public void help(CommandActor actor, CommandHelp<String> helpEntries, @Default("1") int page) {
        if (page <= 0) { //fixing error
            shotHelpMessage(actor, helpEntries, page, 10);
            return;
        }

        shotHelpMessage(actor, helpEntries, page, 10);
    }

    @Subcommand("create")
    @Usage("<name>, <displayName>")
    @CommandPermission("region.create")
    public void regionCreateCommand(BukkitActor actor,
            @Named("name") String name,
            @Named("display name") String displayName) {

        if (plugin.getRegionHandler().getRegion(name) != null) {
            actor.reply(config
                    .getString("messages.region.exists"));
            return;
        }

        Region region = new Region(name);

        if (actor.isPlayer()) {
            region.getPlayers().add(actor.getUniqueId());
        }

        region.setDisplayName(displayName);
        region.save(true);

        actor.reply(config.getString("messages.region.created")
                .replace("%region_name%", name));

    }

    @Subcommand("delete")
    @Usage("<region>")
    @CommandPermission("region.delete")
    public void regionDeleteCommand(BukkitActor actor, @Named("region") Region region) {
        actor.reply(config.getString("messages.region.deleted")
                .replace("%region_name%", region.getName()));

        region.delete(true);
    }

    @Subcommand({"wand", "claim"})
    @Usage("<region>")
    @CommandPermission("region.create")
    public void regionWandCommand(Player player, @Named("region") Region region) {
        plugin.getClaimHandler().claimToggle(player, region);
    }

    @Subcommand("whitelist")
    @Usage("<region>")
    @CommandPermission("whitelist")
    public void regionWhitelistCommand(BukkitActor actor, @Named("region") Region region) {
        config.getStringList("messages.region.whitelisted.show_list.header")
                .forEach(actor::reply);

        for (UUID uuids : region.getPlayers()) {
            if (uuids == null) {
                continue;
            }

            OfflinePlayer players = Bukkit.getOfflinePlayer(uuids);

            actor.reply(config
                    .getString("messages.region.whitelisted.show_list.body")
                    .replace("%player_name%", players.getName()));
        }

        config.getStringList("messages.region.whitelisted.show_list.footer")
                .forEach(actor::reply);
    }

    @Subcommand("whitelist add")
    @Usage("<region> <target>")
    @CommandPermission("region.add")
    public void regionAddCommand(BukkitActor actor,
            @Named("region") Region region,
            @Named("target") OfflinePlayer target) {

        if (region.getPlayers().contains(target.getUniqueId())) {
            actor.reply(config.getString("messages.region.whitelisted.exists"));
            return;
        }

        region.getPlayers().add(target.getUniqueId());
        actor.reply(config.getString("messages.region.whitelisted.added")
                .replace("%target_name%", target.getName()));
    }

    @Subcommand("whitelist remove")
    @Usage("<region> <target>")
    @CommandPermission("region.remove")
    public void regionRemoveCommand(BukkitActor actor,
            @Named("region") Region region,
            @Named("target") OfflinePlayer target) {

        if (!region.getPlayers().contains(target.getUniqueId())) {
            actor.reply(config.getString("messages.region.whitelisted.does_not_exist"));
            return;
        }

        region.getPlayers().remove(target.getUniqueId());
        actor.reply(config.getString("messages.region.whitelisted.removed")
                .replace("%target_name%", target.getName()));
    }

    private void shotHelpMessage(@NotNull CommandActor actor,
            @NotNull CommandHelp<String> helpEntries,
            @NotNull int page,
            @NotNull int showCommands) {

        actor.reply("&7&m---------------------------");
        for (String entry : helpEntries.paginate(page, showCommands)) {
            actor.reply(entry);
        }
        actor.reply("&7&m---------------------------");
    }

}
