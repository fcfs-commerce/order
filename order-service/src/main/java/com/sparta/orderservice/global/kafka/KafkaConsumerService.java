package com.sparta.orderservice.global.kafka;

import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import com.sparta.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final OrderService orderService;

  @KafkaListener(topics = "order-create", containerFactory = "orderCreateKafkaListenerContainerFactory")
  public void consume(KafkaMessage message) {
    log.info("order create message consumed");
    OrderCreateRequestDto requestDto = (OrderCreateRequestDto) message;
    orderService.createOrder(requestDto.getId(), requestDto);
  }

}
