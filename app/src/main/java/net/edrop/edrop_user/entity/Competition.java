package net.edrop.edrop_user.entity;

import java.io.Serializable;

public class Competition implements Serializable {
    private int id;
    private String question;
    private int typeId;
    private Type type;

    public Competition(int id, String question, int typeId, Type type) {
        this.id = id;
        this.question = question;
        this.typeId = typeId;
        this.type = type;
    }

    public Competition() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", typeId=" + typeId +
                ", type=" + type +
                '}';
    }
}