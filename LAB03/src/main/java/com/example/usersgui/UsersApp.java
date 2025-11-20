package com.example.usersgui;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import com.example.usersgui.User;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class UsersApp
{
    private int numberOfTriesAllowed;
    private int timeBlocked;
    private ArrayList<User> users;

    public UsersApp(int n, int t){
        this.users = new ArrayList<>();
        this.numberOfTriesAllowed = n;
        this.timeBlocked = t;
    }

    public void run(){
        /* reading from users.txt file */
        try {
            File readFile = new File("Users.txt");
            Scanner Reader = new Scanner(readFile);
            while(Reader.hasNextLine()) {
                String line = Reader.nextLine();
                int subspaceIndex = line.indexOf(" ");
                if(subspaceIndex == -1) {throw new IllegalArgumentException("Please enter a valid Email as username");}
                String tempUserEmail = line.substring(0, subspaceIndex);
                String tempUserPassword = line.substring(subspaceIndex + 2); //two subspaces divides between the mail and the password
                try{
                    User newUser = new User(tempUserEmail, tempUserPassword);
                    users.add(newUser);
                }
                catch(IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (FileNotFoundException e) {throw new RuntimeException(e);}
        Collections.sort(users, (u1, u2) -> u1.getName().compareTo(u2.getName()));
        try{
            FileWriter Writer = new FileWriter("out.txt");
            for (User temp : users) {
                String stream = temp.toString();
                Writer.write(stream+"\n");
            }
            Writer.close();
        }
        catch (Exception e) {throw new RuntimeException(e);}
    }

    public boolean authenticateUsername(String name) {
        for(User temp : users)
        {
            if(temp.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public boolean authenticateUser(String email, String password){

        for(User temp : users)
        {
            if(temp.getName().equals(email))
                if(temp.checkPasswordValidity(password)) {
                   return true;
                }
                else
                {
                    temp.WrongTriesAddition();
                    if(temp.getNumberOfWrongTries()>=numberOfTriesAllowed) {
                        Thread t = new Thread(new BlockUserRunnable(temp, timeBlocked));
                        t.start();
                    }
                    return false;
                }
        }
        return false;
    }

    public boolean completeAuthentication(String email){ //check if blocked
        User temp = getUser(email);
        if(temp == null) return false;
        if(getUserIsBlocked(email))
        {
            Thread t = new Thread(new CheckBlockedRunnable(temp, timeBlocked));
            t.start();
            return false;
        }
        temp.resetState();
        return true;
    }

    private User getUser(String email){
        for(User temp : users)
        {
            if(temp.getName().equals(email))
                return temp;
        }
        return null;
    }

    public long getUserTimeOfBlock(String user)
    {
        for(User temp : users)
        {
            if(temp.getName().equals(user))
            {
                return temp.getTimeOfBlock();
            }
        }
        return 0;
    }

    public boolean getUserIsBlocked(String user)
    {
        for(User temp : users)
        {
            if(temp.getName().equals(user))
            {
                return temp.getIsBlocked();
            }
        }
        return false;
    }
}


