import React, {useEffect, useState} from 'react'
import './Stock.scss'
import { useParams } from 'react-router-dom';
import StocksChat from "../../containers/StocksChat/StocksChat";
import {Auth} from "../../utils/Auth";

function Stock() {
    const { ticker } = useParams();
    const [username, setUsername] = useState(null);

    useEffect(() => {
        async function fetchUsername() {
            const auth = new Auth();
            const name = await auth.getUsername();
            await setUsername(name);
        }
        fetchUsername();
    }, []);

    return (
        <div className='containerPage pageStock'>
            <div className='title'>{ticker}</div>
            {
             username?
            <StocksChat stocks={ticker} username={username}/> :<></>
            }
        </div>
    )
}

export default Stock