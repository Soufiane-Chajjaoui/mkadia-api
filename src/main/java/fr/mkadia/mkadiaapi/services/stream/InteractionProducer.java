package fr.mkadia.mkadiaapi.services.stream;

import fr.mkadia.mkadiaapi.enums.InteractionTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InteractionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(InteractionTopic topic, Object payload) {
        kafkaTemplate.send(topic.value(), payload);
        System.out.println(STR."\uD83D\uDCE4 Sent to [\{topic.value()}]: \{payload}");
    }
}
