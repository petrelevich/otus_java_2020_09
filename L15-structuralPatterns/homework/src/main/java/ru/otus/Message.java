package ru.otus;

import java.util.Objects;

public class Message {
    private final String field1;
    private final String field2;
    private final String field3;
    private final String field4;
    private final String field5;
    private final String field6;
    private final String field7;
    private final String field8;
    private final String field9;
    private final String field10;

    //todo: 1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)

    private Message(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.field5 = field5;
        this.field6 = field6;
        this.field7 = field7;
        this.field8 = field8;
        this.field9 = field9;
        this.field10 = field10;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    public String getField4() {
        return field4;
    }

    public String getField5() {
        return field5;
    }

    public String getField6() {
        return field6;
    }

    public String getField7() {
        return field7;
    }

    public String getField8() {
        return field8;
    }

    public String getField9() {
        return field9;
    }

    public String getField10() {
        return field10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!Objects.equals(field1, message.field1)) return false;
        if (!Objects.equals(field2, message.field2)) return false;
        if (!Objects.equals(field3, message.field3)) return false;
        if (!Objects.equals(field4, message.field4)) return false;
        if (!Objects.equals(field5, message.field5)) return false;
        if (!Objects.equals(field6, message.field6)) return false;
        if (!Objects.equals(field7, message.field7)) return false;
        if (!Objects.equals(field8, message.field8)) return false;
        if (!Objects.equals(field9, message.field9)) return false;
        return Objects.equals(field10, message.field10);
    }

    @Override
    public int hashCode() {
        int result = field1 != null ? field1.hashCode() : 0;
        result = 31 * result + (field2 != null ? field2.hashCode() : 0);
        result = 31 * result + (field3 != null ? field3.hashCode() : 0);
        result = 31 * result + (field4 != null ? field4.hashCode() : 0);
        result = 31 * result + (field5 != null ? field5.hashCode() : 0);
        result = 31 * result + (field6 != null ? field6.hashCode() : 0);
        result = 31 * result + (field7 != null ? field7.hashCode() : 0);
        result = 31 * result + (field8 != null ? field8.hashCode() : 0);
        result = 31 * result + (field9 != null ? field9.hashCode() : 0);
        result = 31 * result + (field10 != null ? field10.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", field4='" + field4 + '\'' +
                ", field5='" + field5 + '\'' +
                ", field6='" + field6 + '\'' +
                ", field7='" + field7 + '\'' +
                ", field8='" + field8 + '\'' +
                ", field9='" + field9 + '\'' +
                ", field10='" + field10 + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    public static class Builder {
        private String field1;
        private String field2;
        private String field3;
        private String field4;
        private String field5;
        private String field6;
        private String field7;
        private String field8;
        private String field9;
        private String field10;

        public Builder() {
        }

        private Builder(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
            this.field5 = field5;
            this.field6 = field6;
            this.field7 = field7;
            this.field8 = field8;
            this.field9 = field9;
            this.field10 = field10;
        }

        public Builder field1(String field1) {
            this.field1 = field1;
            return this;
        }

        public Builder field2(String field2) {
            this.field2 = field2;
            return this;
        }

        public Builder field3(String field3) {
            this.field3 = field3;
            return this;
        }

        public Builder field4(String field4) {
            this.field4 = field4;
            return this;
        }

        public Builder field5(String field5) {
            this.field5 = field5;
            return this;
        }

        public Builder field6(String field6) {
            this.field6 = field6;
            return this;
        }

        public Builder field7(String field7) {
            this.field7 = field7;
            return this;
        }

        public Builder field8(String field8) {
            this.field8 = field8;
            return this;
        }

        public Builder field9(String field9) {
            this.field9 = field9;
            return this;
        }

        public Builder field10(String field10) {
            this.field10 = field10;
            return this;
        }

        public Message build() {
            return new Message(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
        }
    }
}
