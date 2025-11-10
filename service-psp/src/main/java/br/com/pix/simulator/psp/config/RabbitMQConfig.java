package br.com.pix.simulator.psp.config;

import lombok.Generated;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Generated
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "pix-events-exchange";

    public static final String QUEUE_REQUESTED_DEBIT = "queue-requested-debit";
    public static final String QUEUE_REQUESTED_CREDIT = "queue-requested-credit";

    public static final String RK_REQUESTED_DEBIT = "rk-requested-debit";
    public static final String RK_REQUESTED_CREDIT = "rk-requested-credit";

    public static final String RK_DEBIT_MADE = "rk-debit-made";
    public static final String RK_DEBIT_FAILED = "rk-debit-failed";
    public static final String RK_CREDIT_MADE = "rk-credit-made";
    public static final String RK_CREDIT_FAILED = "rk-credit-failed";


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue requestedDebitQueue() {
        return new Queue(QUEUE_REQUESTED_DEBIT);
    }

    @Bean
    public Queue requestedCreditQueue() {
        return new Queue(QUEUE_REQUESTED_CREDIT);
    }

    @Bean
    public Binding debitBinding(TopicExchange exchange, Queue requestedDebitQueue) {
        return BindingBuilder.bind(requestedDebitQueue).to(exchange).with(RK_REQUESTED_DEBIT);
    }

    @Bean
    public Binding creditBinding(TopicExchange exchange, Queue requestedCreditQueue) {
        return BindingBuilder.bind(requestedCreditQueue).to(exchange).with(RK_REQUESTED_CREDIT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
