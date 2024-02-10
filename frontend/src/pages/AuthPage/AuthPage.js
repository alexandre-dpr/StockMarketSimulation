import React from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../../internationalization/LanguageSwitcher';

function AuthPage() {
    const { t } = useTranslation();

    return (
        <div>
            <h1>{t('home.welcomeMessage')}</h1>;
        </div>

    )
}

export default AuthPage