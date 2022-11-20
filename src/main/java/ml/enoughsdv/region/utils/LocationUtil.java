package ml.enoughsdv.region.utils;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class LocationUtil {

    @NotNull
    public String parseToString(@NotNull org.bukkit.Location location) {
        return location.getX() + ", "
                + location.getY() + ", "
                + location.getZ() + ", "
                + location.getYaw() + ", "
                + location.getPitch() + ", "
                + location.getWorld().getName();
    }

    @NotNull
    public Location parseToLocation(@NotNull String string) {
        String[] data = string.split(", ");

        try {
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            double z = Double.parseDouble(data[2]);
            float pitch = Float.parseFloat(data[4]);
            float yaw = Float.parseFloat(data[3]);
            org.bukkit.World world = Bukkit.getWorld(data[5]);

            Location location = new Location(world, x, y, z, yaw, pitch);

            return location;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
