
package com.project.common.exception;


public class ServerException extends RuntimeException{
    private static final long serialVersionUID = -8964911162183816298L;

    public ServerException() {
        super();
    }
    public ServerException(String msg) {
        super(msg);
    }
    public ServerException(Exception e){
        super(e);
    }
    public ServerException(String message, Throwable arg) {
        super(message, arg);
    }
}

