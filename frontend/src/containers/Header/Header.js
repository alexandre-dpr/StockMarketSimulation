import React from 'react'
import './Header.scss'
import { useTranslation } from 'react-i18next';
import Button from '../../components/Buttons/Button/Button';
import { useNavigate } from 'react-router-dom';
import routes from '../../utils/routes.json'

function Header() {
    let navigate = useNavigate();
    const { t } = useTranslation();

    const handleClickAuth = (type) => {
        navigate(`${routes.auth}${type}`);
    }

    const handleClickHome = () => {
        navigate(`${routes.home}`);
    }


    return (
        <div className='header'>
            <div onClick={handleClickHome} className='logoApp flex-item'>
                <div className='logoBourse'>
                    {t('home.bourse')}
                </div>
                <div className='logoPlay'>
                    {t('home.play')}
                </div>
                <div className='logo'>
                    B
                </div>
            </div>

            <div className='containerNavBar flex-item'>

            </div>

            <div className='buttonsLogin flex-item'>
                <div>
                    <Button handleClick={() => handleClickAuth(routes.login)} children={t('header.login')} styles={"button black"} />
                </div>
                <div>
                    <Button handleClick={() => handleClickAuth(routes.register)} children={t('header.register')} styles={"button"} />
                </div>
            </div>
        </div>
    )
}

export default Header