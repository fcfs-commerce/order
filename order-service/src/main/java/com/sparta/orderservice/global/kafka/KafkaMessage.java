package com.sparta.orderservice.global.kafka;

public abstract class KafkaMessage {

  private Long id;

  public KafkaMessage() {}

  public KafkaMessage(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
