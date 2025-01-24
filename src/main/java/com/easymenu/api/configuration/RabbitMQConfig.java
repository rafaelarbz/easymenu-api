package com.easymenu.api.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_ORDERS = "orders.exchange";
    public static final String QUEUE_ORDERS_CREATED = "orders.created.queue";
    public static final String QUEUE_ORDERS_UPDATED = "orders.updated.queue";
    public static final String ROUTING_KEY_ORDERS_CREATED = "orders.created";
    public static final String ROUTING_KEY_ORDERS_UPDATED = "orders.updated";

    public static final String EXCHANGE_MENU_ITEMS = "menu.items.exchange";
    public static final String QUEUE_MENU_ITEMS_UPDATED = "menu.items.updated.queue";
    public static final String ROUTING_KEY_MENU_ITEMS_UPDATED = "menu.items.updated";


    public static final String EXCHANGE_TABLES = "tables.exchange";
    public static final String QUEUE_TABLES_UPDATED = "tables.updated.queue";
    public static final String ROUTING_KEY_TABLES_UPDATED = "tables.updated";

    public static final String EXCHANGE_COMMANDS = "commands.exchange";
    public static final String QUEUE_COMMANDS_UPDATED = "commands.updated.queue";
    public static final String ROUTING_KEY_COMMANDS_UPDATED = "commands.updated";


    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(EXCHANGE_ORDERS);
    }

    @Bean
    public Queue ordersCreatedQueue() {
        return new Queue(QUEUE_ORDERS_CREATED);
    }

    @Bean
    public Queue ordersUpdatedQueue() {
        return new Queue(QUEUE_ORDERS_UPDATED);
    }

    @Bean
    public Binding bindingOrdersCreated(Queue ordersCreatedQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersCreatedQueue).to(ordersExchange).with(ROUTING_KEY_ORDERS_CREATED);
    }

    @Bean
    public Binding bindingOrdersUpdated(Queue ordersUpdatedQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersUpdatedQueue).to(ordersExchange).with(ROUTING_KEY_ORDERS_UPDATED);
    }

    @Bean
    public TopicExchange menuItemsExchange() {
        return new TopicExchange(EXCHANGE_MENU_ITEMS);
    }

    @Bean
    public Queue menuItemsUpdatedQueue() {
        return new Queue(QUEUE_MENU_ITEMS_UPDATED);
    }

    @Bean
    public Binding bindingMenuItemsUpdated(Queue menuItemsUpdatedQueue, TopicExchange menuItemsExchange) {
        return BindingBuilder.bind(menuItemsUpdatedQueue).to(menuItemsExchange).with(ROUTING_KEY_MENU_ITEMS_UPDATED);
    }

    @Bean
    public TopicExchange tablesExchange() {
        return new TopicExchange(EXCHANGE_TABLES);
    }

    @Bean
    public Queue tablesUpdatedQueue() {
        return new Queue(QUEUE_TABLES_UPDATED);
    }

    @Bean
    public Binding bindingTablesUpdated(Queue tablesUpdatedQueue, TopicExchange tablesExchange) {
        return BindingBuilder.bind(tablesUpdatedQueue).to(tablesExchange).with(ROUTING_KEY_TABLES_UPDATED);
    }


    @Bean
    public TopicExchange commandsExchange() {
        return new TopicExchange(EXCHANGE_COMMANDS);
    }

    @Bean
    public Queue commandsUpdatedQueue() {
        return new Queue(QUEUE_ORDERS_UPDATED);
    }

    @Bean
    public Binding bindingCommandsUpdated(Queue commandsUpdatedQueue, TopicExchange commandsExchange) {
        return BindingBuilder.bind(commandsUpdatedQueue).to(commandsExchange).with(ROUTING_KEY_COMMANDS_UPDATED);
    }
}
