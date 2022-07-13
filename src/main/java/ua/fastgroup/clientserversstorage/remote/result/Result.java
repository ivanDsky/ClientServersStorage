package ua.fastgroup.clientserversstorage.remote.result;

public abstract class Result <T>{
    public boolean isSuccess(){
        return this instanceof Success;
    }

    public boolean isError(){
        return this instanceof Error;
    }

    public Success<T> getSuccess(){
        return (Success<T>) this;
    }

    public Error getError(){
        return (Error) this;
    }
}
