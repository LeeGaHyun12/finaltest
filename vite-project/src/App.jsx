import { useState, useRef } from 'react';
import axios from 'axios';

function App() {
    const [recording, setRecording] = useState(false);
    const [audioBlob, setAudioBlob] = useState(null);
    const [audioUrl, setAudioUrl] = useState(null);
    const mediaRecorderRef = useRef(null);
    const audioChunksRef = useRef([]);

    const startRecording = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            mediaRecorderRef.current = new MediaRecorder(stream);

            mediaRecorderRef.current.ondataavailable = (event) => {
                audioChunksRef.current.push(event.data);
            };

            mediaRecorderRef.current.onstop = () => {
                const blob = new Blob(audioChunksRef.current, { type: 'audio/wav' });
                setAudioBlob(blob);
                audioChunksRef.current = [];
                handleSubmit(blob);
            };

            mediaRecorderRef.current.start();

            // Stop recording after 3 seconds
            setTimeout(() => {
                mediaRecorderRef.current.stop();
                setRecording(false);
            }, 3000);

            setRecording(true);
        } catch (error) {
            console.error('Error accessing microphone:', error);
            alert('Microphone access is required to record audio.');
        }
    };

    const handleSubmit = async (blob) => {
        const formData = new FormData();
        formData.append('file', blob, 'recording.wav');

        try {
            const response = await axios.post('http://localhost:8080/api/tts/convert', formData, {
                responseType: 'arraybuffer', // Important to handle binary data
            });

            // Create a URL for the audio file and set it to play
            const newAudioBlob = new Blob([response.data], { type: 'audio/mpeg' });
            const newAudioUrl = URL.createObjectURL(newAudioBlob);

            // Revoke the old audio URL to free up resources
            if (audioUrl) {
                URL.revokeObjectURL(audioUrl);
            }

            setAudioUrl(newAudioUrl);
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('An error occurred while processing the file.');
        }
    };

    return (
        <div>
            <h1>Speech-to-Text and Text-to-Speech</h1>
            <button onClick={recording ? () => {} : startRecording}>
                {recording ? 'Recording...' : 'Start Recording'}
            </button>
            {audioUrl && (
                <div>
                    <h2>Generated Speech</h2>
                    <audio key={audioUrl} controls>
                        <source src={audioUrl} type="audio/mpeg" />
                        Your browser does not support the audio element.
                    </audio>
                </div>
            )}
        </div>
    );
}

export default App;
