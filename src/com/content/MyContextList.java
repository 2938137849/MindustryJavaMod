package com.content;

import com.content.blocks.CommendBlock;
import com.content.blocks.ItemChange;
import com.content.blocks.LinkCoreBlock;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.gen.Icon;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MyContextList implements ContentList {
  public static Block
     Bin_Block1,
     Bin_Block2,
     Bin_Block3;

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
        this.localizedName = "������";
        this.description = "������";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
        commend = table -> {
          table.button(Icon.upOpen, Styles.clearTransi, (() -> {
            Vars.logic.skipWave();
          })).size(50).tooltip("��һ��");
          table.button(Icon.warningSmall, Styles.clearTransi, (() -> {
            for (int i = 0; i < 10; i++) {
              Vars.logic.runWave();
            }
          })).size(50).tooltip("��10��");
        };
      }
    };
  }

}
