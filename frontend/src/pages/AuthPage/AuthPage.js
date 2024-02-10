import React, { useEffect, useState } from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';



function AuthPage() {
    const { t } = useTranslation();
    const [authType, setAuthType] = useState()
    const location = useLocation();

    useEffect(() => {
        setAuthType(location.state)
    }, [location.state]);

    return (
        <div>
            <div>
                {/*
            ici on va mettre une image à gauche (comme sur le login Traderepublic)
*/}
            </div>
            <div>

            </div>
        </div>

    )
}

export default AuthPage