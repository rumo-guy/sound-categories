package dev.stashy.soundcategories.v1_19;

import dev.stashy.soundcategories.SCUtil;
import dev.stashy.soundcategories.SoundCategories;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.List;

public class SCOptionsScreen extends GameOptionsScreen {
    private SCSoundList list;

    public SCOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("options.sounds.title"));
    }

    public void init() {
        this.list = new SCSoundList(client, width, height);
        list.addOptions(gameOptions.getSoundDevice());
        list.addOptions(gameOptions.getShowSubtitles(), gameOptions.getDirectionalAudio());
        list.addCategories(SoundCategory.MASTER);

        addPairedElements(list, SCUtil.pairElements(SoundCategories.getUnparentedCategories()));
        SoundCategories.getParentCategories().forEach(
                it -> list.addGroup(it, w -> openCategoryScreen(it))
        );

        addSelectableChild(list);
        addDrawableChild(OptionsUtil.getDoneButton(client, parent, width, height));
    }

    private void openCategoryScreen(SoundCategory cat) {
        if (client == null) return;
        client.setScreen(new SCGroupScreen(this, gameOptions, cat));
    }

    private void addPairedElements(SCSoundList list, List<Pair<SoundCategory, SoundCategory>> pairs) {
        pairs.forEach(it -> {
            if (it.getRight() != null)
                list.addCategories(it.getLeft(), it.getRight());
            else
                list.addCategories(it.getLeft());
        });
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        var children = list.children().stream().flatMap(it -> it.children().stream());
        children.forEach(it -> {
            if (it instanceof OrderableTooltip && ((Element) it).isMouseOver(mouseX, mouseY)) {
                renderOrderedTooltip(matrices, ((OrderableTooltip) it).getOrderedTooltip(), mouseX, mouseY);
            }
        });
    }
}
