package org.ships.vessel.common.assits;

import org.core.CorePlugin;
import org.core.entity.living.human.player.User;
import org.ships.permissions.vessel.CrewPermission;
import org.ships.vessel.common.types.Vessel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * specifies that the implementation of the vessel has crew with different roles
 */
public interface CrewStoredVessel extends Vessel {

    /**
     * Gets the crew of the ship
     * @return a Map value containing the UUID of the player and their permission status
     */
    Map<UUID, CrewPermission> getCrew();

    /**
     * Gets the default crew permission for if the user who interacts with the ship is not specified within the map value above.
     * This is not to be granted to the player in the map.
     * @return the default permission
     */
    CrewPermission getDefaultPermission();

    /**
     * Gets the permission of the user. Note that it will default to the default permission if no user was found
     * @param user The player in question
     * @return The permission the user has on this ship
     */
    default CrewPermission getPermission(UUID user){
        CrewPermission permission = getCrew().get(user);
        if(permission == null){
            permission = getDefaultPermission();
        }
        return permission;
    }

    /**
     * Gets all the users with a permission.
     * @param permission The permission
     * @return The users with the specific permission
     */
    default Set<User> getUserCrew(CrewPermission permission){
        Set<User> users = new HashSet<>();
        getCrew(permission).stream().forEach(uuid -> CorePlugin.getServer().getOfflineUser(uuid).ifPresent(u -> users.add(u)));
        return users;
    }

    /**
     * Gets all the users with a permission.
     * @param permissionId The permission
     * @return The users with the specific permission
     */
    default Set<User> getUserCrew(String permissionId){
        Set<User> users = new HashSet<>();
        getCrew(permissionId).stream().forEach(uuid -> CorePlugin.getServer().getOfflineUser(uuid).ifPresent(u -> users.add(u)));
        return users;
    }

    /**
     * Gets all the users uuids with permission
     * @param permission The permission
     * @return The users uuids ith the specific permission
     */
    default Set<UUID> getCrew(CrewPermission permission){
        Map<UUID, CrewPermission> permissionMap = getCrew();
        return permissionMap.keySet().stream().filter(u -> permissionMap.get(u).equals(permission)).collect(Collectors.toSet());
    }

    /**
     * Gets all the users uuids with permission
     * @param permissionId The permission
     * @return The users uuids ith the specific permission
     */
    default Set<UUID> getCrew(String permissionId){
        Map<UUID, CrewPermission> permissionMap = getCrew();
        return permissionMap.keySet().stream().filter(u -> permissionMap.get(u).getId().equals(permissionId)).collect(Collectors.toSet());
    }
}
