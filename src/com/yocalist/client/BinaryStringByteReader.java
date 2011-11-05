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
package com.yocalist.client;

import java.io.IOException;

import com.yocalist.shared.ByteReader;

/**
 * Byte reader from a binary string
 * 
 * @author Chad Retz
 */
public class BinaryStringByteReader implements ByteReader {

    private final String data;
    private int index;
    
    public BinaryStringByteReader(String data) {
        this.data = data;
    }

    @Override
    public int getLength() {
        return data.length();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Byte read() throws IOException {
        if (index >= data.length() - 1) {
            return null;
        }
        return (byte) data.charAt(++index);
    }

    @Override
    public void read(byte[] bytes) throws IOException {
        if (index + bytes.length >= data.length() - 1) {
            throw new IOException("Not enough left in stream");
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) data.charAt(++index);
        }
    }

    @Override
    public void readAndMask(byte[] bytes, int mask) throws IOException {
        if (index + bytes.length >= data.length() - 1) {
            throw new IOException("Not enough left in stream");
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (((byte) data.charAt(++index)) & mask);
        }
    }

    @Override
    public void skip() throws IOException {
        ++index;
    }

    @Override
    public void skip(int count) throws IOException {
        index += count;
    }

    @Override
    public void close() throws IOException {
        //no-op
    }
}
