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

/**
 * Simple color class
 * 
 * @author Chad Retz
 */
public class Color {

    private int red;
    private int green;
    private int blue;

    public Color() {

    }
    
    public Color(int rgb) {
        this((rgb >> 16) & 0xFF, 
                (rgb >> 8) & 0xFF, 
                (rgb >> 0) & 0xFF); 
    }

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
    
    public int getRgb() {
        return (255 << 24) |
                (red << 16) |
                (green << 8) |
                blue;
    }
    
    public Color xor(Color color) {
        return new Color(getRgb() ^ color.getRgb());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blue;
        result = prime * result + green;
        result = prime * result + red;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Color other = (Color) obj;
        if (blue != other.blue) {
            return false;
        }
        if (green != other.green) {
            return false;
        }
        if (red != other.red) {
            return false;
        }
        return true;
    }

}
