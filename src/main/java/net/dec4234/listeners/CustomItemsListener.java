package net.dec4234.listeners;

import net.dec4234.guis.content.items.ItemType;
import net.dec4234.src.KitPvPCoreMain;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class CustomItemsListener implements Listener {

	private static HashMap<UUID, Long> grappleCooldown = new HashMap<>();
	private static HashMap<UUID, Long> thorBackstabCooldown = new HashMap<>();
	private static Set<UUID> thorUsers = new HashSet<>();
	private static HashMap<Block, Long> cobbleBlocks = new HashMap<>();
	private static List<Block> toRemove = new ArrayList<>();
	private static Set<UUID> thorUsersToRemove = new HashSet<>();
	private static boolean isCobbleActive = false;
	private static boolean isThorActive = false;
	private static int cobble = 0;
	private static int thor = 0;

	/**
	 * Event to give functionality to the custom cobble blocks
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.COBBLESTONE) {
			if (ItemBuilder.hasNbtIdentifier(event.getItemInHand(), ItemType.BLOCKS.getIdentifier())) {
				event.setCancelled(false);
				addCobbleBlock(block);
			}
		}
	}

	/**
	 * Grapple event for Grappling Hook
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	public void onGrapple(PlayerFishEvent event) {
		Vector vector3;
		Entity entity;
		Block block;
		Player player = event.getPlayer();
		double d;

		// If a player has not waited 5 seconds since previous use, then stop them
		if (grappleCooldown.containsKey(player.getUniqueId()) && grappleCooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
			return;
		}

		// If the fishing rod in hand is a custom fishing rod and not just any
		if (player.getItemInHand() != null && !ItemBuilder.hasNbtIdentifier(player.getItemInHand(), ItemType.GRAPPLING_HOOK.getIdentifier())) {
			return;
		}

		if (event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)) {
			entity = event.getHook();
			block = entity.getWorld().getBlockAt(entity.getLocation().add(0.0, -0.25, 0.0));

			if (!block.isEmpty() && !block.isLiquid()) {

				vector3 = entity.getLocation().subtract(player.getLocation()).toVector();

				if (vector3.getY() < 0.0) { vector3.setY(0.0); }

				vector3.setX(vector3.getX() * 0.3);
				vector3.setY(vector3.getY() * 0.7 + 0.5);
				vector3.setZ(vector3.getZ() * 0.3);

				d = 7.5 * 7.5;
				if (vector3.clone().setY(0.0).lengthSquared() > d) {
					d = d / vector3.lengthSquared();
					vector3.setX(vector3.getX() * d);
					vector3.setZ(vector3.getZ() * d);
				}

				if (vector3.getY() > 1.5) { vector3.setY(1.5); }

				// Launch the player in the direction of the bobber
				player.setVelocity(vector3);

				// Add user to cooldown list
				grappleCooldown.remove(player.getUniqueId());                 // 5 seconds from now
				grappleCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (5 * 1000));
			}
		}
	}

	/**
	 * While a player is holding their thor hammer, give them Strength I
	 */
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());

		if (heldItem != null && ItemBuilder.hasNbtIdentifier(heldItem, ItemType.THOR_HAMMER.getIdentifier())) {
			addThorUser(player);
		}
	}

	/**
	 * Detect when a player is backstabbed by someone using their thor hammer
	 */
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player attacked = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();

			if(damager.getItemInHand() != null && ItemBuilder.hasNbtIdentifier(damager.getItemInHand(), ItemType.THOR_HAMMER.getIdentifier())) {
				// Stop processing if the user is on a cooldown
				if(thorBackstabCooldown.containsKey(damager.getUniqueId()) && thorBackstabCooldown.get(damager.getUniqueId()) > System.currentTimeMillis()) {
					return;
				}
				double difference = attacked.getLocation().getYaw() - damager.getLocation().getYaw();
				if(difference > 0 && difference < 75 || difference < 0 && difference > -75) {
					damager.sendMessage("§dBackstab! §6You dealt double damage to §c" + attacked.getName() + "§6!");
					event.setDamage(event.getDamage() * 2);

					thorBackstabCooldown.put(damager.getUniqueId(), System.currentTimeMillis() + (5 * 1000));
				}

			}
		}
	}

	private void addThorUser(Player player) {
		thorUsers.add(player.getUniqueId());

		if (!isThorActive) {
			initThor();
		}
	}

	private void initThor() {
		// Repeat the cycle of the call every 1.5 seconds
		thor = Bukkit.getScheduler().scheduleAsyncRepeatingTask(KitPvPCoreMain.getInstance(), this::cycleThor, 0, 30);
		isThorActive = true;
	}

	private void cycleThor() {
		// Convert back to a Syncronous action for thread safety (More or less)
		Bukkit.getScheduler().runTask(KitPvPCoreMain.getInstance(), () -> {
			for (UUID id : thorUsers) {
				Player p = Bukkit.getPlayer(id);
				if (p != null) {
					if (p.getItemInHand() != null && ItemBuilder.hasNbtIdentifier(p.getItemInHand(), ItemType.THOR_HAMMER.getIdentifier())) {
						// Remove any exisiting Strength to allow for a fresh Strength stack
						if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
							p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						}
						// Give the user Strength I for 3 seconds
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 0));
					} else {
						thorUsersToRemove.add(id);
					}
				}
			}
		});

		// Must muse seperate list to remove users to avoid ConccurentModificationException
		for (UUID uuid : thorUsersToRemove) {
			thorUsers.remove(uuid);
		}

		thorUsersToRemove.clear();

		if (thorUsers.isEmpty()) {
			cancelThor();
		}
	}

	private void cancelThor() {
		Bukkit.getScheduler().cancelTask(thor);
		isThorActive = false;
	}

	private void addCobbleBlock(Block block) {
		// Add the block to the list with a time 2 minutes from now
		cobbleBlocks.put(block, System.currentTimeMillis() + (60 * 2 * 1000));
		if (!isCobbleActive) {
			initCobble();
		}
	}

	private void despawn(Block block) {
		// Convert back to synchronous action
		Bukkit.getScheduler().runTask(KitPvPCoreMain.getInstance(), () -> {
			block.setType(Material.AIR);
		});
		cobbleBlocks.remove(block);
	}

	// Workaround for ConcurrentModificationException
	public void addToDespawnList(Block block) {
		toRemove.add(block);
	}

	private void initCobble() {
		cobble = Bukkit.getScheduler().scheduleAsyncRepeatingTask(KitPvPCoreMain.getInstance(), this::cycleCobble, 0, 20);
		isCobbleActive = true;
	}

	private void cancelCobble() {
		Bukkit.getScheduler().cancelTask(cobble);
		isCobbleActive = false;
	}

	public void cycleCobble() {
		for (Block block : cobbleBlocks.keySet()) {
			if (cobbleBlocks.get(block) < System.currentTimeMillis()) {
				// Cannot despawn stuff from here or else it would trigger a ConcurrentModificationException
				addToDespawnList(block);
			}
		}

		for (Block block : toRemove) {
			despawn(block);
		}

		if (cobbleBlocks.isEmpty()) {
			cancelCobble();
		}

	}

	/**
	 * Used to remove all exisiting blocks in the world
	 */
	public static void despawnAll() {
		if (!cobbleBlocks.isEmpty()) {
			for (Block block : cobbleBlocks.keySet()) {
				block.setType(Material.AIR);
				cobbleBlocks.remove(block);
			}
		}
	}
}
