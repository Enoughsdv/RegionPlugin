package ml.enoughsdv.region.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemFlag;

public class ItemBuilder {

    private XMaterial material;
    private Short durability;
    private String title;
    private int amount = 1;
    private List<String> lores;
    private final Map<Enchantment, Integer> enchantments;
    private final ItemStack itemStack;

    public ItemBuilder() {
        this.enchantments = new HashMap<>();
        this.itemStack = new ItemStack(XMaterial.AIR.parseMaterial());
    }

    public ItemBuilder(XMaterial material) {
        this.enchantments = new HashMap<>();
        this.itemStack = new ItemStack(material.parseMaterial());
    }

    public ItemBuilder(ItemStack itemStack) {
        this.enchantments = new HashMap<>();
        this.itemStack = itemStack;
    }

    public ItemBuilder material(XMaterial material) {
        this.material = material;
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder lores(List<String> lores) {
        this.lores = lores;
        return this;
    }

    public ItemBuilder lores(String... lores) {
        this.lores = Arrays.asList(lores);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment) {
        this.enchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder clearLore() {
        lores.clear();
        return this;
    }

    public ItemBuilder hideFlags() {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.enchantments.clear();
        return this;
    }

    public ItemStack build() {
        ItemStack item = this.itemStack;
        if (this.material != null) {
            item.setType(this.material.parseMaterial());
        }

        enchantments.keySet().forEach(enchantment -> {
            item.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        });

        ItemMeta meta = item.getItemMeta();

        if (this.amount > 0) {
            item.setAmount(this.amount);
        }

        if (this.durability != null) {
            item.setDurability(this.durability);
        }

        if (this.title != null) {
            meta.setDisplayName(MessageUtil.translate("&r" + this.title));
        }

        if (this.lores != null && !this.lores.isEmpty()) {
            meta.setLore(MessageUtil.translate(this.lores));
        }

        item.setItemMeta(meta);
        return item;
    }
}
