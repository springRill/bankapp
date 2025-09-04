package com.exchange.kafka;

import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = {"exchange"})
public class КafkaTest {

    @Autowired
    private KafkaTemplate<String, ExchangeDto> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testExchange() {
        ExchangeDto exchangeDto = new ExchangeDto(CurrencyEnum.RUB, 1D);
        try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
                KafkaTestUtils.consumerProps("exchange-group", "true", embeddedKafkaBroker),
                new StringDeserializer(),
                new JsonDeserializer<>(ExchangeDto.class, false)
        ).createConsumer()) {
            consumerForTest.subscribe(List.of("exchange"));

            kafkaTemplate.send("exchange", exchangeDto.getCurrency().name(), exchangeDto);

            var inputMessage = KafkaTestUtils.getSingleRecord(consumerForTest, "exchange", Duration.ofSeconds(5));
            assertThat(inputMessage.key()).isEqualTo(exchangeDto.getCurrency().name());
            assertThat(inputMessage.value()).isEqualTo(exchangeDto);
        }
    }

    @TestConfiguration
    public static class KafkaTestConfig {
        @Bean
        public KafkaTemplate<String, ExchangeDto> kafkaTemplate(EmbeddedKafkaBroker embeddedKafkaBroker) {
            Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
            producerProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
            producerProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
            return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
        }
    }

}
