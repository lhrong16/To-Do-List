import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//
public class ValidateInput {
    public boolean validateDate (String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date check = null;
        dateFormat.setLenient(false);

        try{
            check = dateFormat.parse(date);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean validatePriority (String priority){
        return priority.equalsIgnoreCase("High") || priority.equalsIgnoreCase("Medium") || priority.equalsIgnoreCase("Low");
    }

    public boolean validateCategory(String category) {
        return category.equalsIgnoreCase("Homework") || category.equalsIgnoreCase("Personal") || category.equalsIgnoreCase("Work");
    }
}
