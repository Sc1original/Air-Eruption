package me.wither.airkoskesh;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AirmoveListener implements Listener {
    @EventHandler
    public void Onclick(PlayerInteractEvent event){
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        BendingPlayer bplayer = BendingPlayer.getBendingPlayer(player);

        if(bplayer.canBend(CoreAbility.getAbility(Airmove.class))){
            new Airmove(player);

        }




    }
}
