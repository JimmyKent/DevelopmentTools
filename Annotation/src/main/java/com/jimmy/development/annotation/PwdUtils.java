package com.jimmy.development.annotation;

import java.util.List;

/**
 * Created by jinguochong on 2017/9/18.
 */

public class PwdUtils {

    @UseCase(id = 47, desc = "pwd must contain at least one numeric")
    public boolean validatePwd(String pwd) {
        return (pwd.matches("\\w*\\d\\w*"));
    }

    @UseCase(id = 48)
    public String encryptPwd(String pwd){
        return new StringBuilder(pwd).reverse().toString();//reverse 反向 ab --> ba
    }

    @UseCase(id = 49, desc = "new pwd cant equal previously used ones")
    public boolean checkPwd(List<String> prePwds, String pwd){
        return prePwds.contains(pwd);
    }
}

