package ru.otus.mongodemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmartPhone {
    private ObjectId _id;
    private String model;
    private String color;
    private String serialNumber;

    private String operatingSystem;
}
