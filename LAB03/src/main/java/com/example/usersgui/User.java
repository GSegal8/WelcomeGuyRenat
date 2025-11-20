package com.example.usersgui;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class User{
    public static final int MAX_EMAIL_LENGTH    = 50;
    public static final int MAX_PASSWORD_LENGTH = 12;
    public static final int MIN_PASSWORD_LENGTH = 8;

    private final String user_email;
    private final String user_password;

    private int numberOfWrongTries = 0;
    private boolean isBlocked = false;
    private long timeOfBlock = 0;
    private Lock lock = new ReentrantLock();

    public User(String user_email, String user_password){
        checkUsername(user_email);
        checkPassword(user_password);
        this.user_email = user_email;
        this.user_password = user_password;
    }
    public String getName(){
        return this.user_email;
    }
    private void checkUsername(String username) {
        if(username.length()> MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("Username is too long, try something shorter");
        }
        if(!username.contains("@") || !username.contains(".") ) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }
        int atIndex = username.indexOf("@");
        int dotIndex = username.indexOf(".", atIndex + 1);
        String beforeAt = username.substring(0, atIndex);
        String beforeDot = username.substring(atIndex + 1, dotIndex);
        String afterDot = username.substring(dotIndex + 1);
        for(int i = 0; i < beforeAt.length(); i++) {
            if(!Character.isLetterOrDigit(beforeAt.charAt(i)) && beforeAt.charAt(i)!= '-' && beforeAt.charAt(i)!= '+' && beforeAt.charAt(i)!= '%' && beforeAt.charAt(i)!= '_') {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }
        for(int i = 0; i < beforeDot.length(); i++) {
            if(!Character.isLetterOrDigit(beforeDot.charAt(i)) && beforeDot.charAt(i)!= '-' && beforeDot.charAt(i)!= '.') {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }
        int afterDot_lettersCount = 0;
        for(int i = 0; i < afterDot.length(); i++) {
            if(Character.isLetter(afterDot.charAt(i))) {
                afterDot_lettersCount++;
            }
        }
        if(afterDot_lettersCount < 2) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }
    }
    private void checkPassword(String password) {
        if(password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Your password is too short, add more characters");
        }
        if(password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Your password is too long, try a shorter one");
        }
        int lettersCount = 0;
        int signCount = 0;
        int digitCount = 0;
        for(int i = 0; i < password.length(); i++) {
            if(password.charAt(i) >= 65 && password.charAt(i) <= 90 || (password.charAt(i) >= 97 && password.charAt(i) <= 122)) {
                lettersCount++;
            }
            if(password.charAt(i) =='!' || password.charAt(i) =='@' || password.charAt(i) =='#' || password.charAt(i) =='+' ||  password.charAt(i) =='%' || password.charAt(i) =='^' ||  password.charAt(i) =='$' || password.charAt(i) == '&' || password.charAt(i) == '*' ||  password.charAt(i) == '(' || password.charAt(i) == ')') {
                signCount++;
            }
            if(password.charAt(i) >= 48 && password.charAt(i) <= 57) {
                digitCount++;
            }
        }
        if(lettersCount < 1|| signCount < 1 || digitCount < 1) {
            throw new IllegalArgumentException("Please enter a valid password");
        }
    }
    public String toString(){
        return this.user_email + " " + this.user_password;
    }

    public boolean checkPasswordValidity(String pass){
        if(this.user_password.equals(pass))
            return true;
        else {
            return false;
        }
    }

    public void WrongTriesAddition(){
        lock.lock();
        try{
            numberOfWrongTries++;
        }
        finally {
            lock.unlock();
        }
    }

    public int getNumberOfWrongTries() {
        lock.lock();
        try{
            return numberOfWrongTries;
        }
        finally {
            lock.unlock();
        }
    }

    public boolean getIsBlocked() {
        lock.lock();
        boolean blocked;
        try{
            blocked = this.isBlocked;
        }
        finally {
            lock.unlock();
        }
        return blocked;
    }

    public void BlockUser() {
        lock.lock();
        try{
            this.isBlocked = true;
            this.timeOfBlock = System.currentTimeMillis();
        }
        finally {
            lock.unlock();
        }
    }

    public void resetState() {
        lock.lock();
        try{
            this.isBlocked = false;
            this.numberOfWrongTries = 0;
            this.timeOfBlock = 0;
        }
        finally{
            lock.unlock();
        }
    }

    public long getTimeOfBlock() {
        lock.lock();
        try{
            return timeOfBlock;
        }
        finally{
            lock.unlock();
        }
    }
}
