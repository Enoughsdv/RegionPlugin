package ml.enoughsdv.region.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MessageUtil {

    @NotNull
    public String translate(@NotNull String string) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    public List<String> translate(@NotNull List<String> messages) {
        List<String> buffered = new ArrayList<>();
        messages.forEach(message -> buffered.add(translate(message)));
        return buffered;
    }

}
