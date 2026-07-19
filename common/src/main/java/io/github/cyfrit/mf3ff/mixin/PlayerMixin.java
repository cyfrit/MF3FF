package io.github.cyfrit.mf3ff.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
abstract class PlayerMixin {
    private static final float LEGACY_LEVEL_THREE_MULTIPLIER = 0.0027F;
    private static final float LEGACY_LEVEL_FOUR_PLUS_MULTIPLIER = 0.00081F;
    private static final double FIXED_MULTIPLIER_BASE = 0.3D;

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void mf3ff$restoreLegacyMultiplier(
            BlockState state,
            CallbackInfoReturnable<Float> callback
    ) {
        Player player = (Player) (Object) this;
        MobEffectInstance miningFatigue = player.getEffect(MobEffects.MINING_FATIGUE);

        if (miningFatigue == null) {
            return;
        }

        int amplifier = miningFatigue.getAmplifier();
        float speed = callback.getReturnValue();

        if (amplifier == 2) {
            float fixedMultiplier = (float) Math.pow(FIXED_MULTIPLIER_BASE, 3);
            callback.setReturnValue(speed / fixedMultiplier * LEGACY_LEVEL_THREE_MULTIPLIER);
        } else if (amplifier >= 3) {
            float fixedMultiplier = (float) Math.pow(FIXED_MULTIPLIER_BASE, amplifier + 1.0D);
            callback.setReturnValue(speed / fixedMultiplier * LEGACY_LEVEL_FOUR_PLUS_MULTIPLIER);
        }
    }
}
