import java.sql.*;

public class MyJDBC {
    public void importSQL(TaskManager task) {
        try {
            Connection connection = DriverManager.getConnection(
                    //change address and password to own
                    "jdbc:mysql://127.0.0.1:3306/list_schema",
                    "root",
                    "112233445566"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM list_schema.list");

                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String duedate = resultSet.getString("duedate");
                    String category = resultSet.getString("category");
                    String priority = resultSet.getString("priority");
                    String recurrence = resultSet.getString("recurrence");
                    boolean status = resultSet.getBoolean("status");
                    boolean recurrenceFlag = resultSet.getBoolean("recurrenceFlag");
                    task.importTasks(title, description, duedate, category, priority, recurrence, status, recurrenceFlag);


                    /*System.out.println("Task "+resultSet.getString("idlist")+" : ");
                    System.out.println("Title : "+resultSet.getString("title"));
                    System.out.println("Description : "+resultSet.getString("description"));
                    System.out.println("Due Date : "+resultSet.getString("duedate"));
                    System.out.println("Status : "+resultSet.getBoolean("status"));
                    System.out.println("Category : "+resultSet.getString("category"));
                    System.out.println("recurrence : "+resultSet.getString("recurrence"));
                    System.out.println("Priority Level : "+resultSet.getString("priority"));
                    System.out.println("recurrenceFlag : "+resultSet.getBoolean("recurrenceFlag"));
                    System.out.println();*/
                }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportSQL(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf){
        try {
            Connection connection = DriverManager.getConnection(
                    //change address and password to own
                    "jdbc:mysql://127.0.0.1:3306/list_schema",
                    "root",
                    "112233445566"
            );

            Statement statement = connection.createStatement();

            int sInt = s ? 1 : 0;
            int rfInt = rf ? 1 : 0;

            statement.executeUpdate("insert into list_schema.list" + "(title,description,duedate,category,priority,recurrence,status,recurrenceFlag)" + "values('"+t+"','"+d+"','"+dd+"','"+c+"','"+pl+"','"+r+"','"+sInt+"','"+rfInt+"')");
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void clearSQL () {
            try {
                Connection connection = DriverManager.getConnection(
                        //change address and password to own
                        "jdbc:mysql://127.0.0.1:3306/list_schema",
                        "root",
                        "112233445566"
                );

                Statement statement = connection.createStatement();

                statement.executeUpdate("TRUNCATE list_schema.list");
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

}
