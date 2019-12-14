package com.abedo.firebasepushnotifications;

import androidx.annotation.NonNull;

/**
 * created by Abedo95 on 11/17/2019
 */
public class UserId {

    public String userId;

    public <T extends UserId> T withId(@NonNull final String id)
    {
        this.userId=id;
        return (T)this;
    }
}
