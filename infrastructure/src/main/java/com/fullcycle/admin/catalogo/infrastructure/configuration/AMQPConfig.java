package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEvents;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

  @Bean()
  @VideoCreatedQueue
  @ConfigurationProperties(prefix = "amqp.queues.video-created")
  public QueueProperties videoCreatedQueueProperties(){
    return new QueueProperties();
  }

  @Bean("videoEncodedQueueProperties")
  @VideoEncodedQueue
  @ConfigurationProperties(prefix = "amqp.queues.video-encoded")
  public QueueProperties videoEncodedQueueProperties(){
    return new QueueProperties();
  }

  @Configuration
  static class RabbitMQAdmin {

    @Bean
    @VideoEvents
    public Exchange videoEventsExchange(@VideoCreatedQueue QueueProperties queueProperties){
      return new DirectExchange(queueProperties.getExchange());
    }

    @Bean
    @VideoCreatedQueue
    public Queue videoCreatedQueue(@VideoCreatedQueue QueueProperties queueProperties){
      return new Queue(queueProperties.getQueue(), true);
    }

    @Bean
    @VideoCreatedQueue
    public Binding videoCreatedBinding(@VideoCreatedQueue QueueProperties queueProperties,
                                       @VideoCreatedQueue Queue videoCreatedQueue,
                                       @VideoEvents DirectExchange exchange){
      return BindingBuilder
          .bind(videoCreatedQueue)
          .to(exchange)
          .with(queueProperties.getRoutingKey());
    }

    @Bean
    @VideoEncodedQueue
    public Binding videoEncodedBinding(@VideoEncodedQueue QueueProperties queueProperties,
        @VideoEncodedQueue Queue videoEncodedQueue,
        @VideoEvents DirectExchange exchange){
      return BindingBuilder
          .bind(videoEncodedQueue)
          .to(exchange)
          .with(queueProperties.getRoutingKey());
    }

    @Bean
    @VideoEncodedQueue
    public Queue videoEncodedQueue(@VideoEncodedQueue QueueProperties queueProperties){
      return new Queue(queueProperties.getQueue(), true);
    }

  }

}
