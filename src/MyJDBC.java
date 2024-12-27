import java.sql.*;

public class MyJDBC {
    public void importSQL(TaskManager task) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sql_schema",
                    "root",
                    "54321"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sql_schema.list");

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String duedate = resultSet.getString("duedate");
                String category = resultSet.getString("category");
                String priority = resultSet.getString("priority");
                String recurrence = resultSet.getString("recurrence");
                boolean status = resultSet.getBoolean("status");
                boolean recurrenceFlag = resultSet.getBoolean("recurrenceFlag");
                boolean emailFlag = resultSet.getBoolean("emailFlag");
                String dependencyID = resultSet.getString("depID");
                task.importTasks(title, description, duedate, category, priority, recurrence, status, recurrenceFlag, emailFlag, dependencyID);

            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void exportSQL(String t, String d, String dd, String c, String pl, String r, boolean s, boolean rf, boolean e, String depID) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sql_schema",
                    "root",
                    "54321"
            );

            Statement statement = connection.createStatement();

            int sInt = s ? 1 : 0;
            int rfInt = rf ? 1 : 0;
            int eInt = e ? 1 : 0;

            statement.executeUpdate("insert into sql_schema.list" + "(title,description,duedate,category,priority,recurrence,status,recurrenceFlag,emailFlag,depID)" + "values('"+t+"','"+d+"','"+dd+"','"+c+"','"+pl+"','"+r+"','"+sInt+"','"+rfInt+"','"+eInt+"','"+depID+"')");
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void clearSQL () {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sql_schema",
                    "root",
                    "54321"
            );

            Statement statement = connection.createStatement();

            statement.executeUpdate("TRUNCATE sql_schema.list");
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
