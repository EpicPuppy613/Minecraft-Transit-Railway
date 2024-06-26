package org.mtr.mod.model;

import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.Init;
import org.mtr.mod.render.MoreRenderLayers;

public class ModelDoorOverlayTop extends EntityModelExtension<EntityAbstractMapping> {

	private final ModelPartExtension bb_main;
	private final ModelPartExtension outer_roof_2_r1;
	private final ModelPartExtension outer_roof_1_r1;

	private static final Identifier TEXTURE_ID = new Identifier(Init.MOD_ID, "textures/block/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTop() {
		super(24, 3);

		bb_main = createModelPart();
		bb_main.setPivot(0, 24, 0);


		outer_roof_2_r1 = createModelPart();
		outer_roof_2_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureUVOffset(0, -12).addCuboid(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = createModelPart();
		outer_roof_1_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureUVOffset(0, -12).addCuboid(-1.1F, -21, 0, 0, 3, 12, 0, false);

		buildModel();
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(GraphicsHolder graphicsHolder, int light, int position) {
		graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(TEXTURE_ID));
		ModelTrainBase.renderMirror(bb_main, graphicsHolder, light / 4 * 3, position);
	}
}