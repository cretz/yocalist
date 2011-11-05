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
package com.yocalist.shared.cdg;

import java.io.IOException;

import com.yocalist.shared.ByteReader;
import com.yocalist.shared.cdg.chunk.BorderPreset;
import com.yocalist.shared.cdg.chunk.CdgChunk;
import com.yocalist.shared.cdg.chunk.DefineTransparentColor;
import com.yocalist.shared.cdg.chunk.LoadColorTable;
import com.yocalist.shared.cdg.chunk.MemoryPreset;
import com.yocalist.shared.cdg.chunk.NoOp;
import com.yocalist.shared.cdg.chunk.ScrollCopy;
import com.yocalist.shared.cdg.chunk.ScrollPreset;
import com.yocalist.shared.cdg.chunk.TileBlock;

/**
 * Base reader
 * 
 * @author Chad Retz
 */
public abstract class CdgReader {
    
    private final ByteReader reader;
    private int length;
    private int index;
    
    public CdgReader(ByteReader reader) {
        this.reader = reader;
        length = reader.getLength() / 24;
        index = -1;
    }

    public abstract void read() throws IOException;
    
    protected boolean next() throws IOException {
        Byte command = reader.read();
        if (command == null) {
            return false;
        }
        command = (byte) (command & 0x3f);
        if (command == 0x09) {
            byte instruction = (byte) (reader.read() & 0x3f);
            reader.skip(2);
            switch (instruction) {
            case 1:
                handleChunk(new MemoryPreset(reader), ++index, length);
                break;
            case 2:
                handleChunk(new BorderPreset(reader), ++index, length);
                break;
            case 6:
            case 38:
                handleChunk(new TileBlock(reader, instruction == 6), 
                        ++index, length);
                break;
            case 20:
                handleChunk(new ScrollPreset(reader), ++index, length);
                break;
            case 24:
                handleChunk(new ScrollCopy(reader), ++index, length);
                break;
            case 28:
                handleChunk(new DefineTransparentColor(reader), ++index, length);
                break;
            case 30:
            case 31:
                handleChunk(new LoadColorTable(reader, instruction == 30), 
                        ++index, length);
                break;
            default:
                handleChunk(new NoOp(reader), ++index, length);
            }
            reader.skip(4);
        }
        return true;
    }
    
    protected abstract void handleChunk(CdgChunk chunk, int index, int length);
}
