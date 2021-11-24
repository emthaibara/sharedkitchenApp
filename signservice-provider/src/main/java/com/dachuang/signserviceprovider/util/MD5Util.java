package com.dachuang.signserviceprovider.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/15
 */
public class MD5Util {
    
    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);
    
    private static final String PASSWORDSALT = "1b2c3d4e";

    public static String formPassToDBPass(String formPass) {
        String str = ""+PASSWORDSALT.charAt(0)+PASSWORDSALT.charAt(2) + formPass +PASSWORDSALT.charAt(5) + PASSWORDSALT.charAt(4);
        return md5(str);
    }

    private static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
}
