package com.smdev.plugins;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ProjectChangeComponent implements ProjectComponent {

    private final static Logger LOG = Logger.getInstance(ProjectChangeComponent.class);

    private final Project project;

    @NotNull
    private final AppChangeComponent applicationComponent;

    private ProjectChangeListener listener;

    /**
     * @param project The current project, i.e. the project which was just opened.
     */
    public ProjectChangeComponent(Project project, @NotNull AppChangeComponent applicationComponent) {
        this.project = project;
        this.applicationComponent = applicationComponent;
    }

    public void initComponent() {
        //called before projectOpened()
    }

    public void projectOpened() {
        LOG.info(String.format("Project '%s' has been opened, base dir '%s'", project.getName(), project.getBaseDir().getCanonicalPath()));

        this.listener = new ProjectChangeListener(this.project);
    }

    public void projectClosed() {
        LOG.info(String.format("Project '%s' has been closed.", project.getName()));

        this.listener.disposeComponent();
    }

    public void disposeComponent() {
        //called after projectClosed()
    }

    @NotNull
    public String getComponentName() {
        return "myProjectComponent";
    }
}
