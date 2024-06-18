package com.simpleboard.userservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {
//    스레드 변수 공유를 막기 위해 동기화를 사용
//    동기화를 피하고 싶을 때 ThreadLocal 변수를 사용
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static final UserContext getContext() {
        UserContext context = userContext.get();

        if(context == null){
            context = createEmptyContext();

            userContext.set(context);
        }

        return userContext.get();
    }

    public static final void setContext(UserContext context){
        Assert.notNull(context, "Only non-null UserContext instances are permitted");

        userContext.set(context);
    }

    public static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
