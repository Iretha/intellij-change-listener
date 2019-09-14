package com.smdev.plugins;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;

import java.util.List;

public class ProjectChangeListener implements BulkFileListener {


    private final MessageBusConnection connection;

    private ProjectChangeManager changeManager;

    public ProjectChangeListener(Project project) {
        this.connection = ApplicationManager.getApplication().getMessageBus().connect();
        this.connection.subscribe(VirtualFileManager.VFS_CHANGES, this);
        this.changeManager = new ProjectChangeManager(project.getName());
    }

    public void disposeComponent() {
        this.connection.disconnect();
        this.changeManager.dispose();
    }

    public void before(List<? extends VFileEvent> events) {
        this.changeManager.registerEvents(events);
        //System.out.println("before" + events);
    }

    public void after(List<? extends VFileEvent> events) {
        this.changeManager.registerEvents(events);
        //System.out.println("after" + events);
    }
}

