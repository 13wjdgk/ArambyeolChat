package Arambyeol.chat.global.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableRabbit
public class RabbitConfig {

	private static final String CHAT_QUEUE_NAME = "chat.queue";
	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "type.*";

	@Bean
	public Queue queue() {
		return new Queue(CHAT_QUEUE_NAME, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(CHAT_EXCHANGE_NAME);
	}


	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
	}



	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
		return rabbitTemplate;
	}

	@Bean
	public SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(CHAT_QUEUE_NAME);
		container.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				System.out.println("message = " + message);
			}
		});
		return container;
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}