package com.cookiewyq.cookiemod.model;// Made with Blockbench 4.9.1
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports



import com.cookiewyq.cookiemod.entity.TigerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TigerModel <T extends TigerEntity> extends EntityModel<T> {
	private final ModelRenderer bb_main;

	public TigerModel() {
		textureWidth = 128;
		textureHeight = 128;

		// 创建一个ModelRenderer对象并绑定到当前模型
		bb_main = new ModelRenderer(this);

		// 设置模型的旋转中心点在Y轴24.0F位置（通常用于调整模型与实体的对齐）
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);

		// 添加右侧前上角方块
		// 纹理偏移(12, 0)，坐标(3.0F, -4.0F, -11.0F)，尺寸(3x4x3)
		bb_main.setTextureOffset(12, 0).addBox(3.0F, -4.0F, -11.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);

		// 添加右侧后上角方块
		// 纹理偏移(9, 11)，坐标(3.0F, -4.0F, 8.0F)，尺寸(3x4x3)
		bb_main.setTextureOffset(9, 11).addBox(3.0F, -4.0F, 8.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);

		// 添加左侧前上角方块
		// 纹理偏移(0, 7)，坐标(-6.0F, -4.0F, -11.0F)，尺寸(3x4x3)
		bb_main.setTextureOffset(0, 7).addBox(-6.0F, -4.0F, -11.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);

		// 添加左侧后上角方块
		// 纹理偏移(0, 0)，坐标(-6.0F, -4.0F, 8.0F)，尺寸(3x4x3)
		bb_main.setTextureOffset(0, 0).addBox(-6.0F, -4.0F, 8.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);

		// 添加主体门框部分
		// 纹理偏移(0, 0)，坐标(-6.0F, -13.0F, -12.0F)，尺寸(12x9x24)
		bb_main.setTextureOffset(0, 0).addBox(-6.0F, -13.0F, -12.0F, 12.0F, 9.0F, 24.0F, 0.0F, false);

		// 添加底部装饰或支撑结构
		// 纹理偏移(0, 33)，坐标(-4.0F, -17.0F, -19.0F)，尺寸(8x8x7)
		bb_main.setTextureOffset(0, 33).addBox(-4.0F, -17.0F, -19.0F, 8.0F, 8.0F, 7.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}