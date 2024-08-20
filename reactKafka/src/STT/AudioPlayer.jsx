import React from 'react';

// eslint-disable-next-line react/prop-types
const AudioPlayer = ({ audioUrl }) => (
    <div>
        {audioUrl ? <audio controls src={audioUrl} /> : 'No audio available'}
    </div>
);

export default AudioPlayer;
