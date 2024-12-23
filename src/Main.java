import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager task = new TaskManager();
        Scanner sc = new Scanner(System.in);
        task.importSQL();

        boolean running = true;
        while (running) {
            System.out.print("""
                             === Task Manager ===
                             
                             1. Add a New Task
                             2. Mark Task as Complete
                             3. Update Task Details
                             4. Delete a Task
                             5. View All Tasks
                             6. Search Tasks
                             7. Set Task Dependency
                             8. Edit Task
                             9. Save and Close
                             
                             """);
            System.out.print("Choose an option : ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1 :
                    System.out.print("""
                                     === Add a New Task ===
                                     
                                     1. Not recurring task
                                     2. Recurring task
                                     
                                     """);
                    System.out.print("Choose an option : ");

                    choice = sc.nextInt();
                    sc.nextLine();
                    System.out.println();

                    switch (choice) {
                        case 1 :
                            task.addTask();
                            break;
                        case 2:
                            task.addRecurringTask();
                            break;
                    }

                    break;
                case 2 :
                    task.markTaskAsComplete();
                    break;
                case 3 :
                    task.updateTaskDetails();
                    break;
                case 4 :
                    task.deleteTask();
                    break;
                case 5 :
                    System.out.print("""
                                     === View All Tasks ===
                                     
                                     1. Without sorting
                                     2. Sorting
                                     
                                     """);
                    System.out.print("Choose an option : ");

                    choice = sc.nextInt();
                    sc.nextLine();
                    System.out.println();

                    switch (choice) {
                        case 1 :
                            task.viewTasks();
                            break;
                        case 2:
                            task.sortTasks();
                            task.viewTasks();
                            break;
                    }
                    break;
                case 6 :
                    task.searchTasks();
                    break;
                case 7 :
                    task.setTaskDependency();
                    break;
                case 8 :
                    task.editTask();
                    break;
                case 9:
                    task.closeTaskManager();
                    System.exit(0);
                default :
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println();
    }
}


