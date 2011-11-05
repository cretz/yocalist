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
 * Define transparent color chunk
 * 
 * @author Chad Retz
 */
public class DefineTransparentColor extends CdgChunk {

    private int colorIndex;
    
    public DefineTransparentColor() {
    }
    
    public DefineTransparentColor(ByteReader reader) throws IOException {
        colorIndex = reader.read() & 0x3f;
        reader.skip(15);
    }

    public int getColorIndex() {
        return colorIndex;
    }
    
    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }
}
