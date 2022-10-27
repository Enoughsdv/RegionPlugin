package ml.enoughsdv.region.commands.arguments;

import java.util.ArrayList;
import java.util.List;
import me.vaperion.blade.argument.Argument;
import me.vaperion.blade.argument.ArgumentProvider;
import me.vaperion.blade.context.Context;
import me.vaperion.blade.exception.BladeExitMessage;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.utils.MessageUtil;
import org.jetbrains.annotations.NotNull;

public class RegionArgument implements ArgumentProvider<Region> {

    private final RegionPlugin plugin;

    public RegionArgument(RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Region provide(@NotNull Context ctx, @NotNull Argument arg) throws BladeExitMessage {
        Region region = plugin.getRegionHandler().getRegion(arg.getString());

        if (arg == null || region == null) {
            throw new BladeExitMessage(MessageUtil.translate(plugin.getConfig()
                    .getString("messages.region.does_not_exist", "Region not found!")));
        }

        return region;
    }

    @NotNull
    @Override
    public List<String> suggest(@NotNull Context context, @NotNull Argument arg) throws BladeExitMessage {
        List<String> list = new ArrayList<>();

        plugin.getRegionHandler().getRegions().forEach(regions -> {
            if (regions.getName().toLowerCase().startsWith(arg.getString().toLowerCase())) {
                list.add(regions.getName());
            }
        });

        return list;
    }

}
