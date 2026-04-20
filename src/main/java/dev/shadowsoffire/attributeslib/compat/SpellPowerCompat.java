package dev.shadowsoffire.attributeslib.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.SpellPowerMechanics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SpellPowerCompat {

    public static double getRealSpellValue(Attribute attribute, LivingEntity entity, double originalValue) {
        try {

            for (SpellSchool school : SpellSchools.all()) {
                if (school.attribute == attribute) {
                    return SpellPower.getSpellPower(school, entity).baseValue();
                }
            }

            if (SpellPowerMechanics.CRITICAL_CHANCE.attribute == attribute) {
                double sp = 100 + SpellPower.getSpellPower(SpellSchools.ARCANE, entity).criticalChance() * 100;
                BigDecimal roundedSp = new BigDecimal(sp).setScale(2, RoundingMode.HALF_EVEN);

                return roundedSp.doubleValue();
            }
            if (SpellPowerMechanics.CRITICAL_DAMAGE.attribute == attribute) {
                return SpellPower.getSpellPower(SpellSchools.ARCANE, entity).criticalDamage() * 100;
            }
            if (SpellPowerMechanics.HASTE.attribute == attribute) {
                return SpellPower.getHaste(entity, SpellSchools.ARCANE) * 100;
            }

        } catch (Throwable ignored) {}

        return originalValue;
    }

    public static double getRealSpellBaseValue(Attribute attribute, double originalBase) {
        try {
            if (SpellPowerMechanics.CRITICAL_CHANCE.attribute == attribute) {
                return originalBase + 5.0;
            }
            if (SpellPowerMechanics.CRITICAL_DAMAGE.attribute == attribute) {
                return originalBase + 50.0;
            }
        } catch (Throwable ignored) {}

        return originalBase;
    }

    public static double getSpellPowerBonus(Attribute attribute, double spValue, double spBaseValue, double value, double baseValue) {
        for (SpellSchool school : SpellSchools.all()) {
            if (school.attribute == attribute) {
                return (spValue / value) - 1;
            }
        }
        if (SpellPowerMechanics.HASTE.attribute == attribute || SpellPowerMechanics.CRITICAL_CHANCE.attribute == attribute || SpellPowerMechanics.CRITICAL_DAMAGE.attribute == attribute) {
            return ((spValue - spBaseValue) - (value - baseValue)) / 100;
        }
        else return 0;
    }
}