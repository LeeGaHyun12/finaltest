import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import WebSocketClient from './WebSocketClient.jsx';
import './index.css';

const Root = () => (
    <React.StrictMode>
        <App />
        <WebSocketClient />
    </React.StrictMode>
);

ReactDOM.createRoot(document.getElementById('root')).render(<Root />);
