package com.docky.wiki.util;

import java.io.Serializable;

/**
 * @author Docky
 * @date 2021/4/22 14:10
 */
public class RequestContext  implements Serializable {
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();
    public static String getRemoteAddr(){
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr){
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
