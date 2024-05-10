package dev.stashy.soundcategories.gui;

import dev.stashy.soundcategories.SoundCategories;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoundList extends ElementListWidget<SoundList.SoundEntry>
{

    public SoundList(MinecraftClient minecraftClient, int i, int j, int k, int l, int m)
    {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public int addCategory(SoundCategory cat)
    {
        var option = this.client.options.getSoundVolumeOption(cat);
        return this.addEntry(SoundEntry.create(this.client.options, this.width, option));
    }

    public int addDoubleCategory(SoundCategory first, @Nullable SoundCategory second)
    {
        return super.addEntry(SoundEntry.createDouble(
            this.client.options,
            this.client.options.getSoundVolumeOption(first),
            second != null ? this.client.options.getSoundVolumeOption(second) : null,
            this.width
        ));
    }

    public int addOption(GameOptions o, SimpleOption<?> w)
    {
        return super.addEntry(SoundEntry.createOption(o, w, this.width));
    }

    public int addGroup(SoundCategory group, ButtonWidget.PressAction pressAction)
    {
        var option = this.client.options.getSoundVolumeOption(group);
        return super.addEntry(SoundEntry.createGroup(this.client.options, option, this.width, pressAction));
    }

    @Environment(EnvType.CLIENT)
    protected static class SoundEntry extends ElementListWidget.Entry<SoundList.SoundEntry>
    {
        List<? extends ClickableWidget> widgets;

        public SoundEntry(List<? extends ClickableWidget> w)
        {
            widgets = w;
        }

        public static SoundEntry create(GameOptions options, int width, SimpleOption<?> option)
        {
            return new SoundEntry(List.of(option.createWidget(options, width / 2 - 155, 0, 310)));
        }

        public static SoundEntry createDouble(GameOptions options, SimpleOption<?> first, @Nullable SimpleOption<?> second, int width)
        {
            List<ClickableWidget> w = new ArrayList<>();
            w.add(first.createWidget(options, width / 2 - 155, 0, 150));
            if (second != null)
                w.add(second.createWidget(options, width / 2 + 5, 0, 150));
            return new SoundEntry(w);
        }

        public static SoundEntry createOption(GameOptions o, SimpleOption<?> w, int width)
        {
            var b = w.createWidget(o, width / 2 - 155, 0, 310);
            return new SoundEntry(List.of(b));
        }

        public static SoundEntry createGroup(GameOptions options, SimpleOption<?> option, int width, ButtonWidget.PressAction pressAction)
        {
            return new SoundEntry(
                    List.of(
                            option.createWidget(options, width / 2 - 155, 0, 285),
                            new TexturedButtonWidget(width / 2 + 135, 0, 20, 20, 0, 0, 20,
                                                     SoundCategories.SETTINGS_ICON, 20, 40, pressAction)
                    ));
        }

        public List<? extends Element> children()
        {
            return this.widgets;
        }

        public List<? extends Selectable> selectableChildren()
        {
            return this.widgets;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta)
        {
            this.widgets.forEach((s) -> {
                s.setY(y);
                s.render(matrices, mouseX, mouseY, tickDelta);
            });
        }
    }
}
