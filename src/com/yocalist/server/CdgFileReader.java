package com.yocalist.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.yocalist.shared.cdg.CdgChunkArrayReader;

public class CdgFileReader extends CdgChunkArrayReader {

    public CdgFileReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file), (int) file.length());
    }
    
    public CdgFileReader(InputStream stream, int length) {
        super(new InputStreamByteReader(stream, length));
    }
}
