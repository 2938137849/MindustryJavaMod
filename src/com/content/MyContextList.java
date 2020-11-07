package com.content;

import arc.scene.ui.layout.Table;
import com.content.blocks.CommendBlock;
import com.content.blocks.ItemChange;
import com.content.blocks.LinkCoreBlock;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Firec;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MyContextList implements ContentList {
  public static Block
     Bin_Block1, Bin_Block2, Bin_Block3, Bin_Block4;

  @Override public void load() {
    Bin_Block1 = new ItemChange("Bin_Block1") {
      {
        this.localizedName = "����ת����";
        this.description = "������Ʒ,ת��Ϊ��������һ����Ʒ";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Block2 = new LinkCoreBlock("Bin_Block2") {
      {
        this.localizedName = "���Ӻ���������";
        this.description = "ͨ������ͨ�����������,ͬʱװ����װж��";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Block3 = new CommendBlock("Bin_Block3") {
      {
        this.localizedName = "ָ����";
        this.description = "���ɸ���ָ��";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
        commend = MyContextList::display;
      }
    };
    Bin_Block4 = new CommendBlock("Bin_Block4") {
      {
        this.localizedName = "�ٻ���";
        this.description = "�ٻ�ָ����λ";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);

        this.commend = (building, table) -> ItemSelection.buildTable(
           table,
           Vars.content.units(),
           () -> null,
           unitType -> {
             Unit unit = unitType.create(building.team());
             unit.set(building.x, building.y + 1);
             unit.add();
           }
        );
      }
    };
  }

  private static void display(Building building, Table table) {
    table.button(Icon.upOpen, Styles.clearTransi, (() -> {
      Vars.logic.skipWave();
    })).size(50).tooltip("��һ��");
    table.button(Icon.warningSmall, Styles.clearTransi, (() -> {
      for (int i = 0; i < 10; i++) {
        Vars.logic.runWave();
      }
    })).size(50).tooltip("��10��");
    table.button(Icon.file, Styles.clearTransi, () -> {
      Groups.all.each(syncs -> syncs instanceof Firec, Entityc::remove);
    }).size(50).tooltip("�������");
  }
}
