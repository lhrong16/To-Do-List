import java.util.HashMap;
import java.util.Map;

public class DataAnalytics {
    private TaskManager taskManager;

    public DataAnalytics(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void showAnalyticsDashboard() {
        int totalTasks = taskManager.getTaskArrayList().size();
        int completedTasks = 0;
        int pendingTasks = 0;
        Map<String, Integer> categorySummary = new HashMap<>();

        for (Task task : taskManager.getTaskArrayList()) {
            if (task.getStatus()) {
                completedTasks++;
            } else {
                pendingTasks++;
            }

            String category = task.getCategory();
            categorySummary.put(category, categorySummary.getOrDefault(category, 0) + 1);
        }

        double completionRate = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0;

        System.out.println("=== Analytics Dashboard ===");
        System.out.println("- Total Tasks: " + totalTasks);
        System.out.println("- Completed: " + completedTasks);
        System.out.println("- Pending: " + pendingTasks);
        System.out.println("- Completion Rate: " + String.format("%.2f", completionRate) + "%");
        System.out.print("- Task Categories: ");
        String[] categories = {"Homework", "Personal", "Work"};
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            if (categorySummary.containsKey(category)) {
                System.out.print(category + ": " + categorySummary.get(category));
                if (i < categories.length - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println();
    }
}
