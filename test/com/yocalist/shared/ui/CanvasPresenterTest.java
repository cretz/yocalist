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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import junit.framework.TestCase;

import com.yocalist.server.ui.AwtImageCanvasView;
import com.yocalist.shared.cdg.CdgFileTest;

/**
 * Canvas presenter test
 * 
 * @author Chad Retz
 */
public class CanvasPresenterTest extends TestCase {

    public void testCanvas() throws Exception {
        final AwtImageCanvasView view = new AwtImageCanvasView(CdgFileTest.getSampleReader());
        CanvasPresenter presenter = new CanvasPresenter(view);
        //init
        presenter.init();
        //let's go...panel first
        @SuppressWarnings("serial")
        class MyPanel extends JPanel {
            private final Image image;
            
            public MyPanel() {
                super();
                image = createImage(view.getSource());
                setPreferredSize(new Dimension(
                        AwtImageCanvasView.FULL_WIDTH, 
                        AwtImageCanvasView.FULL_HEIGHT));
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, this);
            }
        }
        JLabel index = new JLabel(); 
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(index, BorderLayout.NORTH);
        frame.getContentPane().add(new MyPanel(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        while (presenter.tick()) {
            //chill
        }
    }
}
