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
package com.yocalist.client.ui;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.yocalist.shared.ui.CanvasPresenter;

public class ClientWidget extends Composite {

    private static ClientWidgetUiBinder uiBinder = GWT
            .create(ClientWidgetUiBinder.class);

    interface ClientWidgetUiBinder extends UiBinder<Widget, ClientWidget> {
    }

    @UiField
    DropPanel dropPanel;
    
    @UiField
    Label status;
    
    @UiField
    Canvas canvas;

    public ClientWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiFactory
    Canvas createCanvas(int width, int height) {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        return canvas;
    }

    @UiHandler("dropPanel")
    public void onDragOver(DragOverEvent event) {
        event.stopPropagation();
        event.preventDefault();
    }
    
    @UiHandler("dropPanel")
    public void onDragEnter(DragEnterEvent event) {
        dropPanel.getElement().getStyle().setBorderColor("red");
        event.stopPropagation();
        event.preventDefault();
    }
    
    @UiHandler("dropPanel")
    public void onDragLeave(DragLeaveEvent event) {
        dropPanel.getElement().getStyle().setBorderColor("black");
        event.stopPropagation();
        event.preventDefault();
    }
    
    @UiHandler("dropPanel")
    public void onDrop(DropEvent event) {
        FileList files = event.getDataTransfer().<DataTransferExt>cast().getFiles();
        GWT.log("File length: " + files.getLength());
        for (File file : files) {
            GWT.log("Name: " + file.getName() + " Size: " + file.getSize() +
                    " Type: " + file.getType());
            final FileReader reader = new FileReader();
            reader.addLoadEndHandler(new LoadEndHandler() {
                @Override
                public void onLoadEnd(LoadEndEvent event) {
                    try {
                        final GwtCanvasView view = new GwtCanvasView(canvas.getCanvasElement(), 
                                reader.getResult(), status);
                        final CanvasPresenter presenter = new CanvasPresenter(view);
                        presenter.init();
                        Scheduler.get().scheduleIncremental(new RepeatingCommand() {
                                @Override
                                public boolean execute() {
                                    if (view.isLoadComplete()) {
                                        return presenter.tick();
                                    } else {
                                        return true;
                                    }
                                }
                            });
                    } catch (Exception e) {
                        GWT.log("Can't read", e);
                    }
                }
            });
            reader.readAsBinaryString(file);
        }
        dropPanel.getElement().getStyle().setBorderColor("black");
        event.stopPropagation();
        event.preventDefault();
    }
}
