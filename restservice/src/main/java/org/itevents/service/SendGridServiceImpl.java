package org.itevents.service;
import com.sendgrid.*;
import org.itevents.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by max on 29.07.15.
 */
public class SendGridServiceImpl implements SendGridService {
    private String api_key;
    FileInputStream fis;
    Properties sendGridProperty = new Properties();



    public void sendMail(String message, User user){

        try {
            fis = new FileInputStream("src/main/resources/sendgrid.properties");
            sendGridProperty.load(fis);
            api_key = sendGridProperty.getProperty("api_key");
        } catch (IOException e) {
            System.err.println("ERROR: File sendgrid.properties not found.");
        }

        SendGrid sendgrid = new SendGrid(api_key);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(user.getLogin());
        email.setFrom("ultimax@ukr.net");
        email.setSubject("IT Events of the week");
        email.setText(message);

        try {
            SendGrid.Response response = sendgrid.send(email);
            System.out.println(response.getMessage());
        } catch (SendGridException e) {
            System.err.println(e);
        }
    }
}
