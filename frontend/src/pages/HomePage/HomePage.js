import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { Auth } from '../../utils/Auth';
import Wallet from './Wallet/Wallet';
import PresentationPage from './PresentationPage/PresentationPage';


function HomePage() {
    const [isAuth, setIsAuth] = useState(null);
    const auth = new Auth();

    useEffect(() => {
        async function verifierToken() {
            const token = await auth.getJwtToken();
            if (token !== null) {
                const dcode = jwtDecode(token);
                const currentTime = Math.floor(Date.now() / 1000);
                setIsAuth(currentTime < dcode.exp);
            } else {
                setIsAuth(false);
            }
        }

        verifierToken();
    }, []);

    if (isAuth === null) {
        return <></>;
    }

    return <>{isAuth === true ? <Wallet /> : <PresentationPage />}</>;
}

export default HomePage;
