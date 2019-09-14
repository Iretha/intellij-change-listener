package com.smdev.plugins;

import com.intellij.openapi.vfs.newvfs.events.VFileEvent;

import java.util.*;

public class ProjectChangeManager {

    private String projectName;

    public ProjectChangeManager(String projectName) {
        this.projectName = projectName;
        AppChangeManager.INSTANCE.onProjectOpened(this.projectName);
    }

    public <E extends VFileEvent> void registerEvents(List<E> events) {
        AppChangeManager.INSTANCE.onProjectFileChanged(this.projectName, events);
    }

    public void dispose() {
        AppChangeManager.INSTANCE.onProjectClosed(this.projectName);
    }
}
