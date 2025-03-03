package org.mtr.mod.render.pids;

import org.apache.logging.log4j.Logger;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockArrivalProjectorBase;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.data.ArrivalsCacheClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.RenderModularPIDS;

public class LegacyModule extends PIDSModule {
    public final String type = "legacy";
    private static final Logger LOGGER = Init.LOGGER;

    protected int layer = 0;
    protected String colorMode = "basic";
    protected int color = 0xFFFFFF;
    protected float textPadding = 0.1f;
    protected int maxArrivals = 3;

    public LegacyModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackString("colorMode", (value) -> colorMode = value);
        data.unpackInt("color", (value) -> color = value);
        data.unpackInt("layer", (value) -> this.layer = value);
        data.unpackDouble("textPadding", (value) -> textPadding = (float) value);
        data.unpackInt("arrivals", (value) -> maxArrivals = value);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderModularPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing) {
        final float scale = 160 * maxArrivals / height * textPadding;
        final boolean hasDifferentCarLengths = hasDifferentCarLengths(arrivals);
        int arrivalIndex = 0;

        for (int i = 0; i < maxArrivals; i++) {
            final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / RenderModularPIDS.SWITCH_TEXT_TICKS;
            final ArrivalResponse arrivalResponse;
            final String customMessage = entity.getMessage(i);
            final String[] destinationSplit;
            final String[] customMessageSplit = customMessage.split("\\|");
            final boolean renderCustomMessage;
            final int languageIndex;

            if (entity.getHideArrival(i)) {
                if (customMessage.isEmpty()) {
                    continue;
                }
                arrivalResponse = null;
                destinationSplit = new String[0];
                renderCustomMessage = true;
                languageIndex = languageTicks % customMessageSplit.length;
            } else {
                arrivalResponse = Utilities.getElement(arrivals, arrivalIndex);
                if (arrivalResponse == null) {
                    if (customMessage.isEmpty() || customMessageSplit.length == 0) {
                        continue;
                    }
                    destinationSplit = new String[0];
                    renderCustomMessage = true;
                    languageIndex = languageTicks % customMessageSplit.length;
                } else {
                    destinationSplit = arrivalResponse.getDestination().split("\\|");
                    final int messageCount = destinationSplit.length + (customMessage.isEmpty() ? 0 : customMessageSplit.length);
                    renderCustomMessage = languageTicks % messageCount >= destinationSplit.length;
                    languageIndex = (languageTicks % messageCount) - (renderCustomMessage ? destinationSplit.length : 0);
                    if (!entity.alternateLines() || i % 2 == 1) {
                        arrivalIndex++;
                    }
                }
            }

            //graphicsHolder.translate((startX - 8) / 16, -startY / 16 + i * maxHeight / maxArrivals / 16, (startZ - 8) / 16 - SMALL_OFFSET * 2);

            float layerY = y + i * (height - textPadding * 2) / maxArrivals + textPadding * 2;
            float textSize = (height - textPadding * 2) / maxArrivals - textPadding * 2;
            float textScale = textSize / 1.5f;

            if (renderCustomMessage) {
                RenderModularPIDS.renderText(graphicsHolder, customMessageSplit[languageIndex], x + textPadding * 2, layerY, textSize, entity.textColor(), width - textPadding * 4, IGui.HorizontalAlignment.LEFT, layer);
            } else {
                final long arrival = (arrivalResponse.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000;
                final int color = arrival <= 0 ? entity.textColorArrived() : entity.textColor();
                final String destination = destinationSplit[languageIndex];
                final boolean isCjk = IGui.isCjk(destination);
                final String destinationFormatted;

                switch (arrivalResponse.getCircularState()) {
                    case CLOCKWISE:
                        destinationFormatted = (isCjk ? TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_CLOCKWISE_VIA).getString(destination);
                        break;
                    case ANTICLOCKWISE:
                        destinationFormatted = (isCjk ? TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA).getString(destination);
                        break;
                    default:
                        destinationFormatted = destination;
                        break;
                }

                final String carLengthString = (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_CAR_CJK : TranslationProvider.GUI_MTR_ARRIVAL_CAR).getString(arrivalResponse.getCarCount());
                final String arrivalString;

                if (arrival >= 60) {
                    arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_MIN_CJK : TranslationProvider.GUI_MTR_ARRIVAL_MIN).getString(arrival / 60);
                } else if (arrival > 0) {
                    arrivalString = (arrivalResponse.getRealtime() ? "" : "*") + (isCjk ? TranslationProvider.GUI_MTR_ARRIVAL_SEC_CJK : TranslationProvider.GUI_MTR_ARRIVAL_SEC).getString(arrival);
                } else {
                    arrivalString = "";
                }

                float xPos = x + textPadding * 2;

                if (entity.alternateLines()) {
                    if (i % 2 == 0) {
                        RenderModularPIDS.renderText(graphicsHolder, destinationFormatted, xPos, layerY, textSize, color, width - textPadding * 4, IGui.HorizontalAlignment.LEFT, layer);
                        //debug box
                        //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, width - textPadding * 4, textSize, 0xFF00FF00, layer - 1);
                    } else {
                        if (hasDifferentCarLengths) {
                            RenderModularPIDS.renderText(graphicsHolder, carLengthString, xPos, layerY, textSize, 0xFFFF0000, 6.4f * textScale, IGui.HorizontalAlignment.LEFT, layer);
                            //debug box
                            //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, 6.4f * textScale, textSize, 0xFFFFFF00, layer - 1);
                            xPos += 6.6f * textScale;
                        }
                        RenderModularPIDS.renderText(graphicsHolder, arrivalString, xPos, layerY, textSize, color, x + width - textPadding * 2 - xPos, IGui.HorizontalAlignment.RIGHT, layer);
                        //debug box
                        //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, x + width - textPadding * 2 - xPos, textSize, 0xFF00FFFF, layer - 1);
                    }
                } else {
                    final boolean showPlatformNumber = entity instanceof BlockArrivalProjectorBase.BlockEntityArrivalProjectorBase;

                    if (entity.showArrivalNumber()) {
                        RenderModularPIDS.renderText(graphicsHolder, String.valueOf(arrivalIndex), xPos, layerY, textSize, color, 1.0f * textScale, IGui.HorizontalAlignment.LEFT, layer);
                        //debug box
                        //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, 1.0f * textScale, textSize, 0xFFFF0000, layer - 1);
                        xPos += 1.3f * textScale;
                    }

                    final float destinationWidth = width - (textPadding * 4) - 7.0f * textScale - (hasDifferentCarLengths || showPlatformNumber ? showPlatformNumber ? 2.6f * textScale : 4.5f * textScale : 0f) - (entity.showArrivalNumber() ? 1.3f * textScale : 0f);
                    RenderModularPIDS.renderText(graphicsHolder, destinationFormatted, xPos, layerY, textSize, color, destinationWidth, IGui.HorizontalAlignment.LEFT, layer);
                    //debug box
                    //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, destinationWidth, textSize, 0xFF00FF00, layer - 1);
                    xPos += destinationWidth + 0.4f * textScale;

                    if (showPlatformNumber) {
                        RenderModularPIDS.renderText(graphicsHolder, arrivalResponse.getPlatformName(), xPos, layerY, textSize, color, 2.2f * textScale, IGui.HorizontalAlignment.RIGHT, layer);
                        //debug box
                        //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, 2.2f * textScale, textSize, 0xFF0000FF, layer - 1);
                        xPos += 2.6f * textScale;
                    } else if (hasDifferentCarLengths) {
                        RenderModularPIDS.renderText(graphicsHolder, carLengthString, xPos, layerY, textSize, 0xFF0000, 4.2f * textScale, IGui.HorizontalAlignment.RIGHT, layer);
                        //debug box
                        //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, 4.2f * textScale, textSize, 0xFFFFFF00, layer - 1);
                        xPos += 4.5f * textScale;
                    }

                    RenderModularPIDS.renderText(graphicsHolder, arrivalString, xPos, layerY, textSize, color, x + width - xPos - textPadding * 2, IGui.HorizontalAlignment.RIGHT, layer);
                    //debug box
                    //renderPIDS.renderRect(entity, blockPos, facing, xPos, layerY, x + width - xPos - textPadding * 2, textSize, 0xFF00FFFF, layer - 1);
                }
            }
        }
    }

    private static boolean hasDifferentCarLengths(ObjectList<ArrivalResponse> arrivalResponseList) {
        int carCount = 0;
        for (final ArrivalResponse arrivalResponse : arrivalResponseList) {
            final int currentCarCount = arrivalResponse.getCarCount();
            if (carCount > 0 && currentCarCount != carCount) {
                return true;
            }
            carCount = currentCarCount;
        }
        return false;
    }

    public int getSize() {
        return maxArrivals;
    }
}
