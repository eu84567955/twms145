package client.messages.commands;
import client.MapleClient;
import client.MapleStat;
import constants.ServerConstants.PlayerGMRank;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

import java.util.Arrays;
import java.util.List;

public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    public static class 經驗值歸零 extends AbstractsCommandExecute {

        @Override
        public boolean execute(MapleClient c, List<String> args) {
            c.getPlayer().setExp(0);
            c.getPlayer().updateSingleStat(MapleStat.EXP, c.getPlayer().getExp());
            c.getPlayer().showInfo("指令", true, "經驗值歸零成功。");
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "@fixExp - 經驗值歸零";
        }
    }

    public static class 怪物 extends AbstractsCommandExecute {

        @Override
        public boolean execute(MapleClient c, List<String> args) {
            MapleMonster mob = null;
            for (final MapleMapObject obj : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) obj;
                if (mob.isAlive()) {
                    c.getPlayer().dropMessage(-11, "怪物: " + mob.toString());
                    break; //only one
                }
            }
            if (mob == null) {
                c.getPlayer().dropMessage(-11, "沒找到任何怪物");
            }
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "@怪物 - 怪物資訊";
        }
    }

    public static class 克隆我 extends AbstractsCommandExecute {

        @Override
        public boolean execute(MapleClient c, List<String> args) {
            c.getPlayer().cloneLook();
            return true;
        }

        @Override
        public String getHelpMessage() {
            return "@克隆我 - 產生一個克隆人";
        }
    }

    public static class 清除克隆 extends AbstractsCommandExecute {
        @Override
        public boolean execute(MapleClient c, List<String> args) {
            c.getPlayer().dropMessage(-11, c.getPlayer().getCloneSize() + " 個克隆被清除了");
            c.getPlayer().disposeClones();
            return true;
        }
        @Override
        public String getHelpMessage() {
            return "@清除克隆 - 清除克隆人";
        }
    }

}
