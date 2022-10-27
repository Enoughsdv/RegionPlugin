package ml.enoughsdv.region.menu;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InventoryListener<T> {

    @Getter private final Class<T> type;
    private final Consumer<T> consumer;

    public void accept(T t) {
        consumer.accept(t);
    }
}
