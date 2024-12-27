import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager task = new TaskManager();
        Mail mail = new Mail();
        Scanner sc = new Scanner(System.in);
        DataAnalytics analytics = new DataAnalytics(task);
        dateCheckThread dateCheck = new dateCheckThread(task, mail);

        task.importSQL();
        task.importDependencies();

        boolean running = true;
        while (running) {
            System.out.print("""
                             === Task Manager ===
                             
                             1. Add a New Task
                             2. Edit Task
                             3. Delete a Task
                             4. Task Dependency
                             5. Mark Task as Complete
                             6. View All Tasks
                             7. Search Tasks
                             8. Show Analytics Dashboard
                             9. Connect with Email
                             10. Save and Close
                             
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
                    task.editTask();
                    break;
                case 3 :
                    task.deleteTask();
                    break;
                case 4 :
                    System.out.print("""
                                     === Task Dependency ===
                                     
                                     1. Set dependency
                                     2. Delete dependency
                                     
                                     """);
                    System.out.print("Choose an option : ");
                    choice = sc.nextInt();
                    System.out.println();
                    sc.nextLine();

                    switch (choice) {
                        case 1 :
                            task.setTaskDependency();
                            break;
                        case 2:
                            task.deleteTaskDependency();
                            break;
                    }
                    break;
                case 5 :
                    task.markTaskAsComplete();
                    break;
                case 6 :
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
                case 7 :
                    task.searchTasks();
                    break;
                case 8 :
                    analytics.showAnalyticsDashboard();
                    break;
                case 9 :
                    System.out.print("Enter Email Address : ");
                    String email = sc.nextLine();
                    mail.getEmail(email);
                    dateCheck.start();
                    break;
                case 10 :
                    task.closeTaskManager();
                    System.exit(0);
                default :
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println();
    }

}
