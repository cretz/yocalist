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
import java.util.ArrayList;
import java.util.List;

import com.yocalist.shared.ByteReader;
import com.yocalist.shared.YocalistRuntimeException;
import com.yocalist.shared.cdg.chunk.CdgChunk;

public class CdgChunkArrayReader extends CdgReader {

    public CdgChunkArrayReader(ByteReader reader) {
        super(reader);
    }

    private List<CdgChunk> chunks;
    
    @Override
    public void read() throws IOException {
        if (chunks != null) {
            throw new YocalistRuntimeException("Reader can only be used once"); 
        }
        while (next()) {
            //use up this thread
        }
    }
    
    @Override
    protected void handleChunk(CdgChunk chunk, int index, int length) {
        if (chunks == null) {
            chunks = new ArrayList<CdgChunk>(length);
        }
        chunks.add(chunk);
    }

    public List<CdgChunk> getChunks() {
        return chunks;
    }
}
