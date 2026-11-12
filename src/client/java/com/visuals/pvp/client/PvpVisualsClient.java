package com.visuals.pvp.client;

import com.visuals.pvp.PvpVisuals;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.resources.Identifier;

public class PvpVisualsClient implements ClientModInitializer {
    private static Entity glowingTarget;
    private static boolean targetWasAlreadyGlowing;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Entity next = null;
            if (client.player != null && client.level != null && client.hitResult instanceof EntityHitResult hit
                    && hit.getType() == HitResult.Type.ENTITY && hit.getEntity() instanceof Player target
                    && target != client.player && target.isAlive()) {
                next = target;
            }

            if (glowingTarget != next) {
                if (glowingTarget != null && glowingTarget.isAlive()) {
                    glowingTarget.setGlowingTag(targetWasAlreadyGlowing);
                }
                glowingTarget = next;
                if (glowingTarget != null) {
                    targetWasAlreadyGlowing = glowingTarget.isCurrentlyGlowing();
                    glowingTarget.setGlowingTag(true);
                }
            }
        });

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(PvpVisuals.MOD_ID, "target_hud"),
                PvpVisualsClient::renderTargetHud);
    }

    private static void renderTargetHud(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.options.hideGui || !(mc.hitResult instanceof EntityHitResult hit)
                || !(hit.getEntity() instanceof Player target) || target == mc.player || !target.isAlive()) {
            return;
        }

        int x = 12;
        int y = graphics.guiHeight() - 104;
        int width = 174;
        int height = 88;
        graphics.fill(x, y, x + width, y + height, 0xD9101118);
        graphics.fill(x, y, x + width, y + 2, 0xFF35E66B);
        graphics.fill(x + 7, y + 7, x + 35, y + 35, 0xFF303744);

        String name = target.getName().getString();
        if (name.length() > 18) name = name.substring(0, 18);
        graphics.drawString(mc.font, Component.literal(name), x + 42, y + 8, 0xFFFFFFFF, true);

        float health = Math.max(0, target.getHealth());
        float maxHealth = Math.max(1, target.getMaxHealth());
        String hpText = String.format(java.util.Locale.ROOT, "%.1f / %.0f HP", health, maxHealth);
        graphics.drawString(mc.font, hpText, x + 42, y + 21, 0xFFFFD66B, true);

        int barX = x + 42;
        int barY = y + 34;
        int barW = 122;
        graphics.fill(barX, barY, barX + barW, barY + 6, 0xFF343943);
        graphics.fill(barX, barY, barX + Math.round(barW * Math.min(1f, health / maxHealth)), barY + 6,
                health / maxHealth > 0.5f ? 0xFF35E66B : health / maxHealth > 0.25f ? 0xFFFFC247 : 0xFFFF5555);

        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
        int itemX = x + 8;
        int itemY = y + 52;
        for (int i = 0; i < slots.length; i++) {
            ItemStack stack = target.getItemBySlot(slots[i]);
            graphics.fill(itemX + i * 26 - 2, itemY - 2, itemX + i * 26 + 22, itemY + 22, 0x883A414D);
            if (!stack.isEmpty()) {
                graphics.renderItem(stack, itemX + i * 26, itemY);
                graphics.renderItemDecorations(mc.font, stack, itemX + i * 26, itemY);
            }
        }
    }
}
