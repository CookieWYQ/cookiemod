package com.cookiewyq.cookiemod.particle;

import com.mojang.serialization.Codec;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ScintillatingParticleType extends ParticleType<ScintillatingParticleData> {
    public ScintillatingParticleType() {
        super(false, ScintillatingParticleData.DESERIALIZER);
    }

    @Override
    public Codec<ScintillatingParticleData> func_230522_e_() {
        return Codec.unit(new ScintillatingParticleData(new Vector3d(0, 0, 0), new Color(0), 0));
    }
}
