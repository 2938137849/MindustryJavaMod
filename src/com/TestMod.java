package com;

import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import com.content.MyContextList;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.NetClient;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unitc;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.modules.ItemModule;

/**
 * @author bin
 */
@SuppressWarnings("unused")
public class TestMod extends Mod {
  private static boolean isOpen = false;
  private int times = 100;

  public TestMod() {
    Log.info(("����TestMod������"));
    init();
  }

  @Override
  public void init() {
    if (isOpen) {
      Log.err(("����TestMod������"));
      return;
    }
    isOpen = true;
    Events.on(EventType.UnitDestroyEvent.class, this::unitDestroyEvent);
    Events.on(EventType.BlockDestroyEvent.class, this::blockDestroyEvent);
  }

  @Override
  public void registerClientCommands(CommandHandler handler) {
    handler.register("fire", "ȫͼѸ�����", ModCommander::fire);
    handler.<Player>register("kill", "��ɱ", (args, player) -> player.unit().destroy());
    handler.register("killAll", "��ɱȫͼ��λ", (args) -> Groups.unit.forEach(Unitc::destroy));
    handler.register("runWave", "[Int]", "��һ��,Ĭ�ϲ���1", ModCommander::runWave);
    handler.register("skipWave", "��һ��(�����ʱ��)", (args) -> Vars.logic.skipWave());
    handler.<Player>register("gameOver", "��������", (args, player) -> Logic.gameOver(player.team()));
    handler.register("waveSpacing", "[Int]", "��ʾ/���ò������", ModCommander::waveSpacing);
    handler.register("buildSpeed", "[Int]", "��ʾ/���ý����ٶ�", ModCommander::buildSpeed);
    handler.<Player>register("setItemDrop", "[Int]", "��ʾ/���õ�λ����������Դ����,Ĭ��100", (args, player) -> {
      if (args.length == 1) {
        try {
          times = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
          chat(("arg must be INT : %s"), args[0]);
        }
      }
      chat(("��ǰ��λ����:����*%d"), times);
    });
    handler.register("callUnit", "<@Unit.name>", "����һ����Ӧ�ĵ�λˢ�������λ��", ModCommander::callUnit);
  }

  private void blockDestroyEvent(EventType.BlockDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.tile.team())) {
      Block block = e.tile.block();
      ItemModule items = Vars.player.team().items();
      if (items.length() == 0) {
        return;
      }
      StringBuilder sb = new StringBuilder();
      for (ItemStack itemStack : block.requirements) {
        items.add(itemStack.item, itemStack.amount);
        sb.append("  ")
           .append(itemStack.item.localizedName)
           .append(" : ")
           .append(itemStack.amount);
      }
      chat(("���� %s �������:%s"), block.localizedName, sb.toString());
    }
  }

  private void unitDestroyEvent(EventType.UnitDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.unit.team)) {
      ItemStack stack = Tools.getRandomItemStack(Vars.state.wave * times);
      Vars.player.team().items().add(stack.item, stack.amount);
      String s = stack.item.localizedName;
      chat(("���� %s ������� %s : %d"), e.unit.type().localizedName, s, stack.amount);

    }
  }

  private void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }

  @Override
  public void loadContent() {
    Log.info("����TestMod����");
    new MyContextList().load();
  }

}
