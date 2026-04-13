package dev.shadowsoffire.attributeslib.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeSorter {

    private record AttributeData(String cleanName, boolean hasIcon) {}

    private static final Map<Attribute, AttributeData> CACHE = new ConcurrentHashMap<>();

    public static final Comparator<? super AttributeInstance> ICON_SAFE_COMPARATOR = (attr1, attr2) -> {
        Attribute a1 = attr1.getAttribute();
        Attribute a2 = attr2.getAttribute();

        AttributeData data1 = CACHE.computeIfAbsent(a1, AttributeSorter::computeData);
        AttributeData data2 = CACHE.computeIfAbsent(a2, AttributeSorter::computeData);

        if (data1.hasIcon() && !data2.hasIcon()) return -1;
        else if (!data1.hasIcon() && data2.hasIcon()) return 1;

        return data1.cleanName().compareToIgnoreCase(data2.cleanName());
    };

    private static AttributeData computeData(Attribute attribute) {
        String rawName = I18n.get(attribute.getDescriptionId());
        boolean hasIcon = containsIcon(rawName);

        String noFormatting = ChatFormatting.stripFormatting(rawName);
        if (noFormatting == null) noFormatting = "";

        String cleanName = stripIcons(noFormatting);

        return new AttributeData(cleanName, hasIcon);
    }

    private static boolean containsIcon(String input) {
        if (input == null || input.isEmpty()) return false;

        for (int i = 0; i < input.length(); ) {
            int codePoint = input.codePointAt(i);
            if (isIconCodePoint(codePoint)) {
                return true;
            }
            i += Character.charCount(codePoint);
        }
        return false;
    }

    private static boolean isIconCodePoint(int codePoint) {
        return (codePoint >= 0xE000 && codePoint <= 0xF8FF) || // Private Use Area
                (codePoint >= 0xF0000 && codePoint <= 0x10FFFF) || // Supplementary Private Use Area
                (codePoint >= 0x1CD00 && codePoint <= 0x1CDEF) || // Extension Private Use Area
                (codePoint >= 0x2700 && codePoint <= 0x27BF); // Misc Symbols (Dingbats)
    }

    private static String stripIcons(String input) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < input.length(); ) {
            int codePoint = input.codePointAt(i);
            if (!isIconCodePoint(codePoint)) {
                clean.appendCodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }

        return clean.toString().trim();
    }
}