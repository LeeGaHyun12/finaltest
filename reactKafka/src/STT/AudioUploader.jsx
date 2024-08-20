import axios from "axios";
import {useEffect, useState} from "react";

const AudioUploader = () => {
    const [recordedChunks, setRecordedChunks] = useState([]);
    const [audioUrl, setAudioUrl] = useState(null);
    const [mediaRecorder, setMediaRecorder] = useState(null);

    useEffect(() => {
        navigator.mediaDevices.getUserMedia({ audio: true }).then(stream => {
            const recorder = new MediaRecorder(stream);

            recorder.ondataavailable = event => {
                if (event.data.size > 0) {
                    setRecordedChunks(prevChunks => [...prevChunks, event.data]);
                }
            };

            setMediaRecorder(recorder);
        });
    }, []);

    const startRecording = () => {
        if (mediaRecorder) {
            mediaRecorder.start();
        }
    };

    const stopRecording = () => {
        if (mediaRecorder) {
            mediaRecorder.stop();
        }
    };

    const handleUpload = async () => {
        const blob = new Blob(recordedChunks, { type: 'audio/wav' });
        const formData = new FormData();
        formData.append('file', blob, 'recording.wav');

        try {
            const sttResponse = await axios.post('http://localhost:8080/api/stt', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            const ttsResponse = await axios.post('http://localhost:8080/api/tts',
                { text: sttResponse.data },
                {
                    responseType: 'arraybuffer'
                }
            );

            const audioBlob = new Blob([ttsResponse.data], { type: 'audio/mp3' });
            const audioUrl = URL.createObjectURL(audioBlob);
            setAudioUrl(audioUrl);
        } catch (error) {
            console.error('Error uploading file', error);
            if (error.response) {
                console.error('Error response data:', error.response.data);
                console.error('Error response status:', error.response.status);
                console.error('Error response headers:', error.response.headers);
            } else {
                console.error('Error message:', error.message);
            }
        }
    };

    return (
        <div>
            <button onClick={startRecording}>Start Recording</button>
            <button onClick={stopRecording}>Stop Recording</button>
            <button onClick={handleUpload}>Upload and Convert</button>
            {audioUrl && (
                <audio controls>
                    <source src={audioUrl} type="audio/mp3" />
                    Your browser does not support the audio element.
                </audio>
            )}
        </div>
    );
};

export default AudioUploader;
