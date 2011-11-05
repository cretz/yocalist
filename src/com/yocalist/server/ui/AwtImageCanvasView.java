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
package com.yocalist.server.ui;

import java.awt.image.MemoryImageSource;
import java.io.IOException;

import com.yocalist.server.CdgFileReader;
import com.yocalist.shared.Color;
import com.yocalist.shared.YocalistRuntimeException;
import com.yocalist.shared.cdg.chunk.BorderPreset;
import com.yocalist.shared.cdg.chunk.CdgChunk;
import com.yocalist.shared.cdg.chunk.LoadColorTable;
import com.yocalist.shared.cdg.chunk.MemoryPreset;
import com.yocalist.shared.cdg.chunk.TileBlock;
import com.yocalist.shared.ui.CanvasView;

/**
 * {@link CanvasView} for a {@link MemoryImageSource}. Much of the logic
 * here came from knowledge gained reading 
 * <a href="https://github.com/martinb3/java_karaoke/blob/master/src/org/mbs3/jkaraoke/Display.java">this</a>
 * code.
 * 
 * @author Chad Retz
 */
public class AwtImageCanvasView implements CanvasView {
    
    public static final int FULL_WIDTH = 300;
    public static final int FULL_HEIGHT = 260;
    private static final int TILES_PER_ROW = FULL_WIDTH / TILE_WIDTH;
    private static final int TILES_PER_COL = FULL_HEIGHT / TILE_HEIGHT;
    
    private final MemoryImageSource source;
    private final int[] pixels = new int[FULL_WIDTH * FULL_HEIGHT];
    private final int[] colorValues = new int [FULL_WIDTH * FULL_HEIGHT];
    private final int[] colorTable = new int[16];
    private final CdgFileReader reader;
    private int currentIndex = -1;
    
    public AwtImageCanvasView(CdgFileReader reader) {
        source = new MemoryImageSource(FULL_WIDTH, FULL_HEIGHT, 
                pixels, 0, FULL_WIDTH);
        source.setAnimated(true);
        this.reader = reader;
    }
    
    @Override
    public void init() {
        try {
            reader.read();
            currentIndex = -1;
        } catch (IOException e) {
            throw new YocalistRuntimeException(e);
        }
    }
    
    @Override
    public boolean tick() {
        if (currentIndex >= reader.getChunks().size() - 1) {
            return false;
        }
        CdgChunk chunk = reader.getChunks().get(++currentIndex);
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
        return true;
    }
    
    private void setColorTable(Color[] colors, boolean low) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] != null) {
                colorTable[low ? i : i + 8] = (255 << 24) |
                        (colors[i].getRed() << 16) |
                        (colors[i].getGreen() << 8) |
                        colors[i].getBlue();
            }
        }
        //refresh
        for(int i = 0; i < pixels.length; i++) {
            setPixelColor(i, colorValues[i]);
        }
        source.newPixels(0, 0, FULL_WIDTH, FULL_HEIGHT);
    }

    private void setTile(TileBlock block) {
        int row = TILE_HEIGHT * (block.getRow() + 1);
        int column = TILE_WIDTH * (block.getColumn() + 1);
        int index = row * FULL_WIDTH + column;
        for(int i = 0; i < block.getPixels().length; i++) {
            int[] eachPixel = new int[6];
            for (int j = 0; j < eachPixel.length; j++) {
                eachPixel[j] = (block.getPixels()[i] >>> (5-j)) & 0x01;
            }
            for(int j = 0; j < 6; j++) {
                int colorIndex = eachPixel[j] == 0 ? 
                        block.getColorIndexOff() : block.getColorIndexOn();
                if (!block.isNormal()) {
                    colorIndex ^= colorValues[index];
                }
                setPixelColor(index, colorIndex);
                index++;
            }
            index += FULL_WIDTH - TILE_WIDTH;    
        }
        source.newPixels(column, row, TILE_WIDTH, TILE_HEIGHT);
    }

    private void clear(int colorIndex) {
        for(int y = 1; y < TILES_PER_COL - 1; y++) {
            for(int x = 1; x < TILES_PER_ROW - 1; x++) {
                setTileColor(y, x, colorIndex);
            }
        }
    }

    private void setBorder(int colorIndex) {
        //top
        for(int x = 0; x < TILES_PER_ROW; x++) {
            setTileColor(0, x, colorIndex);
        }
        //bottom
        for(int x = 0; x < TILES_PER_ROW; x++) {
            setTileColor(TILES_PER_COL - 1, x, colorIndex);
        }
        //left bar
        for(int y = 0; y < TILES_PER_COL; y++) {
            setTileColor(y, 0, colorIndex);
        }
        //right bar
        for(int y = 0; y < TILES_PER_COL; y++)  {
            setTileColor(y, TILES_PER_ROW - 1, colorIndex);
        }
    }
    
    private void setTileColor(int tileRow, int tileColumn, int colorIndex) {
        int row = TILE_HEIGHT * tileRow;
        int column = TILE_WIDTH * tileColumn;
        int currentIndex = (row * FULL_WIDTH) + column;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                setPixelColor(currentIndex, colorIndex);
                currentIndex++;
            }
            currentIndex += (FULL_WIDTH - TILE_WIDTH);
        }
        source.newPixels(column, row, TILE_WIDTH, TILE_HEIGHT);
    }

    private void setPixelColor(int pixel, int colorIndex) {
        pixels[pixel] = colorTable[colorIndex];
        colorValues[pixel] = colorIndex;
    }
    
    public MemoryImageSource getSource() {
        return source;
    }
}
