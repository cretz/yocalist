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

import junit.framework.TestCase;

import org.junit.Assert;

import com.yocalist.server.CdgFileReader;
import com.yocalist.shared.Color;
import com.yocalist.shared.cdg.chunk.BorderPreset;
import com.yocalist.shared.cdg.chunk.DefineTransparentColor;
import com.yocalist.shared.cdg.chunk.LoadColorTable;
import com.yocalist.shared.cdg.chunk.MemoryPreset;

/**
 * CdgFile test
 * 
 * @author Chad Retz
 */
public class CdgFileTest extends TestCase {
    
    public static CdgFileReader getSampleReader() throws IOException {
        return new CdgFileReader(CdgFileTest.class.
                getResourceAsStream("MaryHadALittleLamb.cdg"), 573024);
    }

    public void testCdgFile() throws IOException {
        CdgFileReader reader = getSampleReader();
        reader.read();
        //check chunk size
        Assert.assertEquals(13992, reader.getChunks().size());
        //check transparent color
        DefineTransparentColor defineChunk = (DefineTransparentColor) 
                reader.getChunks().get(0);
        Assert.assertEquals(0, defineChunk.getColorIndex());
        //check the low colors
        LoadColorTable colorTable = (LoadColorTable) reader.getChunks().get(1);
        Assert.assertTrue(colorTable.isLow());
        Assert.assertArrayEquals(new Color[] {
                new Color(136, 204, 136),
                new Color(17, 17, 17),
                new Color(255, 255, 34),
                new Color(204, 238, 68),
                new Color(68, 102, 68),
                new Color(153, 187, 85),
                new Color(85, 136, 102),
                new Color(102, 187, 119)
            }, colorTable.getColors());
        //check the high colors
        colorTable = (LoadColorTable) reader.getChunks().get(2);
        Assert.assertFalse(colorTable.isLow());
        Assert.assertArrayEquals(new Color[] {
                new Color(170, 221, 119),
                new Color(136, 204, 136),
                new Color(238, 238, 136),
                new Color(153, 153, 153),
                new Color(170, 221, 170),
                new Color(187, 204, 187),
                new Color(221, 221, 221),
                new Color(255, 255, 255)
            }, colorTable.getColors());
        //check border preset
        BorderPreset borderPreset = (BorderPreset) reader.getChunks().get(3);
        Assert.assertEquals(0, borderPreset.getColorIndex());
        //check memory presets
        for (int i = 0; i <= 15; i++) {
            MemoryPreset memoryPreset = (MemoryPreset) reader.getChunks().get(4 + i);
            Assert.assertEquals(0, memoryPreset.getColorIndex());
            Assert.assertEquals(i, memoryPreset.getRepeat());
        }
    }
}
