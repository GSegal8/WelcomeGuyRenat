package com.example.usersgui;

public class BlockUserRunnable implements Runnable {
    private User user;
    private int time;
    public BlockUserRunnable(User user, int t)
    {
        this.user = user;
        this.time = t;
    }
    @Override
    public void run() {
        try{
            user.BlockUser();
            Thread.sleep(time * 1000L);
        }
        catch (InterruptedException e) {}
        user.resetState();
    }
}
