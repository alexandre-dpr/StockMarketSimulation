using Automation.RabbitMq.DTOs;
using RabbitMQ.Client.Events;

namespace Automation.RabbitMq.SenderReceiver;
using System;
using RabbitMQ.Client;
using System.Text;

public class RabbitMQSender
{
    private readonly ConnectionFactory _factory;
    private readonly IConnection _connection;

    public RabbitMQSender()
    {
        _factory = new ConnectionFactory();
        _factory.UserName = "service";
        _factory.Password = "service";
        var endpoints = new System.Collections.Generic.List<AmqpTcpEndpoint> {
            new AmqpTcpEndpoint("hostname"),
            new AmqpTcpEndpoint("localhost")
        };
        _connection = _factory.CreateConnection(endpoints);
    }

    public void SendOrder(OrderDto order)
    {
        using (var channel = _connection.CreateModel())
        {
            channel.QueueDeclare(queue: "bourse.queue.action",
                                 durable: false,
                                 exclusive: false,
                                 autoDelete: false,
                                 arguments: null);

            string message = Newtonsoft.Json.JsonConvert.SerializeObject(order);
            var body = Encoding.UTF8.GetBytes(message);

            channel.BasicPublish(exchange: "bourse.exchange",
                                 routingKey: "bourse.routingkey",
                                 basicProperties: null,
                                 body: body);

            Console.WriteLine("Sent Order to Queue1: {0}", message);
        }
    }

    public string SendMessageToQueue2AndGetResponse(TickerInfoDto ticker)
    {
        using (var channel = _connection.CreateModel())
        {
            channel.QueueDeclare(queue: "bourse.queue.price",
                                 durable: false,
                                 exclusive: false,
                                 autoDelete: false,
                                 arguments: null);

            var replyQueueName = channel.QueueDeclare().QueueName;
            var props = channel.CreateBasicProperties();
            props.ReplyTo = replyQueueName;

            string message = Newtonsoft.Json.JsonConvert.SerializeObject(ticker);
            var body = Encoding.UTF8.GetBytes(message);

            channel.BasicPublish(exchange: "bourse.exchange",
                                 routingKey: "bourse.routingkey",
                                 basicProperties: props,
                                 body: body);

            var consumer = new EventingBasicConsumer(channel);
            string response = null;
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                response = Encoding.UTF8.GetString(body);
            };
            channel.BasicConsume(queue: replyQueueName,
                                 autoAck: true,
                                 consumer: consumer);

            return response;
        }
    }
}
