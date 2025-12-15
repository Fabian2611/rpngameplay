package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.client.widget.ColorButton;
import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.*;
//import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.util.StringUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.TakeLecternBookPacket;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static com.github.minecraftschurlimods.bibliocraft.net.Payload.CHANNEL;

public class BigBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/newspaper.png");
    private static final Component OWNER = Component.translatable(Translations.VANILLA_BY_AUTHOR_KEY, Objects.requireNonNull(Minecraft.getInstance().player).getName()).withStyle(ChatFormatting.DARK_GRAY);
    private static final int BACKGROUND_WIDTH = 220;
    private static final int BACKGROUND_HEIGHT = 256;
    private static final int TEXT_WIDTH = 188;
    private static final int TEXT_HEIGHT = 204;
    private final ItemStack stack;
    private final Player player;
    private final InteractionHand hand;
    private final BlockPos lectern;
    private final boolean writable;
    private final List<List<FormattedLine>> pages;
    private int currentPage;
    private boolean isSigning = false;
    private boolean isUpdatingUI = false;
    private FormattedTextArea textArea;
    private Button modeButton;
    private Button alignmentButton;
    private EditBox colorBox;
    private EditBox sizeBox;
    private Button scaleDownButton;
    private Button scaleUpButton;
    private PageButton backButton;
    private PageButton forwardButton;
    private Button finalizeButton;
    private EditBox titleBox;

    public BigBookScreen(ItemStack stack, Player player, InteractionHand hand) {
        this(stack, player, hand, null);
    }

    public BigBookScreen(ItemStack stack, Player player, BlockPos lectern) {
        this(stack, player, null, lectern);
    }

    private BigBookScreen(ItemStack stack, Player player, InteractionHand hand, BlockPos lectern) {
        super(stack.getHoverName());
        this.stack = stack;
        this.player = player;
        this.hand = hand;
        this.lectern = lectern;
        WrittenBigBookContent content;
        BookStorage storage =  BookStorage.get();
        UUID id;
        if ((id = BookStorage.getUUIDFromItem(stack, false)) != null && storage.containsUuid(id) && stack.hasTag() && stack.getTag().hasUUID("uuid")) {
            BigBookInfo info = BigBookInfo.decode(stack.getTag());
            if (info != null) {
                pages = new ArrayList<>(WrittenBigBookContent.decode(storage.getOrCreateBookContents(id)).pages());
                currentPage = Math.max(0, Math.min(info.currentPage(), pages.size() - 1));
                writable = !stack.is(BCItems.WRITTEN_BIG_BOOK.get()) && lectern == null;
            }
            else
            {
                pages = new ArrayList<>();
                currentPage = 0;
                writable = lectern == null;
            }
        }
        else
        {
            pages = new ArrayList<>();
            currentPage = 0;
            writable = lectern == null;
        }

//        if (stack.hasTag() && WrittenBigBookContent.decode(stack.getTag()) != null) {
//            WrittenBigBookContent content = WrittenBigBookContent.decode(stack.getTag()) ;
//            pages = new ArrayList<>(content.pages());
//            currentPage = content.currentPage();
//            writable = false;
//        } else if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
//            BigBookContent content = Objects.requireNonNull(stack.get(BCDataComponents.BIG_BOOK_CONTENT));
//            pages = new ArrayList<>(content.pages());
//            currentPage = content.currentPage();
//            writable = lectern == null;
//        } else {
//            pages = new ArrayList<>();
//            currentPage = 0;
//            writable = lectern == null;
//        }
        if (pages.isEmpty()) {
            addPage();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void init() {
        int leftX = (width - BACKGROUND_WIDTH - 80) / 2;
        int rightX = (width + BACKGROUND_WIDTH - 80) / 2;
        if (isSigning) {
            titleBox = addRenderableWidget(new EditBox(font, (width - 80) / 2, 50, TEXT_WIDTH, 20, Component.empty()));
            titleBox.setTextColor(0);
            titleBox.setBordered(false);
            titleBox.setResponder(s -> {
                titleBox.setX((width - 80 - font.width(s)) / 2);
                finalizeButton.active = !StringUtil.isBlank(s);
            });
            setFocused(titleBox);
            finalizeButton = addRenderableWidget(Button.builder(Translations.VANILLA_FINALIZE_BUTTON, $ -> finalizeBook())
                    .bounds(rightX + 16, BACKGROUND_HEIGHT - 48, 64, 16)
                    .build());
            finalizeButton.active = false;
            addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, $ -> {
                isSigning = false;
                rebuildWidgets();
            })
                    .bounds(rightX + 16, BACKGROUND_HEIGHT - 32, 64, 16)
                    .build());
        } else if (writable) {
            updateTextArea();

            // Page buttons
            backButton = addRenderableWidget(new PageButton(leftX + 10, BACKGROUND_HEIGHT - 32, false, $ -> {
                pages.set(currentPage, textArea.getLines());
                if (currentPage > 0) {
                    currentPage--;
                }
                updateButtonVisibility();
                updateTextArea();
            }, true));
            forwardButton = addRenderableWidget(new PageButton(leftX + 180, BACKGROUND_HEIGHT - 32, true, $ -> {
                pages.set(currentPage, textArea.getLines());
                if (currentPage < 255) {
                    currentPage++;
                    while (currentPage >= pages.size()) {
                        addPage();
                    }
                }
                updateButtonVisibility();
                updateTextArea();
            }, true));
            addRenderableWidget(Button.builder(Component.literal("+"), $ -> {
                if (pages.size() < 256) {
                    if (currentPage >= 0 && currentPage < pages.size()) {
                        pages.set(currentPage, textArea.getLines());
                    }
                    int index = Math.max(0, Math.min(currentPage, pages.size()));
                    if (index < 0) index = 0;
                    if (index > pages.size()) index = pages.size();
                    pages.add(index, new ArrayList<>(List.of(FormattedLine.DEFAULT)));
                    updateButtonVisibility();
                    updateTextArea();
                }
            }).bounds(leftX + 95, BACKGROUND_HEIGHT - 32, 20, 20).build());
            updateButtonVisibility();

            // Formatting buttons
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_BOLD_SHORT, $ -> {
                textArea.toggleStyle(Style::isBold, Style::withBold);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_BOLD))
                    .bounds(rightX + 16, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_ITALIC_SHORT, $ -> {
                textArea.toggleStyle(Style::isItalic, Style::withItalic);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ITALIC))
                    .bounds(rightX + 32, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_UNDERLINED_SHORT, $ -> {
                textArea.toggleStyle(Style::isUnderlined, Style::withUnderlined);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_UNDERLINED))
                    .bounds(rightX + 48, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_SHORT, $ -> {
                textArea.toggleStyle(Style::isStrikethrough, Style::withStrikethrough);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_STRIKETHROUGH))
                    .bounds(rightX + 64, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_OBFUSCATED_SHORT, $ -> {
                textArea.toggleStyle(Style::isObfuscated, Style::withObfuscated);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_OBFUSCATED))
                    .bounds(rightX + 16, 32, 16, 16)
                    .build());
            modeButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getMode().getTranslationKey()), $ -> {
                        textArea.toggleMode();
                        updateModeButton();
                        setFocused(textArea);
                    })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_MODE))
                    .bounds(rightX + 32, 32, 48, 16)
                    .build());
            alignmentButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getAlignment().getTranslationKey()), $ -> {
                        textArea.toggleAlignment();
                        updateAlignmentButton();
                        setFocused(textArea);
                    })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ALIGNMENT))
                    .bounds(rightX + 16, 48, 64, 16)
                    .build());

            // Color buttons and text box
            ChatFormatting[] colors = BCUtil.getChatFormattingColors().toArray(ChatFormatting[]::new);
            int colorRows = Math.floorDiv(colors.length, 4);
            colorBox = new EditBox(font, rightX + 16, 80 + 16 * colorRows, 64, 16, Component.empty());
            colorBox.setHint(Translations.FANCY_TEXT_AREA_COLOR_HINT);
            colorBox.setMaxLength(7);
            colorBox.setFilter(s -> s.isEmpty() || s.charAt(0) == '#' && s.substring(1).codePoints().allMatch(HexFormat::isHexDigit));
            colorBox.setResponder(s -> {
                if (isUpdatingUI) return;
                if (s.length() <= 1) return;
                textArea.setColor(Integer.parseInt(s.substring(1), 16));
            });
            TextColor color = textArea.getStyleAtCursor().getColor();
            if (color != null) {
                setColor(color.getValue());
            }
            for (int i = 0; i < colors.length; i++) {
                final int j = i; // I love Java
                ColorButton button = addRenderableWidget(new ColorButton(colors[i].getColor(), Button.builder(Component.translatable("color." + colors[i].getName()), $ -> {
                    setColor(colors[j].getColor());
                    setFocused(textArea);
                })
                        .bounds(rightX + 80 - 16 * (4 - i % 4), 80 + 16 * Math.floorDiv(i, 4), 16, 16)));
                button.setTooltip(Tooltip.create(Component.translatable("color." + colors[i].getName())));
            }
            addRenderableWidget(colorBox);

            // Size buttons and text box
            sizeBox = new EditBox(font, rightX + 32, 112 + 16 * colorRows, 32, 16, Component.empty());
            sizeBox.setFilter(s -> {
                try {
                    int i = Integer.parseInt(s);
                    return i >= FormattedLine.MIN_SIZE && i <= FormattedLine.MAX_SIZE;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
            sizeBox.setResponder(s -> {
                if (isUpdatingUI) return;
                try {
                    textArea.setSize(Integer.parseInt(s));
                } catch (NumberFormatException ignored) {}
            });
            scaleDownButton = addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_SCALE_DOWN, $ -> {
                int size = textArea.getSize() - 1;
                sizeBox.setValue(String.valueOf(size));
                // call again to account for invalid values
                sizeBox.setValue(String.valueOf(textArea.getSize()));
                updateSizeButtons(size);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP))
                    .bounds(rightX + 16, 112 + 16 * colorRows, 16, 16)
                    .build());
            addRenderableWidget(sizeBox);
            scaleUpButton = addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_SCALE_UP, $ -> {
                int size = textArea.getSize() + 1;
                sizeBox.setValue(String.valueOf(size));
                // call again to account for invalid values
                sizeBox.setValue(String.valueOf(textArea.getSize()));
                updateSizeButtons(size);
                setFocused(textArea);
            })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_UP_TOOLTIP))
                    .bounds(rightX + 64, 112 + 16 * colorRows, 16, 16)
                    .build());

            onLineChange(textArea.getLines().get(0));
            addRenderableWidget(Button.builder(Translations.VANILLA_SIGN_BUTTON, $ -> {
                isSigning = true;
                rebuildWidgets();
            })
                    .bounds(rightX + 16, BACKGROUND_HEIGHT - 48, 64, 16)
                    .build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, this::done)
                    .bounds(rightX + 16, BACKGROUND_HEIGHT - 32, 64, 16)
                    .build());
        } else {
            backButton = addRenderableWidget(new PageButton(leftX + 50, BACKGROUND_HEIGHT - 32, false, $ -> {
                if (currentPage > 0) {
                    currentPage--;
                }
                updateButtonVisibility();
                if (lectern != null) {
                    CHANNEL.sendToServer(new SetBigBookPageInLecternPacket(currentPage, Either.right(lectern)));
                }
            }, true));
            forwardButton = addRenderableWidget(new PageButton(leftX + 220, BACKGROUND_HEIGHT - 32, true, $ -> {
                if (currentPage < pages.size()) {
                    currentPage++;
                }
                updateButtonVisibility();
                if (lectern != null) {
                    CHANNEL.sendToServer(new SetBigBookPageInLecternPacket(currentPage, Either.right(lectern)));
//                    PacketDistributor.sendToServer(new SetBigBookPageInLecternPacket(currentPage, Either.right(lectern)));
                }
            }, true));
            updateButtonVisibility();
            if (lectern != null) {
                addRenderableWidget(Button.builder(Translations.VANILLA_TAKE_BOOK, button -> {
                    done(button);
                    LecternUtil.takeLecternBook(player, player.level(), lectern);
                        CHANNEL.sendToServer(new TakeLecternBookPacket(lectern));
                })
                        .bounds((width - BACKGROUND_WIDTH) / 2, BACKGROUND_HEIGHT + 4, BACKGROUND_WIDTH / 2 - 4, 20)
                        .build());
                addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, this::done)
                        .bounds(width / 2 + 2, BACKGROUND_HEIGHT + 4, BACKGROUND_WIDTH / 2 - 4, 20)
                        .build());
                } else {
                    addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, this::done)
                        .bounds((width - BACKGROUND_WIDTH) / 2, BACKGROUND_HEIGHT + 4, BACKGROUND_WIDTH, 20)
                        .build());
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return lectern == null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        if (isSigning) {
            int x = (width - BACKGROUND_WIDTH - 80) / 2;
            graphics.drawString(font, Translations.VANILLA_EDIT_TITLE, x + 18 + (TEXT_WIDTH - font.width(Translations.VANILLA_EDIT_TITLE)) / 2, 34, 0, false);
            graphics.drawString(font, OWNER, x + 18 + (TEXT_WIDTH - font.width(OWNER)) / 2, 60, 0, false);
            graphics.drawWordWrap(font, Translations.VANILLA_FINALIZE_WARNING, x + 18, 82, TEXT_WIDTH, 0);
        } else {
            Component pageIndicator = Component.translatable(Translations.VANILLA_PAGE_INDICATOR_KEY, currentPage + 1, pages.size());
            if (writable) {
                graphics.drawString(font, pageIndicator, (width - BACKGROUND_WIDTH - 80) / 2 + 16 + TEXT_WIDTH - font.width(pageIndicator), 18, 0, false);
            } else {
                int x = (width - BACKGROUND_WIDTH) / 2 + 16;
                FormattedTextArea.renderLines(pages.get(currentPage), graphics.pose(), graphics.bufferSource(), x, 26, TEXT_WIDTH, TEXT_HEIGHT);
                graphics.drawString(font, pageIndicator, x + TEXT_WIDTH - font.width(pageIndicator), 18, 0, false);
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isSigning) {
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                finalizeBook();
                return true;
            }
            setFocused(null);
            setFocused(titleBox);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(GuiGraphics graphics){
        super.renderBackground(graphics);
        int x = (width - (writable ? BACKGROUND_WIDTH + 80 : BACKGROUND_WIDTH)) / 2;
        graphics.blit(BACKGROUND, x, 0, 0, 0, 256, 256);
    }

    @Override
    public void onClose() {
        super.onClose();
        if (writable) {
            savePages();
            BookStorage storage = BookStorage.get();
            UUID id = BookStorage.getUUIDFromItem(stack, true);
            WrittenBigBookContent content = new WrittenBigBookContent(pages);
            storage.setBookContents(id, content.encode());

            BigBookInfo info = new BigBookInfo(currentPage,id);
            stack.setTag(info.encode());
            CompoundTag extraData = new CompoundTag();
            extraData.put("info", info.encode());
            extraData.putInt("hand", hand.ordinal());
            BigBookPacketHandler.sendMultipart(content, extraData, 1);
        } else if (hand != null) {
            CHANNEL.sendToServer(new SetBigBookPageInLecternPacket(currentPage, Either.left(hand)));
        } else if (lectern != null) {
            CHANNEL.sendToServer(new SetBigBookPageInLecternPacket(currentPage, Either.right(lectern)));
        }
    }

    private void onLineChange(FormattedLine line) {
        isUpdatingUI = true;
        try {
            if (modeButton != null) updateModeButton();
            if (alignmentButton != null) updateAlignmentButton();
            TextColor color = textArea.getStyleAtCursor().getColor();
            int colorVal = color == null ? 0 : color.getValue();
            if (colorBox != null) {
                String hexString = Integer.toHexString(colorVal);
                colorBox.setValue("#" + "0".repeat(6 - hexString.length()) + hexString);
            }
            if (sizeBox != null) sizeBox.setValue(String.valueOf(line.size()));
            if (scaleDownButton != null && scaleUpButton != null) updateSizeButtons(line.size());
        } finally {
            isUpdatingUI = false;
        }
    }

    private void updateModeButton() {
        modeButton.setMessage(Component.translatable(textArea.getMode().getTranslationKey()));
    }

    private void updateAlignmentButton() {
        alignmentButton.setMessage(Component.translatable(textArea.getAlignment().getTranslationKey()));
    }

    private void updateSizeButtons(int size) {
        scaleDownButton.active = size > FormattedLine.MIN_SIZE;
        scaleUpButton.active = size < FormattedLine.MAX_SIZE;
    }

    private void setColor(int color) {
        textArea.setColor(color);
        String hexString = Integer.toHexString(color);
        colorBox.setValue("#" + "0".repeat(6 - hexString.length()) + hexString);
    }

    private void updateButtonVisibility() {
        this.backButton.visible = !isSigning && currentPage > 0;
        this.forwardButton.visible = !isSigning && currentPage < 255 && (writable || currentPage < pages.size() - 1);
    }

    private void updateTextArea() {
        if (textArea != null) {
            removeWidget(textArea);
        }
        textArea = addRenderableWidget(new FormattedTextArea((width - BACKGROUND_WIDTH - 80) / 2 + 16, 26, TEXT_WIDTH, TEXT_HEIGHT, pages.get(currentPage)));
        textArea.setOnLineChange(this::onLineChange);
        setFocused(textArea);
        textArea.selectFirstLine();
    }

    private void addPage() {
        List<FormattedLine> lines = new ArrayList<>();
        lines.add(FormattedLine.DEFAULT);
        pages.add(lines);
    }

    private void savePages() {
        pages.set(currentPage, textArea.getLines());
        for (int i = pages.size() - 1; i >= 0; i--) {
            List<FormattedLine> lines = pages.get(i);
            if (lines.stream().anyMatch(e -> e != FormattedLine.DEFAULT)) break;
            pages.remove(i);
        }
        currentPage = Math.min(Math.max(currentPage, 0), pages.size() - 1);
    }

    private void finalizeBook() {
        savePages();
        ItemStack newStack = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
//        WrittenBigBookContent content = new WrittenBigBookContent(pages, titleBox.getValue(), player.getName().getString(), true, 0, currentPage);
//        stack.set(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, content);
//        stack.setTag(WrittenBigBookContent.encode(content));
        WrittenBigBookContent content = new WrittenBigBookContent(pages);
        BookStorage storage= BookStorage.get();
        UUID id = BookStorage.getUUIDFromItem(stack, true);
        SignedBigBookInfo info = new SignedBigBookInfo(titleBox.getValue(), player.getName().getString(), 0);
        newStack.setTag(info.addTag(stack.getTag()));
        storage.setBookContents(id, content.encode());
        player.setItemInHand(hand, newStack);
        CompoundTag extraData = new CompoundTag();
        extraData.put("info", info.encode());
        extraData.putInt("hand", hand.ordinal());
        BigBookPacketHandler.sendMultipart(content, extraData, 0);
        Objects.requireNonNull(minecraft).setScreen(null);
    }

    private void done(Button button) {
        onClose();
        Objects.requireNonNull(minecraft).setScreen(null);
    }
}
