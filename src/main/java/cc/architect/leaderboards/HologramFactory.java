package cc.architect.leaderboards;

import com.maximde.hologramapi.hologram.RenderMode;
import com.maximde.hologramapi.hologram.TextHologram;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class HologramFactory {
    public static TextHologram cloneHologram(TextHologram in) {
        TextHologram out = new TextHologram(UUID.randomUUID().toString(), RenderMode.VIEWER_LIST);
            out.setText(Component.text().append(in.getTextAsComponent()).build());
            out.setBillboard(in.getBillboard());
            out.setBackgroundColor(in.getBackgroundColor());
            out.setScale(in.getScale());
            out.setInterpolationDurationRotation(in.getInterpolationDurationRotation());
            out.setInterpolationDurationTransformation(in.getInterpolationDurationTransformation());
            out.setMaxLineWidth(in.getMaxLineWidth());
            out.setSeeThroughBlocks(in.isSeeThroughBlocks());
            out.setShadow(in.isShadow());
            out.setTextOpacity(in.getTextOpacity());
            out.setTranslation(in.getTranslation());
            out.setViewRange(Double.MAX_VALUE);
        return out;
    }
}
