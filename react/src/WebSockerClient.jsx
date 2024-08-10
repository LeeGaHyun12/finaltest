import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WebSocketClient = () => {
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8081/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Connected to WebSocket');
                client.subscribe('/topic/expiry-alerts', message => {
                    const alert = JSON.parse(message.body);
                    setMessages(prevMessages => [...prevMessages, alert]);
                });
            },
            onDisconnect: () => {
                console.log('Disconnected from WebSocket');
            },
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    return (
        <div>
            <h2>Expiry Alerts</h2>
            {messages.map((msg, index) => (
                <div key={index}>{msg}</div>
            ))}
        </div>
    );
};

export default WebSocketClient;
