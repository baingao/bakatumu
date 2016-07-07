package com.bakatumu.bakatumu.app;

/**
 * Created by Lincoln on 06/01/16.
 */
public class EndPoints {

    // localhost url
    // public static final String BASE_URL = "http://192.168.0.101/gcm_chat/v1";

    public static final String BASE_URL = "http://103.240.110.254/gcm_chat/v1";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String USERS = BASE_URL + "/allusers";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
    public static final String PM_THREAD = BASE_URL + "/pm/_ID_";
    public static final String PRIVATE_MESSAGE = BASE_URL + "/pm/_ID_/message";
    public static final String USER_MESSAGE = BASE_URL + "/user/_ID_/message";
}
