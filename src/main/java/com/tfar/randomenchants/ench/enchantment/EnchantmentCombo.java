package com.tfar.randomenchants.ench.enchantment;

import com.tfar.randomenchants.Config;
import com.tfar.randomenchants.RandomEnchants;
import com.tfar.randomenchants.network.Message;
import com.tfar.randomenchants.network.PacketComboReset;
import com.tfar.randomenchants.util.EnchantUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.tfar.randomenchants.Config.Restriction.*;
import static com.tfar.randomenchants.RandomEnchants.ObjectHolders.COMBO;
import static net.minecraft.enchantment.EnchantmentType.WEAPON;

@Mod.EventBusSubscriber(modid = RandomEnchants.MODID)
public class EnchantmentCombo extends Enchantment {
  public EnchantmentCombo() {

    super(Rarity.RARE, WEAPON, new EquipmentSlotType[]{
            EquipmentSlotType.MAINHAND
    });
    this.setRegistryName("combo");
  }

  @Override
  public int getMinEnchantability(int level) {
    return 15;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return Config.ServerConfig.combo.get() != DISABLED && super.canApply(stack);
  }

  @Override
  public boolean isTreasureEnchantment() {
    return Config.ServerConfig.combo.get() == ANVIL;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return Config.ServerConfig.combo.get() != DISABLED && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public boolean isAllowedOnBooks() {
    return Config.ServerConfig.combo.get() == NORMAL;
  }

  private static boolean handled = false;

  @Override
  public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    if (handled) {
      handled = false;
      return;
    }
    if (!(user instanceof PlayerEntity)) return;
    PlayerEntity p = (PlayerEntity) user;
    ItemStack stack = p.getHeldItemMainhand();
    CompoundNBT compound = stack.getOrCreateTag();
    target.attackEntityFrom(DamageSource.causePlayerDamage(p), compound.getInt("combo") * .5f);
    compound.putInt("combo", compound.getInt("combo") + 1);
    handled = true;
  }

  @SubscribeEvent
  public static void onMiss(PlayerInteractEvent.LeftClickEmpty e) {
    PlayerEntity p = e.getEntityPlayer();
    if (!EnchantUtils.hasEnch(e.getItemStack(), COMBO)) return;
    Message.INSTANCE.sendToServer(new PacketComboReset());
  }

  @SubscribeEvent
  public static void onHitBlock(PlayerInteractEvent.LeftClickBlock e) {
    PlayerEntity p = e.getEntityPlayer();
    if (!EnchantUtils.hasEnch(e.getItemStack(), COMBO)) return;
    Message.INSTANCE.sendToServer(new PacketComboReset());
  }
}



