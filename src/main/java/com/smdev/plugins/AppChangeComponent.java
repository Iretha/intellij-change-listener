package com.smdev.plugins;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AppChangeComponent implements ApplicationComponent {
    private static final Logger LOG = Logger.getInstance(AppChangeComponent.class);

    private Date startTime;

    /**
     * This method is called on startup of the application, i.e. as soon as the plugin is initialized.
     */
    public void initComponent() {
        LOG.info("Initializing plugin data structures on intelliJ started");

        this.startTime = new Date();
    }

    /**
     * This method is called as soon as the application is closed.
     */
    public void disposeComponent() {
        LOG.info("Disposing plugin data structures on intelliJ closed");

        Date endTime = new Date();

        System.out.println("App component was running for  " + ((endTime.getTime() - startTime.getTime()) / 1000) + "sec");
    }

    @NotNull
    public String getComponentName() {
        return "myApplicationComponent";
    }
}
