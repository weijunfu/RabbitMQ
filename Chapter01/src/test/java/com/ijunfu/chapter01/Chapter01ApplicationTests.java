package com.ijunfu.chapter01;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootTest
class Chapter01ApplicationTests {

    @Test
    void producer() {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.108");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("12345");

        Connection conn = null;
        Channel channel = null;

        try {
            // 2. 从连接工厂获取连接
            conn = factory.newConnection("Producer");

            // 3.从连接中获取通道
            channel = conn.createChannel();

            String queueName = "Queue01";

            // 4. 声明队列，不存在则创建
            channel.queueDeclare(
                    queueName,  // 队列名称
                    false,          // 队列是否支持持久化
                    false,          // 是否排他，即是否为私有。如果为True，会对当前队列加锁，其它通道不能访问，并且在连接关闭时自动删除，不受持久化和自动删除
                    false,          // 是否自动删除，当最后一个消费者断开连接之后，是否自动删除
                    null            // 队列参数，设置队列有效期、消息最大长度、队列中所有消息的声明周期等
            );

            // 5. 发送消息
            channel.basicPublish("", queueName, null, "Hello World".getBytes());

        } catch (IOException ex) {
            log.error("{}", ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            log.error("{}", ex.getMessage(), ex);
        } finally {
            if(Objects.nonNull(channel) && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    log.error("{}", ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    log.error("{}", ex.getMessage(), ex);
                }
            }

            if(Objects.nonNull(conn) && conn.isOpen()) {
                try {
                    conn.close();
                } catch (IOException ex) {
                    log.error("{}", ex.getMessage(), ex);
                }
            }
        }
    }

    @Test
    void consumer() {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.108");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("12345");

        Connection conn = null;
        Channel channel = null;

        try {
            // 2. 从连接工厂获取连接
            conn = factory.newConnection("Consumer");

            // 3.从连接中获取通道
            channel = conn.createChannel();

            String queueName = "Queue01";

            // 4. 监听队列
            channel.basicConsume(
                    queueName,  // 队列名称
                    true,       // 是否自动确认
                    new DeliverCallback() {     // 接受消息后的业务处理
                        @Override
                        public void handle(String consumerTag, Delivery message) throws IOException {
                            log.warn("收到消息：{}", new String(message.getBody(), Charset.forName("UTF-8")));
                        }
                    },
                    new CancelCallback() {
                        @Override
                        public void handle(String consumerTag) throws IOException {
                            log.warn("cancel - {}", consumerTag);
                        }
                    });

        } catch (IOException ex) {
            log.error("{}", ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            log.error("{}", ex.getMessage(), ex);
        } finally {
            if(Objects.nonNull(channel) && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    log.error("{}", ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    log.error("{}", ex.getMessage(), ex);
                }
            }

            if(Objects.nonNull(conn) && conn.isOpen()) {
                try {
                    conn.close();
                } catch (IOException ex) {
                    log.error("{}", ex.getMessage(), ex);
                }
            }
        }
    }
}
