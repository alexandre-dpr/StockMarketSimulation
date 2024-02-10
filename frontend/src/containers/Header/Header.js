import React from 'react'
import './Header.scss'
import { useTranslation } from 'react-i18next';
import Button from '../../components/Buttons/Button/Button';
import routes from '../../utils/routes.json'
import { Link } from 'react-router-dom';

function Header() {
    const { t } = useTranslation();


    return (
        <div className='header'>
            <Link className='custom-link'
                to="/" >
                <div className='logoApp flex-item'>

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
            </Link >


            <div className='containerNavBar flex-item'>

            </div>

            <div className='buttonsLogin flex-item'>

                <Link className='custom-link'
                    to="/auth"
                    state={routes.login}
                >
                    <Button children={t('header.login')} styles={"button black"} />
                </Link>
                <Link
                    className='custom-link'
                    to="/auth"
                    state={routes.register}
                >
                    <Button children={t('header.register')} styles={"button"} />
                </Link>

            </div>
        </div >
    )
}

export default Header