package com;

import arc.Core;
import arc.Events;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.CommandHandler;
import arc.util.Log;
import com.content.MyContextList;
import com.content.MyTechTreeList;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.NetClient;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unitc;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.values.ItemListValue;
import mindustry.world.modules.ItemModule;

import java.util.Arrays;

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
    handler.<Player>register("gameOver", "��������", (args, player) -> Logic.updateGameOver(player.team()));
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
      items.add(Arrays.asList(block.requirements));
      Table t = new Table(Styles.black3);
      t.touchable = Touchable.disabled;
      t.setPosition(e.tile.x, e.tile.y);
      new ItemListValue(false, block.requirements).display(t);
      t.actions(Actions.show(), Actions.delay(5, Actions.remove()));
      t.pack();
      Core.scene.add(t);
    }
  }

  private void unitDestroyEvent(EventType.UnitDestroyEvent e) {
    if (Vars.player.team().isEnemy(e.unit.team)) {
      ItemStack stack = Tools.getRandomItemStack(Vars.state.wave * times);
      Vars.player.core().items.add(stack.item, stack.amount);
      Table t = new Table(Styles.black3);
      t.touchable = Touchable.disabled;
      t.setPosition(e.unit.x, e.unit.y);
      t.add(new ItemDisplay(stack.item, stack.amount, false)).padRight(5.0F);
      t.actions(Actions.show(), Actions.delay(5, Actions.remove()));
      t.pack();
      Core.scene.add(t);
    }
  }

  private void chat(String format, Object... args) {
    NetClient.sendMessage(String.format(format, args), "Admin", Vars.player);
  }

  @Override
  public void loadContent() {
    Log.info("����TestMod����");
    new MyContextList().load();
    new MyTechTreeList().load();
  }

}
