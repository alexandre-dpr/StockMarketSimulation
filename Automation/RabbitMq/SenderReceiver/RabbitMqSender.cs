using System.Globalization;
using Automation.RabbitMq.DTOs;
using RabbitMQ.Client.Events;

namespace Automation.RabbitMq.SenderReceiver;
using System;
using RabbitMQ.Client;
using System.Text;

public class RabbitMqSender
{
    private readonly ConnectionFactory _factory;
    private readonly IConnection _connection;

    public RabbitMqSender()
    {
        String rabbit = Environment.GetEnvironmentVariable("RABBITMQ");
        Console.WriteLine("RabbitMQ: " + rabbit);
        _factory = new ConnectionFactory();
                _factory.UserName = "service";
                _factory.Password = "service";
                _factory.Port = 5672;
                _factory.VirtualHost = "/";
                _factory.HostName = "rabbitmq";
                var endpoints = new System.Collections.Generic.List<AmqpTcpEndpoint> {
                    new AmqpTcpEndpoint(rabbit),
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
                                 durable: true,
                                 exclusive: false,
                                 autoDelete: false,
                                 arguments: null);

            string message = Newtonsoft.Json.JsonConvert.SerializeObject(order);
            var body = Encoding.UTF8.GetBytes(message);

            channel.BasicPublish(exchange: "bourse.exchange",
                                 routingKey: "bourse.routingkey.action",
                                 basicProperties: null,
                                 body: body);

            Console.WriteLine("Sent Order to Queue1: {0}", message);
        }
    }

    public double getPrice(TickerInfoDto ticker)
    {
        using (var channel = _connection.CreateModel())
        {
            channel.QueueDeclare(queue: "bourse.queue.price",
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: null);

            var replyQueueName = channel.QueueDeclare().QueueName;
            var props = channel.CreateBasicProperties();
            props.ReplyTo = replyQueueName;

            string message = Newtonsoft.Json.JsonConvert.SerializeObject(ticker);
            var body = Encoding.UTF8.GetBytes(message);

            channel.BasicPublish(exchange: "bourse.exchange",
                routingKey: "bourse.routingkey.price",
                basicProperties: props,
                body: body);

            var consumer = new EventingBasicConsumer(channel);
            String response = null;
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                response = Encoding.UTF8.GetString(body);
            };
            
            while (response == null)
            {
                channel.BasicConsume(queue: replyQueueName,
                                autoAck: true,
                                consumer: consumer);
                Thread.Sleep(2000);
            }
            String dec = CultureInfo.CurrentCulture.NumberFormat.NumberDecimalSeparator;
            return double.Parse(response.Replace(".",dec).Replace(",",dec));
        }
    }
}
