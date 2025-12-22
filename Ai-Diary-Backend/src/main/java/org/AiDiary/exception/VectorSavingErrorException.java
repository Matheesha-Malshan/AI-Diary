package org.AiDiary.exception;

public class VectorSavingErrorException extends RuntimeException{

    public VectorSavingErrorException(){
        super("error on creating vector saving");
    }
}
