import React from 'react'
import './Header.scss'
import { useTranslation } from 'react-i18next';
import Button from '../../components/Buttons/Button/Button';
import routes from '../../utils/routes.json'
import NavTo from '../../components/NavTo/NavTo';

function Header() {
    const { t } = useTranslation();


    return (
        <div className='header'>
            <NavTo
                to={routes.home}
                className='logoApp flex-item'>

                <div className='logoBourse'>
                    {t('home.bourse')}
                </div>
                <div className='logoPlay'>
                    {t('home.play')}
                </div>
                <div className='logo'>
                    B
                </div>

            </NavTo>



            <div className='containerNavBar flex-item'>

            </div>

            <div className='buttonsLogin flex-item'>
                <NavTo className='custom-link'
                    path={routes.auth}
                    state={routes.login}
                >
                    <Button children={t('header.login')} styles={"button black"} />
                </NavTo>
                <NavTo
                    className='custom-link'
                    path={routes.auth}
                    state={routes.register}
                >
                    <Button children={t('header.register')} styles={"button"} />
                </NavTo>
            </div>
        </div >
    )
}

export default Header