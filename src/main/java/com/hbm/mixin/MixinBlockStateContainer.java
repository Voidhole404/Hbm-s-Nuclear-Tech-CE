package com.hbm.mixin;

import com.hbm.util.SectionPalette;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateContainer.class)
public abstract class MixinBlockStateContainer {
    @Unique
    public volatile SectionPalette.Data hbm$paletteData;

    // Hook stable vanilla lifecycle methods instead of Forge's added
    // setBits(int, boolean), which has no standard MCP mapping and may be
    // replaced or renamed by other coremods.
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hbm$publishInitialPalette(CallbackInfo ci) {
        hbm$publishPalette();
    }

    @Inject(method = "onResize", at = @At("RETURN"))
    private void hbm$publishResizedPalette(int bits, IBlockState state, CallbackInfoReturnable<Integer> cir) {
        hbm$publishPalette();
    }

    @Inject(method = "read", at = @At("RETURN"))
    private void hbm$publishReadPalette(PacketBuffer buffer, CallbackInfo ci) {
        hbm$publishPalette();
    }

    @Unique
    private void hbm$publishPalette() {
        SectionPalette.resized((BlockStateContainer) (Object) this);
    }
}
