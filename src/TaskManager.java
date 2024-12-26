import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Task {
    private String title;
    private String description;
    private String duedate;
    private String category;
    private String prioritylevel;
    private String recurrence;
    private ArrayList<Task> dependencyArrayList;
    private String dependencyID;
    private boolean isComplete;
    private boolean recurrenceFlag;
    private boolean emailFlag;

    public Task(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf, boolean e, String depIDs) {
        title = t;
        description = d;
        duedate = dd;
        category = c;
        prioritylevel = pl;
        recurrence = r;
        isComplete = s;
        recurrenceFlag = rf;
        emailFlag = e;
        dependencyID = depIDs;
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

    public Boolean getEmailFlag() {
        return emailFlag;
    }

    public ArrayList<Task> getDependencyArrayList() {
        return dependencyArrayList;
    }

    public void setDependency(Task dependency) {
        if (dependencyArrayList != null) {
            dependencyArrayList.add(dependency);
        } else {
            dependencyArrayList = new ArrayList<Task>();
            dependencyArrayList.add(dependency);
        }
    }

    public String getDependencyTitles() {
        String allDependency = "";
        if (dependencyArrayList != null) {
            for (int i=0; i<dependencyArrayList.size(); i++) {
                Task task = dependencyArrayList.get(i);
                allDependency += "\"" + task.getTitle() + "\"";
                if (i + 1 != dependencyArrayList.size()) {
                    allDependency += ", ";
                }
            }
        }
        return allDependency;
    }

    public void deleteDependency(int depId) {
        dependencyArrayList.remove(depId);
    }

    public void setEmailFlag() {
        emailFlag = true;
    }

    public void markAsComplete() {
        isComplete = true;
    }

    public void markAsIncomplete() {
        isComplete = false;
    }

    public void setTitle (String t) {
        title = t;
    }

    public void setDescription (String d) {
        description = d;
    }

    public void setDueDate (String dd) {
        duedate = dd;
    }

    public void setCategory(String c) {
        category = c;
    }

    public void setPriority(String p) {
        prioritylevel = p;
    }

    public String getDependencyID() {
        return dependencyID;
    }

    public void setDependencyID(int depID) {
        dependencyID += depID+" ";
    }

    public void resetDependencyID() {
        dependencyID = "";
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
                                (dependencyArrayList != null ? "\n(Depends on task(s) " + getDependencyTitles() + ")": "")
                );
    }
}

public class TaskManager {
    private Scanner sc = new Scanner(System.in);
    private ArrayList<Task> taskArrayList = new ArrayList<Task>();

    MyJDBC sql = new MyJDBC();
    ValidateInput validate = new ValidateInput();

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void importSQL(){
        sql.importSQL(this);
    }

    public void importTasks(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf, boolean e, String depIDs) {
        Task sqlTask = new Task(t, d, dd, c, pl, r, s, rf, e, depIDs);
        taskArrayList.add(sqlTask);
    }


    public void importDependencies() {
        for (Task task : taskArrayList) {
            if (task.getDependencyID() != null && !task.getDependencyID().isEmpty()) {
                String[] depIdArrayList = task.getDependencyID().split(" ");
                for (String depID : depIdArrayList) {
                    if (depID != null && !depID.isEmpty()) {
                        int depIdInt = Integer.parseInt(depID);
                        task.setDependency(taskArrayList.get(depIdInt));
                    }
                }
                task.resetDependencyID();
            }
        }
    }

    /*
    public void importDependencies() {
        for (Task task : taskArrayList) {
            if (!task.getDependencyID().isEmpty()) {
                String[] depIdArrayList = task.getDependencyID().split(" ");
                for (String depID : depIdArrayList){
                    int depIdInt = Integer.parseInt(depID);
                    task.setDependency(taskArrayList.get(depIdInt));
                    task.resetDependencyID();
                }
            }
        }
    }
    */


    public void addTask() {
        System.out.println("=== Add a New Task ===");
        System.out.println();
        System.out.print("Enter task title : ");
        String title = sc.nextLine();
        System.out.print("Enter task description : ");
        String description = sc.nextLine();
        System.out.print("Enter due date (YYYY-MM-DD) : ");
        String duedate = sc.nextLine();
        while (!validate.validateDate(duedate)) {
            System.out.print("Error! Please enter a valid date (YYYY-MM-DD) : ");
            duedate = sc.nextLine();
        }
        System.out.print("Enter task category (Homework, Personal, Work) : ");
        String category = sc.nextLine();
        while (!validate.validateCategory(category)) {
            System.out.print("Error! Please enter a valid category (Homework, Personal, Work) : ");
            category = sc.nextLine();
        }
        System.out.print("Priority level (Low, Medium, High) : ");
        String prioritylevel = sc.nextLine();
        while (!validate.validatePriority(prioritylevel)) {
            System.out.print("Error! Please enter a valid priority level (Low, Medium, High) : ");
            prioritylevel = sc.nextLine();
        }

        Task newTask = new Task(title, description, duedate, category, prioritylevel, null, false, false, false, null);
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
        while (!validate.validateCategory(category)) {
            System.out.print("Error! Please enter a valid category (Homework, Personal, Work) : ");
            category = sc.nextLine();
        }
        System.out.print("Enter recurrence interval (daily, weekly, monthly) : ");
        String recurrence = sc.nextLine();

        Task recurringTask = new Task(title, description, null, category, null, recurrence, false, true, false, null);
        taskArrayList.add(recurringTask);
        System.out.println("Recurring Task \"" + title + "\" created successfully!");
        System.out.println();
    }

    
    public void setTaskDependency() {
        System.out.println("=== Set Task Dependency ===");

        System.out.print("Enter task number that depends on another task: ");
        int dependentTaskNumber = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter the task number it depends on: ");
        int dependencyTaskNumber = sc.nextInt();
        sc.nextLine();

        if (dependencyTaskNumber != dependentTaskNumber) {
            if (dependentTaskNumber >= 1 && dependentTaskNumber <= taskArrayList.size() && dependencyTaskNumber >= 1 && dependencyTaskNumber <= taskArrayList.size()) {
                Task dependentTask = taskArrayList.get(dependentTaskNumber - 1);
                Task dependencyTask = taskArrayList.get(dependencyTaskNumber - 1);
                boolean dependEachOther = false;
                boolean alreadyDepend = false;

                if (dependencyTask.getDependencyArrayList()!=null && !dependencyTask.getDependencyArrayList().isEmpty()) {
                    for (Task checkDependent : dependencyTask.getDependencyArrayList()) {
                        if (checkDependent == dependentTask) {
                            dependEachOther = true;
                        }
                    }
                }

                if (dependentTask.getDependencyArrayList()!=null && !dependentTask.getDependencyArrayList().isEmpty()) {
                    for (Task checkDependency : dependentTask.getDependencyArrayList()) {
                        if (checkDependency == dependencyTask) {
                            alreadyDepend = true;
                        }
                    }
                }

                if (!alreadyDepend && !dependEachOther) {
                    dependentTask.setDependency(dependencyTask);
                    System.out.println("Task \"" + dependentTask.getTitle() + "\" now depends on \"" + dependencyTask.getTitle() + "\".");
                } else if (alreadyDepend) {
                    System.out.println("Task \"" + dependentTask.getTitle() + "\" already depends on \"" + dependencyTask.getTitle() + "\".");
                } else {
                    System.out.println("Tasks cannot depend on each other!");
                }

            } else {
                System.out.println("Invalid task number.");
            }
        } else {
            System.out.println("Task cannot depend on itself!");
        }
        System.out.println();
    }

    public void setTaskDependency(int dependentTaskNumber) {
        if (dependentTaskNumber >= 1 && dependentTaskNumber <= taskArrayList.size()) {
            System.out.println("=== Set Task Dependency ===");
            System.out.print("Enter the task number it depends on: ");
            int dependencyTaskNumber = sc.nextInt();
            sc.nextLine();

            if (dependencyTaskNumber != dependentTaskNumber) {
                if (dependencyTaskNumber >= 1 && dependencyTaskNumber <= taskArrayList.size()) {
                    Task dependentTask = taskArrayList.get(dependentTaskNumber - 1);
                    Task dependencyTask = taskArrayList.get(dependencyTaskNumber - 1);

                    boolean dependEachOther = false;
                    boolean alreadyDepend = false;

                    if (dependencyTask.getDependencyArrayList()!=null && !dependencyTask.getDependencyArrayList().isEmpty()) {
                        for (Task checkDependent : dependencyTask.getDependencyArrayList()) {
                            if (checkDependent == dependentTask) {
                                dependEachOther = true;
                            }
                        }
                    }

                    if (dependentTask.getDependencyArrayList()!=null && !dependentTask.getDependencyArrayList().isEmpty()) {
                        for (Task checkDependency : dependentTask.getDependencyArrayList()) {
                            if (checkDependency == dependencyTask) {
                                alreadyDepend = true;
                            }
                        }
                    }

                    if (!alreadyDepend && !dependEachOther) {
                        dependentTask.setDependency(dependencyTask);
                        System.out.println("Task \"" + dependentTask.getTitle() + "\" now depends on \"" + dependencyTask.getTitle() + "\".");
                    } else if (alreadyDepend) {
                        System.out.println("Task \"" + dependentTask.getTitle() + "\" already depends on \"" + dependencyTask.getTitle() + "\".");
                    } else {
                        System.out.println("Tasks cannot depend on each other!");
                    }

                } else {
                    System.out.println("Invalid task number.");
                }
            } else{
                System.out.println("Task cannot depend on itself!");
            }
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void deleteTaskDependency() {
        System.out.println("=== Delete Task Dependency ===");
        System.out.print("Enter the task number for which you want to delete the dependency/ies: ");
        int taskNumber = sc.nextInt();
        sc.nextLine();
        System.out.println();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);

            if (task.getDependencyArrayList() != null) {
                System.out.println("=== Dependency/ies of task \"" + task.getTitle() + "\" ===");
                System.out.println();

                for (int i = 0; i<task.getDependencyArrayList().size(); i++) {
                    System.out.println("Task " + (i + 1) + " : ");
                    System.out.println(task.getDependencyArrayList().get(i));
                    System.out.println();
                }

                System.out.print("Enter the task number of dependency task you want to delete: ");
                int depId = sc.nextInt();
                System.out.println("Dependency task \""+ task.getDependencyArrayList().get(depId-1).getTitle()+"\" for task \"" + task.getTitle() + "\" has been deleted.");
                task.deleteDependency(depId-1);
                sc.nextLine();

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

        System.out.print("Enter the task number you want to edit : ");
        int taskNumber = sc.nextInt();
        sc.nextLine();

        if (taskNumber >= 1 && taskNumber <= taskArrayList.size()) {
            Task task = taskArrayList.get(taskNumber - 1);
            System.out.println("=== Edit Task ===");
            System.out.println();
            System.out.println("1. Title");
            System.out.println("2. Description");
            System.out.println("3. Due Date");
            System.out.println("4. Category");
            System.out.println("5. Priority");
            System.out.println("6. Set Task Dependency");
            System.out.println("7. Cancel");
            System.out.println();
            System.out.print("Choose an option : ");
            int editChoice = sc.nextInt();
            sc.nextLine();

            switch (editChoice) {
                case 1:
                    System.out.print("Enter the new title: ");
                    String newTitle = sc.nextLine();
                    String oldTitle = task.getTitle();
                    task.setTitle(newTitle);
                    System.out.println("Task \"" + oldTitle + "\" has been updated to \"" + newTitle + "\".");
                    break;
                case 2:
                    System.out.print("Enter the new description: ");
                    String newDescription = sc.nextLine();
                    String oldDescription = task.getDescription();
                    task.setDescription(newDescription);
                    System.out.println("Task \"" + oldDescription + "\" has been updated to \"" + newDescription + "\".");
                    break;
                case 3:
                    System.out.print("Enter the new due date (YYYY-MM-DD): ");
                    String newDueDate = sc.nextLine();
                    String oldDueDate = task.getDueDate();
                    task.setDueDate(newDueDate);
                    System.out.println("Task \"" + oldDueDate + "\" has been updated to \"" + newDueDate + "\".");
                    break;
                case 4:
                    System.out.print("Enter the new category: ");
                    String newCategory = sc.nextLine();
                    String oldCategory = task.getCategory();
                    task.setCategory(newCategory);
                    System.out.println("Task \"" + oldCategory + "\" has been updated to \"" + newCategory + "\".");
                    break;
                case 5:
                    System.out.print("Enter the new priority: ");
                    String newPriority = sc.nextLine();
                    String oldPriority = task.getPriority();
                    task.setPriority(newPriority);
                    System.out.println("Task \"" + oldPriority + "\" has been updated to \"" + newPriority + "\".");
                    break;
                case 6:
                    setTaskDependency(taskNumber);
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
            Task removedtask = taskArrayList.get(taskNumber - 1);
            String dependentTasks = "";
            boolean isDependency = false;

            for (int i=0; i<taskArrayList.size(); i++) {
                Task task = taskArrayList.get(i);
                if (task.getDependencyArrayList()!=null) {
                    for (Task checkDependent : task.getDependencyArrayList()) {
                        if (checkDependent == removedtask){
                            isDependency = true;
                            dependentTasks += "\""+task.getTitle()+"\", ";
                        }
                    }
                }
            }

            if(!dependentTasks.isEmpty()){
                if (dependentTasks.charAt(dependentTasks.length()-2)==',') {
                    dependentTasks = dependentTasks.substring(0, dependentTasks.length() - 2);
                }
            }

            if (isDependency) {
                System.out.println("Task \""+removedtask.getTitle()+"\" is dependency task for task(s) "+dependentTasks+".");
                System.out.print("Are you sure you want to delete this task ? (Y/N) : ");
                char choice = sc.next().charAt(0);
                sc.nextLine();

                switch (choice){
                    case 'Y':
                        for (Task task : taskArrayList) {
                            if (task.getDependencyArrayList()!=null) {
                                for (int i=0; i<task.getDependencyArrayList().size(); i++) {
                                    Task checkDependent = task.getDependencyArrayList().get(i);
                                    if (checkDependent == removedtask){
                                        task.deleteDependency(i);
                                    }
                                }
                            }
                        }
                        taskArrayList.remove(taskNumber - 1);
                        System.out.println("Task \"" + removedtask.getTitle() + "\" deleted successfully!");
                        break;
                    case 'N':
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                taskArrayList.remove(taskNumber - 1);
                System.out.println("Task \"" + removedtask.getTitle() + "\" deleted successfully!");
            }
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
            boolean allDependencyCompleted = true;

            if (task.getDependencyArrayList() != null){
                for (Task dependency : task.getDependencyArrayList()){
                    if (!dependency.getStatus()){
                        allDependencyCompleted = false;
                        break;
                    }
                }
            }

            if (!allDependencyCompleted){
                String notCompletedDependency = "";

                for (int i = 0; i<task.getDependencyArrayList().size(); i++) {
                    Task dependency = task.getDependencyArrayList().get(i);
                    if (!dependency.getStatus()){
                        notCompletedDependency += "\"" + dependency.getTitle() + "\"";
                    }
                    if (i + 1 != task.getDependencyArrayList().size()) {
                        notCompletedDependency += ", ";
                    }
                }

                System.out.println("Warning: Task \"" + task.getTitle() + "\" cannot be marked as complete because it depends on " + notCompletedDependency + ". Please complete " + notCompletedDependency + " first.");
            } else {
                task.markAsComplete();
                System.out.println("Task \"" + task.getTitle() + "\" marked as complete!");
            }
        } else {
            System.out.println("Invalid task number.");
        }
        System.out.println();
    }

    /*public void markTaskAsComplete() {
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
    }*/

    /*public void updateTaskDetails() {
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
                    while (!validate.validateDate(newduedate)){
                        System.out.print("Error! Please enter a valid date (YYYY-MM-DD) : ");
                        newduedate = sc.nextLine();
                    }
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
    }*/

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
            if (task.getDependencyArrayList() != null && !task.getDependencyArrayList().isEmpty()) {
                System.out.print(" (Depends on " + task.getDependencyTitles() + ")");
            }
            System.out.println();
        }
        System.out.println();
    }


    /*public void viewTasks() {
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
    }*/

    public void searchTasks() {
        System.out.println("=== Search Tasks ===");
        System.out.println();
        System.out.print("Enter a keyword to search by title or description : ");
        String keyword = sc.nextLine();
        ArrayList<Task> results = new ArrayList<>();
        for (Task task : taskArrayList) {
            if (task.getTitle().toLowerCase().contains(keyword.toLowerCase()) || task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
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

    public void closeTaskManager() {
        sql.clearSQL();

        for (Task task : taskArrayList) {
            if (task.getDependencyArrayList() != null && !task.getDependencyArrayList().isEmpty()) {
                for (Task dependencyTask : task.getDependencyArrayList()) {
                    for (int j = 0; j < taskArrayList.size(); j++) {
                        Task checkDependentId = taskArrayList.get(j);
                        if (checkDependentId == dependencyTask) {
                            task.setDependencyID(j);
                        }
                    }
                }
            }
        }

        for (Task task : taskArrayList) {
            String t = task.getTitle();
            String d = task.getDescription();
            String dd = task.getDueDate();
            String c = task.getCategory();
            String pl = task.getPriority();
            String r = task.getRecurrence();
            Boolean s = task.getStatus();
            Boolean rf = task.getRecurrenceFlag();
            Boolean e = task.getEmailFlag();
            String depID = task.getDependencyID() != null ? task.getDependencyID() : "";
            sql.exportSQL(t, d, dd, c ,pl ,r, s, rf, e, depID);
        }
    }
}
