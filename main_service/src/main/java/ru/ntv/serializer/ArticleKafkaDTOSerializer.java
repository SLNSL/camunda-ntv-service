package ru.ntv.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.ntv.dto.kafka.ArticleKafkaDTO;

public class ArticleKafkaDTOSerializer implements Serializer<ArticleKafkaDTO> {
    @Override
    public byte[] serialize(String topic, ArticleKafkaDTO data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }
}