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
package com.yocalist.shared.ui;


/**
 * Simple view
 * 
 * @author Chad Retz
 */
public interface CanvasView {

    int DISPLAY_WIDTH = 294;
    int DISPLAY_HEIGHT = 204;
    
    int FRAME_WIDTH = 300;
    int FRAME_HEIGHT = 216;
    
    int TILE_WIDTH = 6;
    int TILE_HEIGHT = 12;

    int TILES_PER_ROW = FRAME_WIDTH / TILE_WIDTH;
    int TILES_PER_COL = FRAME_HEIGHT / TILE_HEIGHT;
    
    void init();
    
    boolean tick();
}
