import React from 'react'
import './Header.scss'
import { useTranslation } from 'react-i18next';
import Button from '../../components/Buttons/Button/Button';
import routes from '../../utils/routes.json'
import NavTo from '../../components/NavTo/NavTo';
import logo from "./../../assets/img/logo.png"

function Header() {
    const { t } = useTranslation();


    return (
        <div className='header'>
            <div className='containerNavBar flex-item'>

                <NavTo
                    path={routes.home}
                    className='logoApp'>

                    <div className='logoBourse'>
                        {t('home.bourse')}
                    </div>
                    <div className='logoPlay'>
                        {t('home.play')}
                    </div>
                    <div className='containerLogo'>
                        <img className='logo' src={logo} alt='logo' />
                    </div>


                </NavTo>

                <NavTo
                    path={routes.market}
                >
                    <div className='itemNav'>
                        {t('header.market')}
                    </div>
                </NavTo>
                <NavTo
                    path={routes.leaderboard}>
                    <div className='itemNav'>
                        {t('header.leaderboard')}
                    </div>
                </NavTo>
                <NavTo
                    path={routes.about}>
                    <div className='itemNav'>
                        {t('header.about')}

                    </div>
                </NavTo>



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