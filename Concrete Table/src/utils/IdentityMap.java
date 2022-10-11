package utils;

import datasource.Gateway;

import java.util.HashMap;

public class IdentityMap {
    private static IdentityMap identityMap;

    HashMap<Long, Gateway> gatewayMap = new HashMap<>();
    private IdentityMap() {}

    public static IdentityMap getInstance() {
        if (identityMap == null) {
            identityMap = new IdentityMap();
        }
        return identityMap;
    }

    // Get the gateway from the map based on the id
    public Gateway getGateway(long id) {
        return gatewayMap.get(id);
    }

    // Add a gateway to the map based on the id and the gateway
    public void addGateway(long id, Gateway gateway) {
        gatewayMap.put(id, gateway);
    }
}
