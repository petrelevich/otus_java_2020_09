package ru.otus.mongodemo.template;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface MongoTemplate {
    <T> ObjectId insert(T value);

    <T> Optional<T> findOne(ObjectId id, Class<T> tClass) throws Exception;

    <T> List<T> find(Bson query, Class<T> tClass) throws Exception;
    <T> List<T> findAll(Class<T> tClass) throws Exception;
}
