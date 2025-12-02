package com.zullo.christmas.util;

import com.zullo.christmas.constants.ApplicationConstants;
import com.zullo.christmas.model.database.User;

public class CommonUtils {
    
    public static boolean isOwnerOrAdmin(User user, Integer owner){
        return user.getRole().equals(ApplicationConstants.ADMIN) || user.getId() == owner;
    }

}
