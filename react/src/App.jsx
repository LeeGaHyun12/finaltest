import React from 'react';
import axios from 'axios';

const App = () => {
    const sendUserId = async () => {
        try {
            const response = await axios.post('http://localhost:8080/kafka/check-expiry', { userId: 1 });
            console.log(response.data);
        } catch (error) {
            console.error('Error sending userId to Kafka:', error);
        }
    };

    return (
        <div>
            <button onClick={sendUserId}>Check Expiry</button>
        </div>
    );
};

export default App;
