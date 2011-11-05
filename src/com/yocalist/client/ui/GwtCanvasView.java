/*
 * Copyright 2011 Chad Retz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.yocalist.client.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.HasText;
import com.yocalist.client.BinaryStringByteReader;
import com.yocalist.shared.Color;
import com.yocalist.shared.YocalistRuntimeException;
import com.yocalist.shared.cdg.CdgReader;
import com.yocalist.shared.cdg.chunk.BorderPreset;
import com.yocalist.shared.cdg.chunk.CdgChunk;
import com.yocalist.shared.cdg.chunk.LoadColorTable;
import com.yocalist.shared.cdg.chunk.MemoryPreset;
import com.yocalist.shared.cdg.chunk.TileBlock;
import com.yocalist.shared.ui.CanvasView;

public class GwtCanvasView implements CanvasView {

    private final Context2d context;
    private final List<List<ImageDataUpdate>> updates = 
            new ArrayList<List<ImageDataUpdate>>();
    private String data;
    private final Color[] colors = new Color[16];
    private final int[] colorIndexes = new int[FRAME_WIDTH * FRAME_HEIGHT];
    private int dataIndex = 0;
    private final HasText progress;
    private boolean loadComplete = false;
    
    public GwtCanvasView(CanvasElement element, String data, HasText progress) {
        context = element.getContext2d();
        this.data = data;
        this.progress = progress;
    }
    
    public boolean isLoadComplete() {
        return loadComplete;
    }
    
    @Override
    public void init() {
        try {
            new CdgReader(new BinaryStringByteReader(data)) {
                
                @Override
                public void read() throws IOException {
                    Scheduler.get().scheduleIncremental(new RepeatingCommand() {
                        @Override
                        public boolean execute() {
                            try {
                                if (next()) {
                                    return true;
                                } else {
                                    loadComplete = true;
                                    return false;
                                }
                            } catch (IOException e) {
                                throw new YocalistRuntimeException(e);
                            }
                        }
                    });
                }
                
                @Override
                protected void handleChunk(CdgChunk chunk, int index, int length) {
                    progress.setText("Loaded " + index + " of " + length);
                    if (chunk instanceof LoadColorTable) {
                        setColorTable(((LoadColorTable) chunk).getColors(), 
                                ((LoadColorTable) chunk).isLow());
                    } else if (chunk instanceof MemoryPreset) {
                        if (((MemoryPreset) chunk).getRepeat() == 0) {
                            clear(((MemoryPreset) chunk).getColorIndex());
                        }
                    } else if (chunk instanceof BorderPreset) {
                        setBorder(((BorderPreset) chunk).getColorIndex());
                    } else if (chunk instanceof TileBlock) {
                        setTile((TileBlock) chunk);
                    }
                }
            }.read();
        } catch (IOException e) {
            throw new YocalistRuntimeException(e);
        }
    }
    
    private ImageDataUpdate setTileColor(int tileRow, int tileColumn, int colorIndex) {
        ImageData image = context.createImageData(TILE_WIDTH, TILE_HEIGHT);
        CanvasPixelArray imageData = image.getData();
        int dataIndex = 0;
        int colorValueIndex = (TILE_HEIGHT * tileRow * FRAME_WIDTH) + (TILE_WIDTH * tileColumn);
        for (int i = 0; i < TILE_HEIGHT; i++) {
            for (int j = 0; j < TILE_WIDTH; j++) {
                imageData.set(dataIndex * 4, colors[colorIndex].getRed());
                imageData.set(dataIndex * 4 + 1, colors[colorIndex].getGreen());
                imageData.set(dataIndex * 4 + 2, colors[colorIndex].getBlue());
                imageData.set(dataIndex * 4 + 3, 255);
                colorIndexes[colorValueIndex] = colorIndex;
                dataIndex++;
                colorValueIndex++;
            }
            colorValueIndex += FRAME_WIDTH - TILE_WIDTH;
        }
        return new ImageDataUpdate(TILE_WIDTH * tileColumn, TILE_HEIGHT * tileRow, image);
    }
    
    private void setColorTable(Color[] colors, boolean low) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] != null) {
                this.colors[low ? i : i + 8] = colors[i];
            }
        }
        refreshColorTable();
    }

    private void refreshColorTable() {
        ImageData image = context.createImageData(FRAME_WIDTH, FRAME_HEIGHT);
        CanvasPixelArray imageData = image.getData();
        for (int i = 0; i < FRAME_WIDTH * FRAME_HEIGHT; i++) {
            imageData.set(i * 4, colors[colorIndexes[i]].getRed());
            imageData.set(i * 4 + 1, colors[colorIndexes[i]].getGreen());
            imageData.set(i * 4 + 2, colors[colorIndexes[i]].getBlue());
            imageData.set(i * 4 + 3, 255);
        }
        updates.add(Collections.singletonList(new ImageDataUpdate(0, 0, image)));
    }
    
    private void setTile(TileBlock tile) {
        ImageData image = context.createImageData(TILE_WIDTH, TILE_HEIGHT);
        CanvasPixelArray imageData = image.getData();
        int x = tile.getColumn() * TILE_WIDTH;
        int y = tile.getRow() * TILE_HEIGHT;
        int dataIndex = 0;
        int colorValueIndex = y * FRAME_WIDTH + x;
        int pix, colorIndex;
        for (int i = 0; i < tile.getPixels().length; i++) {
            for (int j = 0; j < TILE_WIDTH; j++) {
                pix = (tile.getPixels()[i] >>> (5 - j)) & 0x01;
                colorIndex = pix == 0 ? tile.getColorIndexOff() : tile.getColorIndexOn();
                if (!tile.isNormal()) {
                    colorIndex ^= colorIndexes[colorIndex];
                }
                imageData.set(dataIndex * 4, colors[colorIndex].getRed());
                imageData.set(dataIndex * 4 + 1, colors[colorIndex].getGreen());
                imageData.set(dataIndex * 4 + 2, colors[colorIndex].getBlue());
                imageData.set(dataIndex * 4 + 3, 255);
                colorIndexes[colorValueIndex] = colorIndex;
                dataIndex++;
                colorValueIndex++;
            }
            colorValueIndex += FRAME_WIDTH - TILE_WIDTH;
        }
        updates.add(Collections.singletonList(new ImageDataUpdate(x, y, image)));
    }
    
    private void clear(int colorIndex) {
        List<ImageDataUpdate> update = new ArrayList<ImageDataUpdate>();
        for (int y = 1; y < TILES_PER_COL - 1; y++) {
            for (int x = 1; x < TILES_PER_ROW - 1; x++) {
                update.add(setTileColor(y, x, colorIndex));
            }
        }
        updates.add(update);
    }
    
    private void setBorder(int colorIndex) {
        List<ImageDataUpdate> update = new ArrayList<ImageDataUpdate>();
        for (int i = 0; i < TILES_PER_ROW; i++) {
            update.add(setTileColor(0, i, colorIndex));
            update.add(setTileColor(TILES_PER_COL - 1, i, colorIndex));
        }
        for (int i = 0; i < TILES_PER_COL; i++) {
            update.add(setTileColor(i, 0, colorIndex));
            update.add(setTileColor(i, TILES_PER_ROW - 1, colorIndex));
        }
        updates.add(update);
    }

    @Override
    public boolean tick() {
        for (ImageDataUpdate update : updates.get(dataIndex)) {
            context.putImageData(update.imageData, update.x, update.y);
        }
        return ++dataIndex < updates.size();
    }

    private static class ImageDataUpdate {
        private final int x;
        private final int y;
        private final ImageData imageData;
        
        private ImageDataUpdate(int x, int y, ImageData imageData) {
            this.x = x;
            this.y = y;
            this.imageData = imageData;
        }
    }
    
    
}
