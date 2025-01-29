package mr.demonid.service.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Настройка RabbitMQ.
 */
@Configuration
public class RabbitConfig {

    /**
     * Компонент для отправки сообщений в RabbitMQ
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * Создаем обменник.
     */
    @Bean
    public FanoutExchange springCloudBusExample() {
        return new FanoutExchange("springCloudBusPK8000");
    }

    /**
     * Создаем очередь для сообщений.
     */
    @Bean
    public Queue springCloudBusQueue() {
        return new Queue("springCloudBusQueuePK8000");
    }

    /**
     * Связываем очередь сообщений с обменником.
     */
    @Bean
    public Binding binding(FanoutExchange springCloudBusExample, Queue springCloudBusQueue) {
        return BindingBuilder.bind(springCloudBusQueue)
                .to(springCloudBusExample);
    }
}