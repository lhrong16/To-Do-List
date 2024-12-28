import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private TaskManager taskManager;
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    public GUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskList);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = createButton("Add Task", "add.png");
        JButton viewButton = createButton("View Tasks", "view.png");
        JButton sortDueDateAscButton = createButton("Sort Due Date Asc", "sort_asc.png");
        JButton sortDueDateDescButton = createButton("Sort Due Date Desc", "sort_desc.png");
        JButton markCompleteButton = createButton("Mark Complete", "complete.png");
        JButton deleteButton = createButton("Delete Task", "delete.png");
        JButton saveButton = createButton("Save & Close", "save.png");

        panel.add(addButton);
        panel.add(viewButton);
        panel.add(sortDueDateAscButton);
        panel.add(sortDueDateDescButton);
        panel.add(markCompleteButton);
        panel.add(deleteButton);
        panel.add(saveButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTasks();
            }
        });

        sortDueDateAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTasksByDueDate(true);
            }
        });

        sortDueDateDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTasksByDueDate(false);
            }
        });

        markCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markTaskAsComplete();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAndClose();
            }
        });

        frame.setVisible(true);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setIcon(new ImageIcon(iconPath));
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void addTask() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dueDateField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priorityField = new JTextField();

        panel.add(new JLabel("Enter task title:"));
        panel.add(titleField);
        panel.add(new JLabel("Enter task description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Enter due date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Enter task category (Homework, Personal, Work):"));
        panel.add(categoryField);
        panel.add(new JLabel("Enter priority level (Low, Medium, High):"));
        panel.add(priorityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String dueDate = dueDateField.getText();
            String category = categoryField.getText();
            String priority = priorityField.getText();

            taskManager.addTask(title, description, dueDate, category, priority);
            viewTasks();
        }
    }

    private void viewTasks() {
        taskListModel.clear();
        for (Task task : taskManager.getTaskArrayList()) {
            taskListModel.addElement(task.toFormattedString());
        }
    }

    private void sortTasksByDueDate(boolean ascending) {
        taskManager.sortTasks(ascending);
        viewTasks();
    }

    private void markTaskAsComplete() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskManager.getTaskArrayList().get(selectedIndex);
            boolean allDependenciesCompleted = true;

            if (task.getDependencyArrayList() != null) {
                for (Task dependency : task.getDependencyArrayList()) {
                    if (!dependency.getStatus()) {
                        allDependenciesCompleted = false;
                        break;
                    }
                }
            }

            if (allDependenciesCompleted) {
                taskManager.markTaskAsComplete(selectedIndex);
                viewTasks();
            } else {
                JOptionPane.showMessageDialog(frame, "This task cannot be marked as complete because it depends on incomplete tasks.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to mark as complete.");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskManager.deleteTask(selectedIndex);
            viewTasks();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to delete.");
        }
    }

    private void saveAndClose() {
        taskManager.closeTaskManager();
        System.exit(0);
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.importSQL();
        taskManager.importDependencies();
        new GUI(taskManager);
    }
}
