package com.example.usersgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;

public class LoginController
{
    @FXML public Label loginLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private UsersApp usersApp;
    private String username;
    private String password;
    public static final int n = 5;
    public static final int t = 20;

    public LoginController() {
        usersApp = new UsersApp(n,t);
        usersApp.run();
    }
    @FXML public void onLoginButtonClick() throws IOException {
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty())
            loginLabel.setText("Please enter valid username and password");
        else
        {
            username = usernameField.getText();
            password = passwordField.getText();
            if (!usersApp.authenticateUsername(username)) //username is incorrect
                loginLabel.setText("The username is not registered");
            else if (!usersApp.authenticateUser(username, password)) //password is incorrect
            {
                if(usersApp.getUserIsBlocked(username))
                {
                    long blockageEndTime = usersApp.getUserTimeOfBlock(username) + t*1000L;
                    Instant instant = Instant.ofEpochMilli(blockageEndTime);
                    String formatted = Instant.ofEpochMilli(blockageEndTime).atZone(ZoneId.systemDefault()).toLocalDateTime().toString();
                    loginLabel.setText("Too many tries. User is blocked, try again at " + formatted);
                }
                else
                    loginLabel.setText("The password is incorrect");
            }
            else
            {//password is correct
                if(usersApp.completeAuthentication(username))
                {
                    try {
                        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("Homepage.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage)loginLabel.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Login Successful");
                        stage.show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    long blockageEndTime = usersApp.getUserTimeOfBlock(username) + t*1000L;
                    Instant instant = Instant.ofEpochMilli(blockageEndTime);
                    String formatted = Instant.ofEpochMilli(blockageEndTime).atZone(ZoneId.systemDefault()).toLocalDateTime().toString();
                    loginLabel.setText("Too many tries. User is blocked, try again at " + formatted);
                }
            }
        }
    }
}

