package ru.otus.messagesystem.message;

import ru.otus.messagesystem.client.ResultDataType;
import java.util.Objects;

class TestData extends ResultDataType {
    int x;
    String str;
    int y;

    TestData(int x, String str, int y) {
        this.x = x;
        this.str = str;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestData testData = (TestData) o;
        return x == testData.x &&
                y == testData.y &&
                Objects.equals(str, testData.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, str, y);
    }

    @Override
    public String toString() {
        return "TestData{" +
                "x=" + x +
                ", str='" + str + '\'' +
                ", y=" + y +
                '}';
    }
}
