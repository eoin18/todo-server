package io.emccarthy.todo.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.joda.time.LocalDate;

public class ToDoItem implements Cloneable{

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Length
    private long _id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status = Status.IN_PROGRESS;

    public ToDoItem(){}

    public ToDoItem(long _id, String title, String description, LocalDate dueDate, Status status){
        this._id = _id;
        this.title = Preconditions.checkNotNull(title, "All todo items must have a title");
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    @JsonProperty
    public long get_id() {
        return this._id;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    @JsonFormat(pattern=DATE_FORMAT)
    public LocalDate getDueDate() {
        return dueDate;
    }

    @JsonProperty
    public Status getStatus() {
        return this.status;
    }

    @JsonProperty
    public void setStatus(Status status) {
        if(status != null) {
            this.status = status;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ToDoItem) {
            if(this == obj) {
                return true;
            }
            ToDoItem other = (ToDoItem) obj;
            return new EqualsBuilder().append(this._id, other._id)
                    .append(this.title, other.title)
                    .append(this.description, other.description)
                    .append(this.dueDate, other.dueDate)
                    .append(this.status, other.status)
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(get_id())
                .append(getTitle())
                .append(getDescription())
                .append(getDueDate())
                .append(getStatus())
                .toHashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ToDoItem clone = (ToDoItem) super.clone();
        clone._id = this._id;
        clone.title = this.title;
        clone.description = this.description;
        clone.dueDate = LocalDate.fromDateFields(this.dueDate.toDate());
        clone.setStatus(this.status);
        return clone;
    }

    @Override
    public String toString() {
       return new StringBuilder()
               .append("To Do Item: ")
               .append("\tID: [").append(this._id).append("]\n")
               .append("\tTitle: [").append(this.title).append("]\n")
               .append("\tDescription: [").append(this.description).append("]\n")
               .append("\tDue Date: [").append(this.dueDate.toString()).append("]\n")
               .append("\tStatus: [").append(this.status).append("]\n")
               .toString();
    }
}
