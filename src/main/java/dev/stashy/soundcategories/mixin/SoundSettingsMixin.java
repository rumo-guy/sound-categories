package dev.stashy.soundcategories.mixin;

import dev.stashy.soundcategories.gui.CustomSoundOptionsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionsScreen.class)
public class SoundSettingsMixin
{
    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "method_19829", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Screen> cir) {
        cir.setReturnValue(new CustomSoundOptionsScreen(OptionsScreen.class.cast(this), settings));
    }
}
