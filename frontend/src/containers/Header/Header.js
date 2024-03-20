import React, {useEffect} from 'react'
import './Header.scss'
import {useTranslation} from 'react-i18next';
import Button from '../../components/Buttons/Button/Button';
import routes from '../../utils/routes.json'
import NavTo from '../../components/NavTo/NavTo';
import logo from "./../../assets/img/logo.png"
import {useLocation} from "react-router-dom";
import iconSignOut from "../../assets/img/icons8-déconnexion-60.png"
import {Auth} from "../../utils/Auth";


function Header({isAuth, handleClick}) {
    const auth = new Auth();
    const location = useLocation();
    const {t} = useTranslation();

    useEffect(() => {
        if (location.pathname === routes.home) {
            const baliseHeader = document.getElementById("header");
            baliseHeader.className = "header h-home"
        }

        return () => {
            const baliseHeader = document.getElementById("header");
            baliseHeader.className = "header"
        };

    }, [location.pathname]);


    return (
        <div id="header" className='header'>
            <div className="flex-item-1">
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
                            <img className='logo' src={logo} alt='logo'/>
                        </div>
                    </NavTo>
            </div>
            <div className='containerNavBar flex-item-1'>
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

            <div className="flex-item-1">
                {
                    !isAuth ?
                        <div className='buttonsLogin w-100 d-flex justify-end'>
                            <NavTo className='custom-link'
                                   path={routes.auth}
                                   state={routes.login}
                            >
                                <Button children={t('header.login')} styles={"button black"}/>
                            </NavTo>
                            <NavTo
                                className='custom-link'
                                path={routes.auth}
                                state={routes.register}
                            >
                                <Button children={t('header.register')} styles={"button"}/>
                            </NavTo>
                        </div>
                        :
                        <div className="w-100 d-flex justify-end" >
                            <img style={{width:"30px" , cursor:"pointer"}} className="pr-4" onClick={handleClick} src={iconSignOut} alt=""/>
                        </div>
                }
            </div>
        </div>
    )
}

export default Header