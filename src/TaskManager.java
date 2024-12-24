import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TaskManager {
    private Scanner sc = new Scanner(System.in);
    private ArrayList<Task> taskArrayList = new ArrayList<Task>();

    MyJDBC sql = new MyJDBC();

    class Task {
        private String title;
        private String description;
        private String duedate;
        private String category;
        private String prioritylevel;
        private String recurrence;
        private Task dependency;
        private boolean isComplete;
        private boolean recurrenceFlag;
        private int dependencyId;

        public Task(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf, int depId) {
            title = t;
            description = d;
            duedate = dd;
            category = c;
            prioritylevel = pl;
            recurrence = r;
            isComplete = s;
            recurrenceFlag = rf;
            dependencyId = depId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDueDate() {
            return duedate;
        }

        public String getCategory() {
            return category;
        }

        public String getPriority() {
            return prioritylevel;
        }

        public String getRecurrence() {
            return recurrence;
        }

        public Boolean getStatus() {
            return isComplete;
        }

        public Boolean getRecurrenceFlag() {
            return recurrenceFlag;
        }

        public Task getDependency() {
            return dependency;
        }

        public void setDependency(Task dependency) {
            this.dependency = dependency;
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

        public int getDependencyId() {
            return dependencyId;
        }

        public void setDependencyId(int dependencyId) {
            this.dependencyId = dependencyId;
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
                                    "\nPriority Level : " + prioritylevel +
                                    (dependency != null ? "\n(Depends on \"" + dependency.getTitle() + "\")" : "")
                    );
        }
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void importSQL(){
        sql.importSQL(this);
    }

    public void importTasks(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf, int depId) {
        Task sqlTask = new Task(t, d, dd, c, pl, r, s, rf, depId);
        taskArrayList.add(sqlTask);
    }

    public void addTask() {
        System.out.println("=== Add a New Task ===");
        System.out.println();
        System.out.print("Enter task title: ");
        String title = sc.nextLine();
        System.out.print("Enter task description: ");
        String description = sc.nextLine();
        System.out.print("Enter due date (YYYY-MM-DD): ");
        String duedate = sc.nextLine();
        // Check date format**
        System.out.print("Enter task category (Homework, Personal, Work): ");
        String category = sc.nextLine();
        // Check category format**
        System.out.print("Priority level (Low, Medium, High): ");
        String prioritylevel = sc.nextLine();
        // Check priority format**

        Task newTask = new Task(title, description, duedate, category, prioritylevel, null, false, false, 0);
        taskArrayList.add(newTask);

        System.out.println("Task \"" + title + "\" added successfully!");
        System.out.println();
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

        Task recurringTask = new Task(title, description, null, category,null, recurrence, false, true, 0);
        //recurringTask.isRecurrence();
        taskArrayList.add(recurringTask);
        System.out.println("Recurring Task \"" + title + "\" created successfully!");
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
            if (task.getDependency() != null && !task.getDependency().getStatus()) {
                System.out.println("Warning: Task \"" + task.getTitle() + "\" cannot be marked as complete because it depends on \"" + task.getDependency().getTitle() + "\". Please complete \"" + task.getDependency().getTitle() + "\" first.");
            } else {
                task.markAsComplete();
                System.out.println("Task \"" + task.getTitle() + "\" marked as complete!");
            }
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }

    /*
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
    */


    public void viewTasks() {
        System.out.println("=== Task List ===");
        System.out.println();

        if (taskArrayList.isEmpty()) {
            System.out.println("No task available.");
            return;
        }

        for (int i = 0; i < taskArrayList.size(); i++) {
            Task task = taskArrayList.get(i);
            System.out.print((i + 1) + ". [" + (task.getStatus() ? "Complete" : "Incomplete") + "] " + task.getTitle() + ": " + task.getDescription());
            if (task.getDueDate() != null) {
                System.out.print(" - Due: " + task.getDueDate());
            }
            if (task.getDependency() != null) {
                System.out.print(" (Depends on " + task.getDependency().getTitle() + ")");
            }
            System.out.println();
        }
        System.out.println();
    }

    /*
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
    */

    public void searchTasks() {
        System.out.println("=== Search Tasks ===");
        System.out.println();
        System.out.print("Enter a keyword to search by title or description : ");
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
                System.out.println();
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

        switch (choice) {
            case 1 -> bubbleSortByDueDate(true);
            case 2 -> bubbleSortByDueDate(false);
            case 3 -> bubbleSortByPriority(true);
            case 4 -> bubbleSortByPriority(false);
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

    public void setTaskDependency() {
        System.out.println("=== Set Task Dependency ===");

        System.out.print("Enter task number that depends on another task: ");
        int dependentTaskNumber = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter the task number it depends on: ");
        int dependencyTaskNumber = sc.nextInt();
        sc.nextLine();

        if (dependentTaskNumber >= 1 && dependentTaskNumber <= taskArrayList.size() &&
                dependencyTaskNumber >= 1 && dependencyTaskNumber <= taskArrayList.size()) {
            Task dependentTask = taskArrayList.get(dependentTaskNumber - 1);
            Task dependencyTask = taskArrayList.get(dependencyTaskNumber - 1);
            dependentTask.setDependency(dependencyTask);
            System.out.println("Task \"" + dependentTask.getTitle() + "\" now depends on \"" + dependencyTask.getTitle() + "\".");
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }


    public void deleteTaskDependency() {
        System.out.println("=== Delete Task Dependency ===");
        System.out.print("Enter the task number for which you want to delete the dependency: ");
        int taskNumber = sc.nextInt();
        sc.nextLine();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);
            if (task.getDependency() != null) {
                task.setDependency(null);
                System.out.println("Dependency for task \"" + task.getTitle() + "\" has been deleted.");
            } else {
                System.out.println("Task \"" + task.getTitle() + "\" does not have any dependency.");
            }
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }


    public void editTask() {
        System.out.println("=== Edit Task ===");
        System.out.println();

        if (taskArrayList.isEmpty()) {
            System.out.println("No tasks available to edit.");
            return;
        }

        viewTasks();

        System.out.print("Enter the task number you want to edit: ");
        int taskNumber = sc.nextInt();
        sc.nextLine();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);
            System.out.println("What would you like to edit?");
            System.out.println("1. Title");
            System.out.println("2. Description");
            System.out.println("3. Due Date");
            System.out.println("4. Category");
            System.out.println("5. Priority");
            System.out.println("6. Set Task Dependency");
            System.out.println("7. Cancel");
            System.out.print("> ");
            int editChoice = sc.nextInt();
            sc.nextLine();

            switch (editChoice) {
                case 1:
                    System.out.print("Enter the new title: ");
                    String newTitle = sc.nextLine();
                    String oldTitle = task.title;
                    task.title = newTitle;
                    System.out.println("Task \"" + oldTitle + "\" has been updated to \"" + newTitle + "\".");
                    break;
                case 2:
                    System.out.print("Enter the new description: ");
                    String newDescription = sc.nextLine();
                    String oldDescription = task.description;
                    task.description = newDescription;
                    System.out.println("Task \"" + oldDescription + "\" has been updated to \"" + newDescription + "\".");
                    break;
                case 3:
                    System.out.print("Enter the new due date (YYYY-MM-DD): ");
                    String newDueDate = sc.nextLine();
                    String oldDueDate = task.duedate;
                    task.duedate = newDueDate;
                    System.out.println("Task \"" + oldDueDate + "\" has been updated to \"" + newDueDate + "\".");
                    break;
                case 4:
                    System.out.print("Enter the new category: ");
                    String newCategory = sc.nextLine();
                    String oldCategory = task.category;
                    task.category = newCategory;
                    System.out.println("Task \"" + oldCategory + "\" has been updated to \"" + newCategory + "\".");
                    break;
                case 5:
                    System.out.print("Enter the new priority: ");
                    String newPriority = sc.nextLine();
                    String oldPriority = task.prioritylevel;
                    task.prioritylevel = newPriority;
                    System.out.println("Task \"" + oldPriority + "\" has been updated to \"" + newPriority + "\".");
                    break;
                case 6:
                    setTaskDependency();
                    break;
                case 7:
                    System.out.println("Edit cancelled.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }


    public void closeTaskManager() {
        sql.clearSQL();
        for (Task task : taskArrayList) {
            String t = task.getTitle();
            String d = task.getDescription();
            String dd = task.getDueDate();
            String c = task.getCategory();
            String pl = task.getPriority();
            String r = task.getRecurrence();
            Boolean s = task.getStatus();
            Boolean rf = task.getRecurrenceFlag();
            int depId = task.getDependency() != null ? task.getDependency().getDependencyId() : 0;
            sql.exportSQL(t, d, dd, c, pl, r, s, rf, depId);
        }
    }
}
