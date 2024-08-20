import { useState, useRef } from 'react';
import axios from 'axios';
//import {env} from "../../.eslintrc.cjs";

const baseURL = 'http://localhost:8080';

const AudioRecorder = () => {
    const [recording, setRecording] = useState(false);
    const [recordedChunks, setRecordedChunks] = useState([]);
    const [audioUrl, setAudioUrl] = useState('');
    const mediaRecorderRef = useRef(null);
    const audioRef = useRef(null);

    const startRecording = () => {
        setRecording(true);
        navigator.mediaDevices.getUserMedia({ audio: true })
            .then(stream => {
                mediaRecorderRef.current = new MediaRecorder(stream);
                mediaRecorderRef.current.ondataavailable = event => {
                    if (event.data.size > 0) {
                        setRecordedChunks(prev => [...prev, event.data]);
                    }
                };
                mediaRecorderRef.current.start();
            })
            .catch(err => console.error('Error accessing media devices.', err));
    };

    const stopRecording = () => {
        mediaRecorderRef.current.stop();
        setRecording(false);
    };

    const handleUpload = async () => {
        const blob = new Blob(recordedChunks, { type: 'audio/wav' });
        const formData = new FormData();
        formData.append('file', blob, 'recording.wav');

        try {
            // STT API 호출
            const sttResponse = await axios.post('http://localhost:8080/api/stt', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            // STT API의 응답 데이터 로그 확인
            console.log('STT Response:', sttResponse.data);

            // TTS API 호출
            const ttsResponse = await axios.post('http://localhost:8080/api/tts',
                { text: sttResponse.data },
                {
                    responseType: 'arraybuffer'  // 응답 데이터가 binary 형태일 경우
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
            <h1>Audio Recorder</h1>
            <div>
                {!recording ? (
                    <button onClick={startRecording}>Start Recording</button>
                ) : (
                    <button onClick={stopRecording}>Stop Recording</button>
                )}
                <button onClick={handleUpload} disabled={recordedChunks.length === 0}>
                    Upload and Process
                </button>
            </div>
            {audioUrl && (
                <div>
                    <h2>Generated Audio</h2>
                    <audio ref={audioRef} controls src={audioUrl} />
                </div>
            )}
        </div>
    );
};

export default AudioRecorder;
