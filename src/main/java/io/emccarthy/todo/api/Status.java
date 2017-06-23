package io.emccarthy.todo.api;

public enum Status {

    IN_PROGRESS,
    COMPLETE,
    OVERDUE;

    public static Status fromString(String name) {
        for(Status status : Status.values()){
            if(status.name().equals(name)){
                return status;
            }
        }
        return null;
    }

}
