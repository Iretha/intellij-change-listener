package com.smdev.plugins;

import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.smdev.plugins.util.ProjectChangesPrinter;

import java.util.*;

public enum AppChangeManager {

    INSTANCE;

    /**
     * 5 minutes
     */
    public static final long MAX_INACTIVE_INTERVAL_SECONDS = 5 * 50 * 1000;

    private static Map<String, Deque<Change>> PROJECT_CHANGES_IN_PROGRESS = new HashMap<>();
    private static Map<String, Deque<Change>> PROJECT_CHANGES_LOG = new HashMap<>();
    private static Map<String, Map<String, Long>> PROJECT_CHANGES_DURATION_BY_FILE = new HashMap<>();
    private static Map<String, Date> PROJECT_UPDATE_TIMES = new HashMap<>();

    public void onProjectOpened(String project) {
        PROJECT_CHANGES_IN_PROGRESS.put(project, new LinkedList<>());
        PROJECT_CHANGES_DURATION_BY_FILE.put(project, new HashMap<>());
        PROJECT_UPDATE_TIMES.put(project, new Date());

        PROJECT_CHANGES_LOG.getOrDefault(project, new LinkedList<>());

        //TODO load history
    }

    public <E extends VFileEvent> void onProjectFileChanged(String project, List<E> events) {
        if (events == null) {
            return;
        }

        Date now = new Date();
        checkProjectIfInactive(project, now);
        for (E event : events) {
            registerProjectChangeEvent(project, event, now);
        }
    }

    private <E extends VFileEvent> void registerProjectChangeEvent(String project, E event, Date now) {
        Deque<Change> projectChangesInProgress = PROJECT_CHANGES_IN_PROGRESS.getOrDefault(project, new LinkedList<>());

        Change previousChange = projectChangesInProgress.peek();
        Change currentChange = new Change(event.getPath());

        if (previousChange == null) { // this is the first change since the project is opened or after pause
            projectChangesInProgress.push(currentChange);
        } else {
            previousChange.setEndTime(now); // update last change time

            if (!previousChange.getPath().equalsIgnoreCase(currentChange.getPath())) { // it's another file
                moveLastProjectChangeToLog(project, projectChangesInProgress.pollLast()); // move prev. file change to log
                projectChangesInProgress.push(currentChange);
            }
        }
    }

    private void checkProjectIfInactive(String project, Date now) {
        if (PROJECT_UPDATE_TIMES.get(project).getTime() + MAX_INACTIVE_INTERVAL_SECONDS > now.getTime()) {
            moveCurrentProjectChangesToLog(project);  // user was inactive, don't sum durations, but add a new one
        }
    }

    private void moveCurrentProjectChangesToLog(String project) {
        Deque<Change> projectChangesInProgress = PROJECT_CHANGES_IN_PROGRESS.getOrDefault(project, new LinkedList<>());
        while (!projectChangesInProgress.isEmpty()) {
            moveLastProjectChangeToLog(project, projectChangesInProgress.pollLast());
        }
    }

    private void moveLastProjectChangeToLog(String project, Change lastChange) {
        if (lastChange == null) {
            return;
        }
        String path = lastChange.getPath();

        // add to log
        PROJECT_CHANGES_LOG.getOrDefault(project, new LinkedList<>()).add(lastChange);

        // sum change duration
        Long prevDuration = PROJECT_CHANGES_DURATION_BY_FILE.getOrDefault(project, new HashMap<>()).getOrDefault(path, 0L);
        PROJECT_CHANGES_DURATION_BY_FILE.get(project).put(path, prevDuration + lastChange.getChangeDuration());
    }

    public void onProjectClosed(String project) {
        moveCurrentProjectChangesToLog(project);

        StringBuilder projectChangesLog = ProjectChangesPrinter.generateProjectChangeLog(project, PROJECT_CHANGES_DURATION_BY_FILE.get(project));
        System.out.println(projectChangesLog.toString());

        //TODO save history
    }
}
