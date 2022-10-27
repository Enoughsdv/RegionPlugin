package ml.enoughsdv.region.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClickableItem {

    private final ItemStack item;
    private final Consumer<InventoryClickEvent> consumer;

    public static ClickableItem empty(ItemStack item) {
        return of(item, e -> {});
    }

    public static ClickableItem of(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        return new ClickableItem(item, consumer);
    }

    public void run(InventoryClickEvent e) {
        consumer.accept(e);
    }

    public ItemStack getItem() {
        return item;
    }

}
