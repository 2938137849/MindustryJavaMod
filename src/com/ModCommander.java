package com;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.gen.Entityc;
import mindustry.gen.Firec;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import java.lang.reflect.Field;

/**
 * @author bin
 * @version 1.0.0
 */
public class ModCommander {
  public static void fire(String[] args) {
    Groups.all.each(syncs -> syncs instanceof Firec, Entityc::remove);
    Tools.chat("[green]ȫͼѸ�����[]");
  }

  public static void waveSpacing(String[] args) {
    try {
      if (args.length == 1) {
        Vars.state.rules.waveSpacing = Integer.parseInt(args[0]);
      }
      Tools.chat("��ǰ�������: %s", Vars.state.rules.waveSpacing);
    } catch (NumberFormatException numberFormatException) {
      Tools.chat(("arg must be Int : %s"), args[0]);
    }
  }

  public static void buildSpeed(String[] args) {
    try {
      if (args.length == 1) {
        Vars.state.rules.buildSpeedMultiplier = Integer.parseInt(args[0]);
      }
      Tools.chat("��ǰ�����ٶ�: %s", Vars.state.rules.buildSpeedMultiplier);
    } catch (NumberFormatException numberFormatException) {
      Tools.chat(("arg must be Int : %s"), args[0]);
    }
  }

  public static void callUnit(String[] args, Player player) {
    try {
      if (args.length >= 1) {
        Field field = UnitTypes.class.getField(args[0]);
        Object o = field.get(UnitTypes.class);
        if (o instanceof UnitType) {
          Unit unit = ((UnitType)o).create(player.team());
          unit.set(player);
          unit.add();
          Tools.chat("�ٻ� %s �ɹ�", ((UnitType)o).localizedName);
        } else {
          Tools.chat("�ٻ�ʧ��:��Ӧ��λ���ʹ���");
        }
      } else {
        Tools.chat("��Ҫ����");
      }
    } catch (NoSuchFieldException e) {
      Tools.chat("��Ӧ��λδ�ҵ�");
    } catch (IllegalAccessException e) {
      Tools.chat(e.getLocalizedMessage());
    }
  }
}
