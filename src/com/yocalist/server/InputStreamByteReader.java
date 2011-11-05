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
package com.yocalist.server;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.yocalist.shared.ByteReader;

/**
 * {@link ByteReader} for an {@link InputStream}
 * 
 * @author Chad Retz
 */
public class InputStreamByteReader implements ByteReader, Closeable {

    private final InputStream stream;
    private final int length;
    private int index = 0;
    
    public InputStreamByteReader(InputStream stream, int length) {
        this.stream = stream;
        this.length = length;
    }
    
    @Override
    public int getLength() {
        return length;
    }
    
    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Byte read() throws IOException {
        ++index;
        int read = stream.read();
        return read == -1 ? null : (byte) read;
    }

    @Override
    public void read(byte[] bytes) throws IOException {
        if (stream.read(bytes) != bytes.length) {
            throw new EOFException();
        }
        index += bytes.length;
    }

    @Override
    public void readAndMask(byte[] bytes, int mask) throws IOException {
        read(bytes);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] &= mask;
        }
    }

    @Override
    public void skip() throws IOException {
        skip(1);
    }

    @Override
    public void skip(int count) throws IOException {
        stream.skip(count);
        index += count;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

}
