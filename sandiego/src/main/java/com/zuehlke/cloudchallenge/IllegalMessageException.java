package com.zuehlke.cloudchallenge;

import java.io.IOException;

public class IllegalMessageException extends Exception {
    public IllegalMessageException(IOException e) {
        super(e);
    }
}
