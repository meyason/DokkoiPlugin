package org.meyason.dokkoi.event.player;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.meyason.dokkoi.Dokkoi;
import org.meyason.dokkoi.constants.GameItemKeyString;
import org.meyason.dokkoi.constants.GameState;
import org.meyason.dokkoi.game.Game;
import org.meyason.dokkoi.goal.GachaAddict;
import org.meyason.dokkoi.item.CustomItem;
import org.meyason.dokkoi.item.GameItem;
import org.meyason.dokkoi.item.gacha.GachaMachine;
import org.meyason.dokkoi.item.gacha.menu.GachaPointMenu;

import java.util.*;

public class InteractEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Game game = Game.getInstance();
        Player player = event.getPlayer();
        if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.END) return;

        if(game.getGameState() == GameState.PREP && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if(block instanceof Container){
                event.setCancelled(true);
            }

        }else if(game.getGameState() == GameState.IN_GAME){

            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if(!item.hasItemMeta()){
                return;
            }
            player.sendMessage("aaa");
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();

            NamespacedKey itemKey = new NamespacedKey(Dokkoi.getInstance(), GameItemKeyString.ITEM_NAME);
            if(container.has(itemKey, PersistentDataType.STRING) && Objects.equals(container.get(itemKey, PersistentDataType.STRING), "gacha_machine")) {
                player.sendMessage("bbb");

                CustomItem customItem = CustomItem.getItem(item);
                if(customItem == null){
                    player.sendMessage("ccc");
                    return;
                }
                if (customItem.isUnique && customItem.getId().equals(GachaMachine.id)) {
                    player.sendMessage("dddd");
                    event.setCancelled(true);
                    if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        player.sendMessage("eeee");
                        ItemStack newItem = GachaMachine.doGacha(event.getPlayer());
                        event.getPlayer().getInventory().addItem(Objects.requireNonNull(newItem));
                    } else {
                        player.sendMessage("ffff");
                        GachaPointMenu menu = new GachaPointMenu();
                        menu.sendMenu(event.getPlayer());
                    }
                }
            }

        }


    }
}
