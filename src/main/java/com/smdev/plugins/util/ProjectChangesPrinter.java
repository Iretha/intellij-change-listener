package com.smdev.plugins.util;

import java.util.Map;

public abstract class ProjectChangesPrinter {

    public static StringBuilder generateProjectChangeLog(String project, Map<String, Long> fileChanges){
        long overallSec = 0;
        long overallMin = 0;

        long durationMs;
        long durationSec;
        long durationMin;

        StringBuilder b = new StringBuilder();
        b.append(project).append(":\n");
        for (Map.Entry<String, Long> entry : fileChanges.entrySet()) {
            durationMs = entry.getValue();
            durationSec = durationMs / 1000;
            durationMin = durationSec / 60;

            b.append(entry.getKey())
                    .append(" => ")
                    .append(durationMs).append("ms")
                    .append(" = ")
                    .append(durationSec).append("sec")
                    .append(" = ")
                    .append(durationMin).append("min")
                    .append("\n");

            overallSec += durationSec;
            overallMin += durationMin;
        }

        b.append("Total = ").append(overallSec).append("sec").append(" = ").append(overallMin).append("min").append("\n");
        return b;
    }
}
