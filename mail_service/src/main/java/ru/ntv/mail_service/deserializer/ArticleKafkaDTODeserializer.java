package ru.ntv.mail_service.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.ntv.mail_service.dto.kafka.ArticleKafkaDTO;

public class ArticleKafkaDTODeserializer implements Deserializer<ArticleKafkaDTO> {
    @Override
    public ArticleKafkaDTO deserialize(String topic, byte[] data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, ArticleKafkaDTO.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing JSON message", e);
        }
    }
}