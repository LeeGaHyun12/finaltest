import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';
import AudioRecorder from "./STT/AudioRecorder.jsx";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
        <AudioRecorder />
    </React.StrictMode>
);
