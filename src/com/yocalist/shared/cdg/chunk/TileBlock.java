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
package com.yocalist.shared.cdg.chunk;

import java.io.IOException;

import com.yocalist.shared.ByteReader;

/**
 * Tile block chunk
 * 
 * @author Chad Retz
 */
public class TileBlock extends CdgChunk {

    private boolean normal;
    private int colorIndexOff;
    private int colorIndexOn;
    private int row;
    private int column;
    private byte[] pixels;
    
    public TileBlock() {
    }
    
    public TileBlock(ByteReader reader, boolean normal) throws IOException {
        this.normal = normal;
        colorIndexOff = reader.read() & 0x0f;
        colorIndexOn = reader.read() & 0x0f;
        row = reader.read() & 0x1f;
        column = reader.read() & 0x3f;
        pixels = new byte[12];
        reader.readAndMask(pixels, 0x3f);
    }
    
    public boolean isNormal() {
        return normal;
    }
    
    public void setNormal(boolean normal) {
        this.normal = normal;
    }
    
    public int getColorIndexOff() {
        return colorIndexOff;
    }
    
    public void setColorIndexOff(int colorIndexOff) {
        this.colorIndexOff = colorIndexOff;
    }
    
    public int getColorIndexOn() {
        return colorIndexOn;
    }
    
    public void setColorIndexOn(int colorIndexOn) {
        this.colorIndexOn = colorIndexOn;
    }
    
    public int getRow() {
        return row;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public int getColumn() {
        return column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }

    public byte[] getPixels() {
        return pixels;
    }
    
    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }
}
