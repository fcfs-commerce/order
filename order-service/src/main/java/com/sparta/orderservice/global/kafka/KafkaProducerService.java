package com.sparta.orderservice.global.kafka;

import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

  @Value("${topic.name.order.create}")
  private String createTopic;

  private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

  public void sendMessageToKafka(OrderCreateRequestDto requestDto) {
    kafkaTemplate.send(createTopic, requestDto);
    log.info("order create message was sent");
  }

}
