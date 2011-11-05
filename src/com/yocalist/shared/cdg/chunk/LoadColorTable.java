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
import com.yocalist.shared.Color;

/**
 * Load color table chunk
 * 
 * @author Chad Retz
 */
public class LoadColorTable extends CdgChunk {

    private boolean low;
    private Color[] colors;
    
    public LoadColorTable() {
    }
    
    public LoadColorTable(ByteReader reader, boolean low) throws IOException {
        this.low = low;
        colors = new Color[8];
        for (int i = 0; i < 8; i++) {
            int one = reader.read() & 0x3f;
            int two = reader.read() & 0x3f;
            colors[i] = new Color(((one & 0x3f) >> 2) * 17, 
                (((one & 0x3) << 2) | ((two & 0x3f) >> 4)) * 17, 
                (two & 0xf) * 17);
        }
    }
    
    public boolean isLow() {
        return low;
    }
    
    public void setLow(boolean low) {
        this.low = low;
    }

    public Color[] getColors() {
        return colors;
    }
    
    public void setColors(Color[] colors) {
        this.colors = colors;
    }
}
