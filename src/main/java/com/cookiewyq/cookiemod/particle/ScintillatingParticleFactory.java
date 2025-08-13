package com.cookiewyq.cookiemod.particle;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ScintillatingParticleFactory implements IParticleFactory<ScintillatingParticleData> {
    private final IAnimatedSprite sprites;

    public ScintillatingParticleFactory(IAnimatedSprite sprite) {
        this.sprites = sprite;
    }

    @Override
    public Particle makeParticle(ScintillatingParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ScintillatingParticle particle = new ScintillatingParticle(worldIn, x, y, z, typeIn.getSpeed(), typeIn.getColor(), typeIn.getDiameter());
        particle.selectSpriteRandomly(sprites);
        return particle;
    }
}
