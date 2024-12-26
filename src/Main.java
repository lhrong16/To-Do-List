import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;

class dateCheckThread extends Thread {
    private TaskManager task;
    private Mail mail;

    public dateCheckThread(TaskManager task, Mail mail) {
        this.task = task;
        this.mail = mail;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < task.getTaskArrayList().size(); i++) {
                Task check = task.getTaskArrayList().get(i);

                if (mail.dateCheck(check.getTitle(), check.getDueDate(), check.getEmailFlag())) {
                    check.setEmailFlag();
                }
            }
        }
    }
}


public class Mail {
    private String recipient;

    public void getEmail(String email){
        recipient = email;
    }

    public boolean dateCheck(String t,String dd, boolean e) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date duedate, currentDate;

        try {
            duedate = dateFormat.parse(dd);
            currentDate = dateFormat.parse(LocalDate.now().toString());
            //System.out.println(duedate + " " + currentDate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (duedate.compareTo(currentDate) < 1 && !e) {
            sendMail(recipient,t);
            return true;
        }

        return false;
    }

    public void sendMail(String recipient,String title) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        String myAccountEmail = "yunhao532@gmail.com";
        String password = "hgri cfxn qeie oogo";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        Message message = prepareMessage(session, myAccountEmail, recipient, title);

        try {
            Transport.send(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("=== Email Notification===");
        System.out.println("Sending reminder email for task \""+title+"\" due in 24 hours");
        System.out.println();
    }

    private static Message prepareMessage (Session session, String myAccountEmail, String recipient, String title) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("To-do-List task due in 24 hours");
            message.setText("Reminder! Task \""+title+"\" due in 24 hours!");
            return message;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
