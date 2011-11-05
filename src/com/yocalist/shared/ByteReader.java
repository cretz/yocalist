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
package com.yocalist.shared;

import java.io.IOException;

/**
 * Simple byte reader interface
 * 
 * @author Chad Retz
 */
public interface ByteReader {

    int getLength();
    
    int getIndex();
    
    Byte read() throws IOException;
    
    void read(byte[] bytes) throws IOException;
    
    void readAndMask(byte[] bytes, int mask) throws IOException;
    
    void skip() throws IOException;
    
    void skip(int count) throws IOException;
    
    void close() throws IOException;
}
