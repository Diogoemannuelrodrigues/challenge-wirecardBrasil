package br.com.github.wirecardBrasil.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "paymentQueue";
    public static final String EMAIL_QUEUE_NAME = "emailQueue";
    public static final String EXCHANGE_NAME = "paymentExchange";
    public static final String CARD_NAME = "cardCreditQueue";

    @Bean
    public Queue paymentQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE_NAME, true);
    }

    @Bean
    public Queue cardCreditQueue() {
        return new Queue(CARD_NAME, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentQueue).to(exchange).with("wirecardbrasil.payment.#");
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with("wirecardbrasil.email.#");
    }

    @Bean
    public Binding cardCreditBinding(Queue emailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with("wirecardbrasil.cardCredit.#");
    }
}

