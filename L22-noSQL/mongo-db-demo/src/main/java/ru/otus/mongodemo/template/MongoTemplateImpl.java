package ru.otus.mongodemo.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@RequiredArgsConstructor
public class MongoTemplateImpl implements MongoTemplate {
    private static final TypeReference<Map<String, Object>> STR_OBJECT_MAP_TYPE_REF = new TypeReference<>() {};

    private final MongoCollection<Document> collection;
    private final ObjectMapper mapper;

    @Override
    public <T> ObjectId insert(T value) {
        val document = new Document(mapper.convertValue(value, STR_OBJECT_MAP_TYPE_REF));
        document.remove("_id");
        collection.insertOne(document);
        return (ObjectId) document.get("_id");
    }

    @Override
    public <T> Optional<T> findOne(ObjectId id, Class<T> tClass) {
        val document = collection.find(eq("_id", id)).first();
        return Optional.ofNullable(document).map(d -> document2Object(d, tClass));
    }

    @Override
    public <T> List<T> find(Bson query, Class<T> tClass) {
        val documents = collection.find(query);

        val res = new ArrayList<T>();
        val cursor = documents.cursor();
        while (cursor.hasNext()) {
            val document = cursor.next();
            res.add(document2Object(document, tClass));
        }
        return res;
    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) throws Exception {
        return find(Document.parse("{}"), tClass);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private <T> T document2Object(Document document, Class<T> tClass) {
        if (tClass.equals(Document.class)) {
            return (T) document;
        }

        val id = document.get("_id");
        document.put("_id", id.toString());
        return mapper.reader().forType(tClass).readValue(document.toJson());
    }
}
