package ml.enoughsdv.region.commands.arguments;

import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.MessageUtil;

import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegionArgument implements ValueResolver<Region>, SuggestionProvider {

    private final RegionPlugin plugin;

    public RegionArgument(@NotNull RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Region resolve(@NotNull ValueResolverContext context) throws Throwable {
        Region region = plugin.getRegionHandler().getRegion(context.pop());

        if (context == null || region == null) {
            throw new CommandErrorException(MessageUtil.translate(plugin.getConfig()
                    .getString("messages.region.does_not_exist", "Region not found!")));
        }

        return region;
    }

    @Override
    public Collection<String> getSuggestions(
            @NotNull List<String> list,
            @NotNull CommandActor commandActor,
            @NotNull ExecutableCommand executableCommand) throws Throwable {

        List<String> listRegion = new ArrayList<>();

        plugin.getRegionHandler().getRegions().forEach(regions -> listRegion.add(regions.getName()));
        return listRegion;
    }

}
