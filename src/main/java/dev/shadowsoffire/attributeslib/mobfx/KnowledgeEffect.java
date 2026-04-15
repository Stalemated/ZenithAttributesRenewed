package dev.shadowsoffire.attributeslib.mobfx;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import dev.shadowsoffire.attributeslib.ALConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class KnowledgeEffect extends MobEffect {

    public KnowledgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF4EE42);
        this.addAttributeModifier(AdditionalEntityAttributes.DROPPED_EXPERIENCE, "55688e2f-7db8-4d0b-bc90-eff194546c04", ALConfig.knowledgeMult, Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amp, AttributeModifier modifier) {
        int level = amp + 1;

        if (ALConfig.knowledgeMultCurve == ALConfig.knowledgeMultCurveType.LINEAR) {
            return level * ALConfig.knowledgeMult;

        } else if (ALConfig.knowledgeMultCurve == ALConfig.knowledgeMultCurveType.EXPONENTIAL) {
            return Math.pow(ALConfig.knowledgeMult, level);

        } else {
            return (level * level) * ALConfig.knowledgeMult;
        }
    }

}
