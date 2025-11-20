package com.example.usersgui;

public class CheckBlockedRunnable implements Runnable{
    private User user;
    private int time;

    public CheckBlockedRunnable(User user, int t) {
        this.user = user;
        this.time = t;
    }
    @Override
    public void run() {
        if (!user.getIsBlocked()) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if((currentTime - user.getTimeOfBlock()) >= this.time * 1000L)
        {
            user.resetState();
        }
    }
}
