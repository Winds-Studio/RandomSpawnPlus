package systems.kscott.randomspawnplus.util;

public enum Permission {

    RSP_COMMAND_WILD("rsp.command.wild"),
    RSP_COMMAND_WILD_OTHERS("rsp.command.wild.others"),
    RSP_COMMAND_WILD_BYPASS_COOLDOWN("rsp.command.wild.bypasscooldown"),
    RSP_COMMAND_WILDBYPASS_COST("rsp.command.wild.bypasscost"),
    RSP_RANDOMSPAWN("rsp.randomspawn"),
    RSP_MANAGE("rsp.manage");

    private final String perm;

    Permission(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return perm;
    }
}
