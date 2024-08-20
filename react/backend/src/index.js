import express from 'express';
import bodyParser from 'body-parser';
import { kafkaProducer, kafkaConsumer } from './kafka.js';
import http from 'http';
import { Server } from 'socket.io';

const app = express();
const port = 8081;

app.use(bodyParser.json());

// Create HTTP server
const server = http.createServer(app);

// Create WebSocket server
const io = new Server(server, {
    cors: {
        origin: '*',
        methods: ['GET', 'POST']
    }
});

app.post('/kafka/check-expiry', async (req, res) => {
    const { userId } = req.body;

    try {
        await kafkaProducer.send({
            topic: 'expiry-topic',
            messages: [{ value: JSON.stringify({ userId }) }],
        });

        res.status(200).send('Message sent to Kafka');
    } catch (error) {
        console.error('Error sending message to Kafka:', error);
        res.status(500).send('Error sending message to Kafka');
    }
});

server.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
    kafkaConsumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            console.log(`Received message: ${message.value.toString()}`);
            io.emit('expiry-alerts', message.value.toString());
        },
    });
});
