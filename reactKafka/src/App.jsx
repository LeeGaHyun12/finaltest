// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
//
// const App = () => {
//     const [data, setData] = useState([]);
//
//     useEffect(() => {
//         axios.get('http://localhost:8080/kafka/check-expiry?userId=1')
//             .then(response => {
//                 setData(response.data);  // 배열을 예상
//             })
//             .catch(error => {
//                 console.error('Error fetching expiry data:', error);
//             });
//     }, []);
//
//     return (
//         <div>
//             <h1>Expiry Check</h1>
//             {data.length > 0 ? (
//                 <ul>
//                     {data.map(item => (
//                         <li key={item.refrifoodId}>
//                             <p>Refrifood ID: {item.refrifoodId}</p>
//                             <p>Food ID: {item.foodId}</p>
//                             <p>Name: {item.name}</p>
//                             <p>Refri User ID: {item.refriuserId}</p>
//                             <p>Expiry Date: {item.expiryDate}</p>
//                         </li>
//                     ))}
//                 </ul>
//             ) : (
//                 <p>Loading...</p>
//             )}
//         </div>
//     );
// };
//
// export default App;

import React, { useState } from 'react';
import axios from 'axios';

const App = () => {
    const [userId, setUserId] = useState('');
    const [notification, setNotification] = useState('');
    const [error, setError] = useState('');

    const handleInputChange = (e) => {
        setUserId(e.target.value);
    };

    const fetchNotification = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/notifications/latest`, {
                params: { userId }
            });
            setNotification(response.data);
            setError('');
        } catch (error) {
            setError('Error fetching notification');
            setNotification('');
        }
    };

    return (
        <div>
            <h1>Expiry Notifications</h1>
            <input
                type="number"
                value={userId}
                onChange={handleInputChange}
                placeholder="Enter User ID"
            />
            <button onClick={fetchNotification}>Fetch Notification</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {notification && <p>{notification}</p>}
        </div>
    );
};

export default App;

