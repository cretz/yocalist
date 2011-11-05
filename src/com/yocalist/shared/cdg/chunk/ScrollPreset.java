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
 * Scroll preset chunk
 * 
 * @author Chad Retz
 */
public class ScrollPreset extends CdgChunk {

    private byte color;
    private byte horizontalScroll;
    private byte verticalScroll;
    
    public ScrollPreset(ByteReader reader) throws IOException {
        color = (byte) (reader.read() & 0x0f);
        horizontalScroll = (byte) (reader.read() & 0x3f);
        verticalScroll = (byte) (reader.read() & 0x3f);
        reader.skip(13);
    }
    
    public byte getColor() {
        return color;
    }
    
    public void setColor(byte color) {
        this.color = color;
    }

    public byte getHorizontalScroll() {
        return horizontalScroll;
    }

    public void setHorizontalScroll(byte horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
    }

    public byte getVerticalScroll() {
        return verticalScroll;
    }

    public void setVerticalScroll(byte verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

}
