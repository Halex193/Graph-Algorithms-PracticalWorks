package activities;

import java.util.List;
import java.util.stream.Collectors;

class ConsoleUI
{
    private Controller controller;

    public ConsoleUI(Controller controller)
    {
        this.controller = controller;
    }

    public void run()
    {
        Controller.DTOProject project = controller.createProject();
        if (project == null)
        {
            System.out.println("The corresponding graph is not a DAG");
            return;
        }
        List<Activity> activities = project.activityList;
        List<String> topologicalOrder = activities.stream()
                                                  .map(activity -> String.valueOf(activity.vertex))
                                                  .collect(Collectors.toList());
        System.out.printf("A topological order is: %s%n", String.join(", ", topologicalOrder));

        String tableFormat = "| %-8s | %-8d | %-5d | %-3d | %-5d | %-3d | %-8s |%n";

        System.out.format("+----------+----------+-------------+-------------+----------+%n");
        System.out.format("| Activity | Duration |   Minimum   |   Maximum   | Critical |%n");
        System.out.format("|          |          | Start | End | Start | End |          |%n");
        System.out.format("+----------+----------+-------+-----+-------+-----+----------+%n");
        for (Activity activity : activities)
        {
            System.out.format(
                    tableFormat,
                    activity.vertex,
                    activity.duration,
                    activity.minStartTime,
                    activity.minEndTime,
                    activity.maxStartTime,
                    activity.maxEndTime,
                    activity.critical ? "X" : ""
            );
        }
        System.out.format("+----------+----------+-------+-----+-------+-----+----------+%n");
        System.out.printf("Project completion time: %s%n", project.minEndTime);
    }

}
