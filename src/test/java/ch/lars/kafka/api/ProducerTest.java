package ch.lars.kafka.api;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest()
@EmbeddedKafka(partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:12345",
                "port=12345",
        })
@ActiveProfiles("test")
public class ProducerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerTest.class);
    private static final String TOPIC = "test-message";

    @Autowired
    private Producer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @BeforeEach
    void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        this.embeddedKafkaBroker = embeddedKafkaBroker;
    }

    private Consumer<String, String> setupConsumer() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("test-consumer", "false", embeddedKafkaBroker));
        configs.put("auto.offset.reset", "earliest");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
    }

    @Test
    public void producerTest() throws Exception {
        Consumer<String, String> consumer = setupConsumer();
        consumer.subscribe(Collections.singleton(TOPIC));
        producer.sendMessage("Expected Message.");
        ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        LOGGER.info("[{}]Consumed record from topic [{}], with message [{}]", consumer.groupMetadata().groupId(), singleRecord.topic(), singleRecord.value());
        consumer.close();

        assertThat(singleRecord).isNotNull();
        assertThat(singleRecord.key()).isEqualTo("key");
        assertThat(singleRecord.value()).isEqualTo("Expected Message.");
    }
}
