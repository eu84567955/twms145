/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools.packet;

import constants.GameConstants;
import handling.SendPacketOpcode;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleNodes.MapleNodeInfo;
import server.movement.ILifeMovementFragment;
import server.status.MonsterStatus;
import server.status.MonsterStatusEffect;
import tools.data.MaplePacketLittleEndianWriter;
import tools.types.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MobPacket {

    public static byte[] damageMonster(final int oid, final int damage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(0);
        if (damage > Integer.MAX_VALUE || damage < 0) {
            mplew.writeInt(Integer.MAX_VALUE);
        } else {
            mplew.writeInt(damage);
        }

        return mplew.getPacket();
    }

    public static byte[] damageFriendlyMob(final MapleMonster mob, int damage, final boolean display) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(mob.getObjectId());
        mplew.write(display ? 1 : 2); //false for when shammos changes map!
        if (damage > Integer.MAX_VALUE) {
            mplew.writeInt(Integer.MAX_VALUE);
        } else {
            mplew.writeInt(damage);
        }
        if (mob.getHp() > Integer.MAX_VALUE) {
            mplew.writeInt((int) ((mob.getHp() / mob.getMobMaxHp()) * Integer.MAX_VALUE));
        } else {
            mplew.writeInt((int) mob.getHp());
        }
        if (mob.getMobMaxHp() > Integer.MAX_VALUE) {
            mplew.writeInt(Integer.MAX_VALUE);
        } else {
            mplew.writeInt((int) mob.getMobMaxHp());
        }
        return mplew.getPacket();
    }

    public static byte[] killMonster(final int oid, final int animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
        mplew.writeInt(oid);
        int fixed = animation == 4 ? 2 : animation;
        mplew.write(fixed); // 0 = dissapear, 1 = fade out, 2+ = special
        if (fixed == 4) {
            mplew.writeInt(-1);
        }

        return mplew.getPacket();
    }

    public static byte[] killAswanMonster(final int oid, final int animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(animation); // 0 = dissapear, 1 = fade out, 2+ = special
        if (animation == 4) {
            mplew.writeInt(-1);
        }

        return mplew.getPacket();
    }

    public static byte[] suckMonster(final int oid, final int chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(4);
        mplew.writeInt(chr);

        return mplew.getPacket();
    }

    public static byte[] healMonster(final int oid, final int heal) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.writeInt(-heal);
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }

    public static byte[] MobToMobDamage(final int oid, final int dmg, final int mobid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOB_TO_MOB_DAMAGE.getValue());
        mplew.writeInt(oid);
        mplew.write(0); // looks like the statEffect, must be > -2
        mplew.writeInt(dmg);
        mplew.writeInt(mobid);
        mplew.write(1); // ?

        return mplew.getPacket();
    }

    public static byte[] getMobSkillEffect(final int oid, final int skillid, final int cid, final int skilllevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SKILL_EFFECT_MOB.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(skillid); // 3110001, 3210001, 13110009, 2210000
        mplew.writeInt(cid);
        mplew.writeShort(skilllevel);

        return mplew.getPacket();
    }

    public static byte[] getMobCoolEffect(final int oid, final int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ITEM_EFFECT_MOB.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(itemid); // 2022588

        return mplew.getPacket();
    }

    public static byte[] showMonsterHP(int oid, int remhppercentage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_MONSTER_HP.getValue());
        mplew.writeInt(oid);
        mplew.write(remhppercentage);

        return mplew.getPacket();
    }

    public static byte[] showCygnusAttack(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CYGNUS_ATTACK.getValue());
        mplew.writeInt(oid); // mob must be 8850011

        return mplew.getPacket();
    }

    public static byte[] showMonsterResist(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_RESIST.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(0);
        mplew.writeShort(1); // resist >0
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] showBossHP(final MapleMonster mob) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(5);
        mplew.writeInt(mob.getId() == 9400589 ? 9300184 : mob.getId()); //hack: MV cant have boss hp bar
        if (mob.getHp() > Integer.MAX_VALUE) {
            mplew.writeInt((int) (((double) mob.getHp() / mob.getMobMaxHp()) * Integer.MAX_VALUE));
        } else {
            mplew.writeInt((int) mob.getHp());
        }
        if (mob.getMobMaxHp() > Integer.MAX_VALUE) {
            mplew.writeInt(Integer.MAX_VALUE);
        } else {
            mplew.writeInt((int) mob.getMobMaxHp());
        }
        mplew.write(mob.getStats().getTagColor());
        mplew.write(mob.getStats().getTagBgColor());
        mplew.writeZeroBytes(30);
        return mplew.getPacket();
    }

    public static byte[] showBossHP(final int monsterId, final long currentHp, final long maxHp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(5);
        mplew.writeInt(monsterId); //has no image
        if (currentHp > Integer.MAX_VALUE) {
            mplew.writeInt((int) (((double) currentHp / maxHp) * Integer.MAX_VALUE));
        } else {
            mplew.writeInt((int) (currentHp <= 0 ? -1 : currentHp));
        }
        if (maxHp > Integer.MAX_VALUE) {
            mplew.writeInt(Integer.MAX_VALUE);
        } else {
            mplew.writeInt((int) maxHp);
        }
        mplew.write(6);
        mplew.write(5);

        //colour legend: (applies to both colours)
        //1 = red, 2 = dark blue, 3 = light green, 4 = dark green, 5 = black, 6 = light blue, 7 = purple
        mplew.writeZeroBytes(30);
        return mplew.getPacket();
    }

    public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<ILifeMovementFragment> moves) {
        return moveMonster(useskill, skill, unk, oid, startPos, moves, null, null);
    }

    public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<ILifeMovementFragment> moves, final List<Integer> unk2, final List<Pair<Integer, Integer>> unk3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_MONSTER.getValue());
        mplew.writeInt(oid);

        mplew.write(useskill ? 1 : 0);
        mplew.write(skill);

        mplew.writeInt(unk);
        mplew.write(unk3 == null ? 0 : unk3.size()); // For each, 2 short
        if (unk3 != null) {
            for (Pair<Integer, Integer> i : unk3) {
                mplew.writeShort(i.left);
                mplew.writeShort(i.right);
            }
        }
        mplew.write(unk2 == null ? 0 : unk2.size()); // For each, 1 short
        if (unk2 != null) {
            unk2.forEach(mplew::writeShort);
        }
        mplew.writeInt(0);
        mplew.writePos(startPos);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }


    public static byte[] movePokemon(int oid, Point startPos, List<ILifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(0); // For each, 2 short
        mplew.write(0); // For each, 1 short
        mplew.writePos(startPos);
        mplew.writeShort(8);
        mplew.writeShort(1);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static void SingleProcessStatSet(MaplePacketLittleEndianWriter mplew, MonsterStatusEffect buff) {
        List<MonsterStatusEffect> ss = new ArrayList<>();
        ss.add(buff);
        ProcessStatSet(mplew, ss);
    }

    public static void ProcessStatSet(MaplePacketLittleEndianWriter mplew, List<MonsterStatusEffect> buffs) {
        EncodeTemporary(mplew, buffs);
        mplew.writeShort(2);
        mplew.write(1);
        // if (MobStat::IsMovementAffectingStat)
        mplew.write(1);
    }

    public static byte[] spawnMonster(MapleMonster life, int spawnType, int link) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER.getValue());
        mplew.writeInt(life.getObjectId());
        boolean isLocalMob = true;
        mplew.writeBool(isLocalMob);
        mplew.writeInt(life.getId());
        if (isLocalMob) {
            addMonsterStatus(mplew, life);
            List<MonsterStatusEffect> buffs = new ArrayList<>(life.getStati().values());
            EncodeTemporary(mplew, buffs);
            mplew.writePos(life.getTruePosition());
            mplew.write(life.getStance());
            mplew.writeShort(0);
            mplew.writeShort(life.getFh());
            mplew.write(spawnType);
            if ((spawnType == -3) || (spawnType >= 0)) {
                mplew.writeInt(link);
            }
            mplew.write(life.getCarnivalTeam());
            mplew.writeInt(63000);
            mplew.write(0);
            if (life.getId() / 10000 == 961) {
                mplew.writeMapleAsciiString("");
            }
        }
        return mplew.getPacket();
    }

    public static byte[] spawnAswanMonster(MapleMonster life, int spawnType, int link) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ASWAN_SPAWN_MONSTER.getValue());
        mplew.writeInt(life.getObjectId());
        boolean isLocalMob = true;
        mplew.writeBool(isLocalMob);
        if (isLocalMob) {
            mplew.writeInt(life.getId());
            addMonsterStatus(mplew, life);
            List<MonsterStatusEffect> buffs = new ArrayList<>(life.getStati().values());
            EncodeTemporary(mplew, buffs);
            mplew.writePos(life.getTruePosition());
            mplew.write(life.getStance());
            mplew.writeShort(0);
            mplew.writeShort(life.getFh());
            mplew.write(spawnType);
            if ((spawnType == -3) || (spawnType >= 0)) {
                mplew.writeInt(link);
            }
            mplew.write(life.getCarnivalTeam());
            mplew.writeInt(63000);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(-1);
        }
//        System.out.println("spawnAswanMonster: " + mplew.getPacket());
        return mplew.getPacket();
    }


    public static void addMonsterStatus(MaplePacketLittleEndianWriter mplew, MapleMonster life) {
        if (life.getStati().size() <= 1) {
            life.addEmpty(); //not done yet lulz ok so we add it now for the lulz
        }
        mplew.write(life.getChangedStats() != null ? 1 : 0);
        if (life.getChangedStats() != null) {
            mplew.writeInt(life.getChangedStats().hp > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) life.getChangedStats().hp);
            mplew.writeInt(life.getChangedStats().mp);
            mplew.writeInt(life.getChangedStats().exp);
            mplew.writeInt(life.getChangedStats().watk);
            mplew.writeInt(life.getChangedStats().matk);
            mplew.writeInt(life.getChangedStats().PDRate);
            mplew.writeInt(life.getChangedStats().MDRate);
            mplew.writeInt(life.getChangedStats().acc);
            mplew.writeInt(life.getChangedStats().eva);
            mplew.writeInt(life.getChangedStats().pushed);
            mplew.writeInt(life.getChangedStats().level);
        }
    }

    public static byte[] controlMonster(MapleMonster life, boolean newSpawn, boolean aggro) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(aggro ? 2 : 1);
        mplew.writeInt(life.getObjectId());
        boolean isLocalMob = true;
        mplew.writeBool(isLocalMob);
        if (isLocalMob) {
            mplew.writeInt(life.getId());
            addMonsterStatus(mplew, life);
            List<MonsterStatusEffect> buffs = new ArrayList<>(life.getStati().values());
            EncodeTemporary(mplew, buffs);
            mplew.writePos(life.getTruePosition());
            mplew.write(life.getStance());
            mplew.writeShort(0);
            mplew.writeShort(life.getFh());
            mplew.write(newSpawn ? -2 : life.isFake() ? -4 : -1);
            mplew.write(life.getCarnivalTeam());
            mplew.writeInt(63000);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(-1);
        }
        return mplew.getPacket();
    }

    public static byte[] controlAswanMonster(MapleMonster life, boolean newSpawn, boolean aggro) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ASWAN_SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(aggro ? 2 : 1);
        mplew.writeInt(life.getObjectId());
        mplew.write(1);
        mplew.writeInt(life.getId());
        addMonsterStatus(mplew, life);
        List<MonsterStatusEffect> buffs = new ArrayList<>(life.getStati().values());
        EncodeTemporary(mplew, buffs);
        mplew.writePos(life.getTruePosition());
        mplew.write(life.getStance());
        mplew.writeShort(0);
        mplew.writeShort(life.getFh());
        mplew.write(newSpawn ? -2 : life.isFake() ? -4 : -1);
        mplew.write(life.getCarnivalTeam());
        mplew.writeInt(63000);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(-1);

        // System.out.println("controlAswanMonster: " + mplew.getPacket());
        return mplew.getPacket();
    }


    public static byte[] stopControllingMonster(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(0);
        mplew.writeInt(oid);

        return mplew.getPacket();
    }

    public static byte[] stopControllingAswanMonster(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ASWAN_SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(0);
        mplew.writeInt(oid);

        //System.out.println("stopControllingAswanMonster: " + mplew.getPacket());
        return mplew.getPacket();
    }

    public static byte[] makeAswanMonsterInvisible(MapleMonster life) {
        //System.out.println("accessing makeAswanMonsterInvisible");
        return spawnAswanMonster(life, -4, 0);
    }


    public static byte[] makeMonsterReal(MapleMonster life) {
        return spawnMonster(life, -1, 0);
    }

    public static byte[] makeAswanMonsterReal(MapleMonster life) {
//        System.out.println("accessing makeAswanMonsterReal");
        return spawnAswanMonster(life, -1, 0);
    }


    public static byte[] makeMonsterFake(MapleMonster life) {
        return spawnMonster(life, -4, 0);
    }

    public static byte[] makeMonsterEffect(MapleMonster life, int effect) {
        return spawnMonster(life, effect, 0);
    }

    public static byte[] moveMonsterResponse(int objectid, short moveid, int currentMp, boolean useSkills) {
        return moveMonsterResponse(objectid, moveid, currentMp, useSkills, 0, 0);
    }

    public static byte[] moveMonsterResponse(int objectid, short moveid, int currentMp, boolean useSkills, int skillId, int skillLevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_MONSTER_RESPONSE.getValue());
        mplew.writeInt(objectid);
        mplew.writeShort(moveid);
        mplew.write(useSkills ? 1 : 0);
        mplew.writeShort(currentMp);
        mplew.write(skillId);
        mplew.write(skillLevel);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] applyMonsterStatus(MapleMonster mons, MonsterStatusEffect ms) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        SingleProcessStatSet(mplew, ms);
        return mplew.getPacket();
    }

    public static byte[] applyMonsterStatus(MapleMonster mons, List<MonsterStatusEffect> mse) {
        if ((mse.size() <= 0) || (mse.get(0) == null)) {
            return CWvsContext.enableActions();
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        ProcessStatSet(mplew, mse);
        return mplew.getPacket();
    }

    public static byte[] cancelMonsterStatus(MapleMonster mons, MonsterStatusEffect ms) {
        List<MonsterStatusEffect> mse = new ArrayList<>();
        mse.add(ms);
        return cancelMonsterStatus(mons, mse);
    }

    public static byte[] cancelMonsterStatus(MapleMonster mons, List<MonsterStatusEffect> mse) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        writeMaskFromList(mplew, mse);
        for (MonsterStatusEffect buff : mse) {
            if (buff.getStatus() == MonsterStatus.BLEED) {
                mplew.writeInt(0);
                int v6 = 0;
                mplew.writeInt(v6);
                if (v6 > 0) {
                    do {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        --v6;
                    } while (v6 == 0);
                }
            }
        }
        mplew.write(2);
        // if (MobStat::IsMovementAffectingStat)
        mplew.write(1);
//        System.out.println("cancelMonsterStatus");
        return mplew.getPacket();
    }

    public static byte[] talkMonster(int oid, int itemId, int seconds, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.TALK_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(seconds > 0 ? (seconds * 1000) : 500);
        mplew.writeInt(itemId);
        mplew.write(itemId <= 3 ? 0 : 1);
        mplew.write(msg == null || msg.length() <= 0 ? 0 : 1);
        if (msg != null && msg.length() > 0) {
            mplew.writeMapleAsciiString(msg);
        }
        mplew.writeInt(1); // direction
        return mplew.getPacket();
    }

    public static byte[] removeTalkMonster(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_TALK_MONSTER.getValue());
        mplew.writeInt(oid);
        return mplew.getPacket();
    }

    public static byte[] getNodeProperties(final MapleMonster objectid, final MapleMap map) {
        if (objectid.getNodePacket() != null) {
            return objectid.getNodePacket();
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MONSTER_PROPERTIES.getValue());
        mplew.writeInt(objectid.getObjectId());
        mplew.writeInt(map.getNodes().size());
        mplew.writeInt(objectid.getPosition().x);
        mplew.writeInt(objectid.getPosition().y);
        for (MapleNodeInfo mni : map.getNodes()) {
            mplew.writeInt(mni.x);
            mplew.writeInt(mni.y);
            mplew.writeInt(mni.attr);
            if (mni.attr == 2) { //msg
                mplew.writeInt(500); //? talkMonster
            }
        }
        mplew.writeInt(0);
        mplew.write(0); // tickcount, extra 1 int
        mplew.write(0);
        objectid.setNodePacket(mplew.getPacket());
        return objectid.getNodePacket();
    }

    public static byte[] showMagnet(int mobid, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_MAGNET.getValue());
        mplew.writeInt(mobid);
        mplew.write(success ? 1 : 0);
        mplew.write(0); // times, 0 = once, > 0 = twice

        return mplew.getPacket();
    }

    public static byte[] catchMonster(int mobid, int itemid, byte success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CATCH_MONSTER.getValue());
        mplew.writeInt(mobid);
        mplew.writeInt(itemid);
        mplew.write(success);
        return mplew.getPacket();
    }

    public static byte[] showBossHPPlayer(int monsterId, long currentHp, long maxHp, byte tagColor, byte tagbgcolor) {
/* 208 */
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 210 */
        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
/* 211 */
        mplew.write(5);
/* 212 */
        mplew.writeInt(monsterId);
/* 213 */
        if (currentHp > 2147483647L)
/* 214 */ mplew.writeInt((int) (currentHp / maxHp * 2147483647.0D));
/*     */
        else {
/* 216 */
            mplew.writeInt((int) (currentHp <= 0L ? -1L : currentHp));
/*     */
        }
/* 218 */
        if (maxHp > 2147483647L)
/* 219 */ mplew.writeInt(2147483647);
/*     */
        else {
/* 221 */
            mplew.writeInt((int) maxHp);
/*     */
        }
/* 223 */
        mplew.write(tagColor);
/* 224 */
        mplew.write(tagbgcolor);
/*     */ 
/* 229 */
        return mplew.getPacket();
/*     */
    }

    private static void writeMaskFromList(MaplePacketLittleEndianWriter mplew, Collection<MonsterStatusEffect> ss) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (MonsterStatusEffect statup : ss) {
            mask[(statup.getStatus().getPosition())] |= statup.getStatus().getValue();
        }
        for (int i = 0; i < mask.length; i++) {
            mplew.writeInt(mask[(i)]);
        }
    }


    public static void EncodeTemporary(MaplePacketLittleEndianWriter mplew, List<MonsterStatusEffect> buffs) {
        writeMaskFromList(mplew, buffs);
        Collections.sort(buffs, (o1, o2) -> {
            int val1 = o1.getStatus().getOrder();
            int val2 = o2.getStatus().getOrder();
            return (val1 < val2 ? -1 : (val1 == val2 ? 0 : 1));
        });
        Collection<MonsterStatus> buffstatus = new LinkedList<>();
        for (MonsterStatusEffect buff : buffs) {
            buffstatus.add(buff.getStatus());
            if (buff.getStatus() == MonsterStatus.DANAGED_ELEM_ATTR) {
                continue;
            }
            if (buff.getStatus() == MonsterStatus.BLEED) {
                List<MonsterStatusEffect> bleedBuffs = new ArrayList<>();
                //MonsterStatusEffect
                buffs.stream().filter((b)
                        -> (b.getStatus().getBitNumber() == MonsterStatus.BLEED.getBitNumber() && b.getMobSkill() != null))
                        .forEach(bleedBuffs::add);
                mplew.write(bleedBuffs.size());
                if (bleedBuffs.size() > 0) {
                    bleedBuffs.stream().forEach((b) -> {
                        mplew.writeInt(8695624);
                        mplew.writeInt(buff.getSkill()); // 技能ID
                        mplew.writeInt(7100); // 每秒傷害?
                        mplew.writeInt(1000); // 延遲毫秒 : dotInterval * 1000
                        mplew.writeInt(187277775);
                        mplew.writeInt(16450);
                        mplew.writeInt(15); // dotTime
                        mplew.writeInt(0);
                        mplew.writeInt(1);
                        mplew.writeInt(7100); // 每秒傷害?
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                    });
                }
                if (buff.getStatus() == MonsterStatus.WEAPON_DAMAGE_REFLECT)
                    mplew.writeInt(0);
                if (buff.getStatus() == MonsterStatus.MAGIC_DAMAGE_REFLECT)
                    mplew.writeInt(0);
                if (buff.getStatus() == MonsterStatus.WEAPON_DAMAGE_REFLECT || buff.getStatus() == MonsterStatus.MAGIC_DAMAGE_REFLECT) {
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                }
                continue;
            }
            if (buff.getStatus() == MonsterStatus.SUMMON) {
                mplew.writeBool(buff.getX() > 0);
                mplew.writeBool(buff.getX() > 0);
                continue;
            }
            if (buff.getStatus() == MonsterStatus.MOB_BUFF_42) {
                mplew.writeBool(buff.getX() > 0);
                continue;
            }
            mplew.writeInt(buff.getX());
            if (buff.getMobSkill() != null) {
                mplew.writeShort(buff.getMobSkill().getSkillId());
                mplew.writeShort(buff.getMobSkill().getSkillLevel());
            } else {
                mplew.writeInt(buff.getSkill());
            }
            mplew.writeShort((short) ((buff.getCancelTask() - System.currentTimeMillis()) / 1000));
        }

    }
}
