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
 * Memory preset chunk
 * 
 * @author Chad Retz
 */
public class MemoryPreset extends CdgChunk {

    private int colorIndex;
    private int repeat;
    
    public MemoryPreset() {
    }
    
    public MemoryPreset(ByteReader reader) throws IOException {
        colorIndex = reader.read() & 0x0f;
        repeat = reader.read() & 0x0f;
        reader.skip(14);
    }
    
    public int getColorIndex() {
        return colorIndex;
    }
    
    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }
    
    public int getRepeat() {
        return repeat;
    }
    
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
