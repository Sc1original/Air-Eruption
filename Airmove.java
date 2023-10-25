package me.wither.airkoskesh;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;


public class Airmove extends AirAbility implements AddonAbility {
    private static double DAMAGE;
    private static double RANGE;
    private static long COOLDOWN;
    public void serField(){
        DAMAGE = ConfigManager.getConfig().getDouble("ExtraAbilities.Sc1_original.Air.AirEruption.Damage");
        RANGE = ConfigManager.getConfig().getDouble("ExtraAbilities.Sc1_original.Air.AirEruption.Range");
        COOLDOWN = ConfigManager.getConfig().getLong("ExtraAbilities.Sc1_original.Air.AirEruption.Cooldown");
    }
    private Listener listener;
    private Permission perm;
    private Location location;
    private Vector direction;
    public Airmove(Player player) {
        super(player);
        location = player.getEyeLocation();
        direction = player.getLocation().getDirection();
        direction.multiply(0.8);
        bPlayer.addCooldown(this);

        serField();
        start();
    }

    @Override
    public void progress() {
        if(!bPlayer.canBendIgnoreBindsCooldowns(this)){
            remove();
            return;

        }

        if(location.distanceSquared(player.getLocation()) > RANGE * RANGE) {
            remove();
            return;
        }


        if(location.getBlock().getType().isSolid()){
            remove();
        }
        if(location.getBlock().isLiquid()){
            remove();

        }




        affectTargets();

        playAirbendingParticles(location, 8, 0.4, 0.4, 0.4);
        ParticleEffect.CLOUD.display(location, 5 , 0.4 , 0.4 , 0.4);
        if(ThreadLocalRandom.current().nextInt(6) == 0){
            playAirbendingSound(location);
        }


        location.add(direction);

    }
    private void affectTargets() {
        List<Entity> targets = GeneralMethods.getEntitiesAroundPoint(location, 1);
        for (Entity target : targets) {

        if(target.getUniqueId() == player.getUniqueId()){
            continue;
        }

        target.setVelocity(direction);

        DamageHandler.damageEntity(target , DAMAGE , this);
        target.setFireTicks(0);

        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }
    @Override
    public String getDescription() {
        return "Skilled Air benders can Throw a powerful Eruption of Air with have" +
                " continuous amount of damage if your enemy gets stuck by it!";

    }

    @Override
    public String getName() {
        return "AirEruption";
    }

    @Override
    public Location getLocation() {
        return location;

    }

    @Override
    public void load() {
            listener = new AirmoveListener();

            ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
            perm = new Permission("bending.ability.aireruption");
            perm.setDefault(PermissionDefault.OP);
            ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
            ConfigManager.getConfig().addDefault("ExtraAbilities.Sc1_original.Air.AirEruption.Damage", 1.5);
            ConfigManager.getConfig().addDefault("ExtraAbilities.Sc1_original.Air.AirEruption.Range", 25);
            ConfigManager.getConfig().addDefault("ExtraAbilities.Sc1_original.Air.AirEruption.Cooldown", 3000);
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(listener);
        ProjectKorra.plugin.getServer().getPluginManager().removePermission(perm);


    }

    @Override
    public String getAuthor() {
        return "sc1_original";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
