package io.github.cyfrit.mf3ff.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
abstract class PlayerMixin {
    private static final float LEGACY_LEVEL_THREE_MULTIPLIER = 0.0027F;
    private static final float LEGACY_LEVEL_FOUR_PLUS_MULTIPLIER = 0.00081F;

    @Redirect(
            method = "getDestroySpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;pow(DD)D"
            )
    )
    private double mf3ff$restoreLegacyMultiplier(double base, double exponent) {
        if (exponent == 3.0D) {
            return LEGACY_LEVEL_THREE_MULTIPLIER;
        }
        if (exponent >= 4.0D) {
            return LEGACY_LEVEL_FOUR_PLUS_MULTIPLIER;
        }

        return Math.pow(base, exponent);
    }
}
