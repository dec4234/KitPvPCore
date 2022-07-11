package net.dec4234.utils;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

	@Getter private Material mat;
	@Getter private String name;
	@Getter private ItemStack itemStack;

	public ItemBuilder(Material material) {
		itemStack = new ItemStack(material);
	}

	public ItemBuilder(Material material, String name) {
		itemStack = new ItemStack(material);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name);
		setItemMeta(meta);
		this.name = name;
	}

	public ItemBuilder(Material material, String name, short data) {
		itemStack = new ItemStack(material, 1, data);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name);
		setItemMeta(meta);
		this.name = name;
	}

	public ItemBuilder(Material mat, String name, PotionEffectType pet) {
		itemStack = new ItemStack(mat, 1);
		setName(name);
		this.name = name;
		PotionMeta pm = (PotionMeta) getItemMeta();
		pm.setMainEffect(pet);
		setItemMeta(pm);
	}

	public List<String> getLore() {
		return itemStack.getItemMeta().getLore();
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta im = getItemMeta();
		im.setLore(lore);
		itemStack.setItemMeta(im);
		return this;
	}

	public ItemBuilder clearLore() {
		List<String> lore = Arrays.asList();
		ItemMeta im = getItemMeta();
		im.setLore(lore);
		setItemMeta(im);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta im = getItemMeta();
		im.setDisplayName(name);
		setItemMeta(im);
		return this;
	}

	public ItemBuilder hideDetails() {
		ItemMeta im = getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE);
		setItemMeta(im);

		return this;
	}

	public ItemBuilder enchant() {
		itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		return this;
	}

	public ItemBuilder enchant(Enchantment enchant) {
		itemStack.addEnchantment(enchant, 1);
		return this;
	}

	public ItemBuilder enchant(Enchantment enchant, int level) {
		itemStack.addEnchantment(enchant, level);
		return this;
	}

	public void addToLore(String add) {
		List<String> lore1 = getLore();
		if(lore1.isEmpty()) {
			lore1 = Arrays.asList(add);
		}
		lore1.add(add);
		setLore(lore1);
	}

	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder addNbtIdentifier(String identifier) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		compound.setString(identifier, "identifier");
		nmsItem.setTag(compound);

		itemStack = CraftItemStack.asBukkitCopy(nmsItem);
		return this;
	}

	public static boolean hasNbtIdentifier(ItemStack is, String identifier) {
		try {
			net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
			NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
			return compound.hasKey(identifier);
		} catch (NullPointerException exception) {
			return false;
		}
	}

	public ItemMeta getItemMeta() {
		return itemStack.getItemMeta();
	}

	public void setItemMeta(ItemMeta meta) {
		itemStack.setItemMeta(meta);
	}

	public ItemStack build() {
		return itemStack;
	}
}
