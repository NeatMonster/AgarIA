package fr.neatmonster.agaria.utils;

import java.awt.Font;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class StringUtils {

    public static Pair<Double, Double> getBounds(final Graphics2D g, final Font font, final String text) {
        return new Pair<Double, Double>() {
            {
                fst = font.getStringBounds(text, g.getFontRenderContext()).getWidth();
                snd = font.createGlyphVector(g.getFontRenderContext(), text).getVisualBounds().getHeight();
            }
        };
    }

    public static String toString(final ByteBuffer buf) {
        String out = "";
        for (int i = 0; i < buf.limit(); ++i) {
            if (!out.isEmpty())
                out += " ";
            final String chr = Integer.toHexString(buf.get(i));
            if (chr.length() == 1)
                out += "0";
            out += chr;
        }
        return out;
    }
}
