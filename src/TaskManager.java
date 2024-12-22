import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TaskManager {
    private Scanner sc = new Scanner(System.in);
    private ArrayList<Task> taskArrayList = new ArrayList<Task>();

    class Task {
        private String title;
        private String description;
        private String duedate;
        private String category;
        private String prioritylevel;
        private String recurrence;
        private boolean isComplete;
        private boolean recurrenceFlag;

        public Task(String t, String d, String dd, String c, String pl, String r) {
            title = t;
            description = d;
            duedate = dd;
            category = c;
            prioritylevel = pl;
            recurrence = r;
        }

        public String getPriority() {
            return prioritylevel;
        }

        public String getDueDate() {
            return duedate;
        }

        public void isReccurence() {
            recurrenceFlag = true;
        }
        public void markAsComplete() {
            isComplete = true;
        }

        public void markAsIncomplete() {
            isComplete = false;
        }

        public void updateDescription(String des) {
            description = des;
        }

        public void updateDueDate(String due) {
            duedate = due;
        }

        public String toString() {
            return "Title : " + title +
                    "\nDescription : " + description +
                    (recurrenceFlag ?
                            "\nCategory : " + category +
                            "\nRecurrence : " + recurrence :
                            "\nDue Date : " + duedate +
                            "\nStatus : " + (isComplete ? "Complete" : "Incomplete") +
                            "\nCategory : " + category +
                            "\nPriority Level : " + prioritylevel
                    );
        }

        public String getTitle() {
            return title;
        }
    }

    public void createTask() {
        System.out.println("=== Add a New Task ===");
        System.out.println();
        System.out.print("Enter task title : ");
        String title = sc.nextLine();
        System.out.print("Enter task description : ");
        String description = sc.nextLine();
        System.out.print("Enter due date (YYYY-MM-DD) : ");
        String duedate = sc.nextLine();
        //check date format**
        System.out.print("Enter task category (Homework, Personal, Work) : ");
        String category = sc.nextLine();
        //check category format**
        System.out.print("Priority level (Low, Medium, High) : ");
        String prioritylevel = sc.nextLine();
        //check priority format**

        Task newTask = new Task(title, description, duedate, category, prioritylevel, null);
        taskArrayList.add(newTask);

        System.out.println("Task \"" + title + "\" added successfully!");
        System.out.println();
    }

    public void deleteTask() {
        System.out.println("=== Delete a Task ===");
        System.out.println();

        if (taskArrayList.isEmpty()) {
            System.out.println("No tasks available to delete.");
            return;
        }

        viewTasks();

        System.out.print("Enter the task number you want to delete : ");
        int taskNumber = sc.nextInt();
        sc.nextLine();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task removedtask = taskArrayList.remove(taskNumber - 1);
            System.out.println("Task \"" + removedtask.getTitle() + "\" deleted successfully!");
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }

    public void markTaskAsComplete() {
        System.out.println("=== Mark Task as Complete ===");
        System.out.println();
        if (taskArrayList.isEmpty()) {
            System.out.println("No tasks available to mark.");
            return;
        }
        viewTasks();
        System.out.print("Enter the task number you want to mark as complete : ");
        int taskNumber = sc.nextInt();
        sc.nextLine();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);
            task.markAsComplete();
            System.out.println("Task \"" + task.getTitle() + "\" marked as complete!");
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }

    public void updateTaskDetails() {
        System.out.println("=== Update Task Details ===");
        System.out.println();

        if (taskArrayList.isEmpty()) {
            System.out.println("No tasks available to update.");
            return;
        }

        viewTasks();

        System.out.print("Enter the task number you want to update : ");
        int taskNumber = sc.nextInt();

        sc.nextLine();
        System.out.println();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);
            System.out.println("1. Update Description.");
            System.out.println("2. Update Due Date.");
            System.out.println();
            System.out.print("Choose an option : ");
            int updatechoice = sc.nextInt();
            sc.nextLine();

            switch (updatechoice) {
                case 1:
                    System.out.print("Enter new description : ");
                    String newdescription = sc.nextLine();
                    task.updateDescription(newdescription);
                    System.out.println("Task description update successfully!");
                    break;
                case 2:
                    System.out.print("Enter new due date (YYYY-MM-DD) : ");
                    String newduedate = sc.nextLine();
                    task.updateDueDate(newduedate);
                    System.out.println("Task due date updated successfully!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }

    public void viewTasks() {
        System.out.println("=== Task List ===");
        System.out.println();

        if (taskArrayList.isEmpty()) {
            System.out.println("No task available.");
            return;
        }

        for (int i = 0; i < taskArrayList.size(); i++) {
            System.out.println("Task " + (i + 1) + " : ");
            System.out.println(taskArrayList.get(i));
            System.out.println();
        }
    }

    public void addRecurringTask() {
        System.out.println("=== Add a Recurring Task ===");
        System.out.println();
        System.out.print("Enter task title : ");
        String title = sc.nextLine();
        System.out.print("Enter task description : ");
        String description = sc.nextLine();
        System.out.print("Enter task category (Homework, Personal, Work) : ");
        String category = sc.nextLine();
        //check format*
        System.out.print("Enter recurrence interval (daily, weekly, monthly) : ");
        String recurrence = sc.nextLine();

        Task recurringTask = new Task(title, category, null, category,null, recurrence);
        recurringTask.isReccurence();
        taskArrayList.add(recurringTask);
        System.out.println("Recurring Task \"" + title + "\" created successfully!");
        System.out.println();
    }

    public void searchTasks() {
        System.out.println("=== Search Tasks ===");
        System.out.println();
        System.out.print("Enter a keyword to search by title or description: ");
        String keyword = sc.nextLine();
        ArrayList<Task> results = new ArrayList<>();
        for (Task task : taskArrayList) {
            if (task.title.toLowerCase().contains(keyword.toLowerCase()) || task.description.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(task);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("=== Search Results ===");
            System.out.println();

            int i = 1;
            for (Task task : results) {
                System.out.println("Task "+i+" :\n"+task);
                i++;
            }
        }
    }

    public void sortTasks() {
        System.out.print("""
                         === Sort Tasks ===
                         
                         1. Due Date (Ascending)
                         2. Due Date (Descending)
                         3. Priority (High to Low)
                         4. Priority (Low to High)

                         """);
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine();
        System.out.println();

        boolean ascending = false;
        switch (choice) {
            case 1 -> bubbleSortByDueDate(true);
            case 2 -> bubbleSortByDueDate(false);
            case 3 -> bubbleSortByPriority(false);
            case 4 -> bubbleSortByPriority(true);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void bubbleSortByPriority(boolean ascending) {
        int n = taskArrayList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int priority1 = getPriorityValue(taskArrayList.get(j).getPriority());
                int priority2 = getPriorityValue(taskArrayList.get(j + 1).getPriority());

                // Debugging output
                //System.out.printf("Comparing priorities: %s (%d) vs %s (%d)%n",
                        //taskArrayList.get(j).getPriority(), priority1, taskArrayList.get(j + 1).getPriority(), priority2);

                if (priority1 == priority2) {
                    continue;
                }

                if ((ascending && priority1 > priority2) || (!ascending && priority1 < priority2)) {
                    Collections.swap(taskArrayList, j, j + 1);
                    //System.out.println("Swapped!");
                }
            }
        }

        // Explicitly reverse for "High to Low"
        if (!ascending) {
            Collections.reverse(taskArrayList);
        }

        //System.out.println("Tasks sorted by Priority (" + (ascending ? "Low to High" : "High to Low") + "):");
        //taskArrayList.forEach(System.out::println);
    }

    private static int getPriorityValue(String priority) {
        if (priority == null) return Integer.MAX_VALUE; // Handle null priorities gracefully
        switch (priority.trim().toLowerCase()) {        // Trim and normalize input
            case "high":   return 1;
            case "medium": return 2;
            case "low":    return 3;
            default:       return Integer.MAX_VALUE;    // Default for invalid priorities
        }
    }

    private void bubbleSortByDueDate(boolean ascending) {
        int n = taskArrayList.size();
        int k = 0;

        for (int i = 0; i < n; i++) {
            String date = taskArrayList.get(i).getDueDate();

            if (date==null){
                if(i!=0) {
                    Collections.swap(taskArrayList, i, k);
                }
                k++;

            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String date1 = taskArrayList.get(j).getDueDate();
                String date2 = taskArrayList.get(j + 1).getDueDate();

                if(date1==null||date2==null){
                    continue;
                }

                if (date1.compareTo(date2)==0){
                    continue;
                }

                if ((ascending && date1.compareTo(date2) > 0) || (!ascending && date1.compareTo(date2) < 0)) {
                    Collections.swap(taskArrayList, j, j + 1);
                }
            }
        }
        //System.out.println("Tasks sorted by Due Date " + (ascending ? "(Ascending)!" : "(Descending)!"));
    }
}
