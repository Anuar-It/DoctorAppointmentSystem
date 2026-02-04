package com.company.session;

import com.company.entity.User;

public class Session {
    private User currentUser;

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User u) { this.currentUser = u; }
    public void clear() { this.currentUser = null; }
}
