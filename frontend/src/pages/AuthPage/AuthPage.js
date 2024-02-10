import React, { useEffect, useState } from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';


function AuthPage() {
    const { t } = useTranslation();
    const { type } = useParams();
    const [authType, setAuthType] = useState()


    useEffect(() => {
        setAuthType(type)
    }, [type]);

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