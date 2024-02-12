import React, { useEffect, useState } from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import tablette from "../../assets/img/tablette.png";
import route from "../../utils/routes.json";
import {RequestAuth} from "../../request/RequestAuth";




function AuthPage() {
    const { t } = useTranslation();
    const request = new RequestAuth();
    const [authType, setAuthType] = useState();
    const location = useLocation();
    const[username, setUsername] = useState();
    const[login,setLogin] = useState();
    const[password, setPassword] = useState();


    useEffect(() => {
        console.log(location.state)
        setAuthType(location.state)
    }, [location.state]);

    function connected(){
        const resp = request.login(login,password);
    }

    function register(){
        const resp = request.register(username,login,password);
    }

    return (
        <div className="login-container">
            <div className="w-50">
                <img src={tablette} className="w-80" alt="Image login" />
            </div>
            <div className="form-container">
                <h2>{authType === route.register ? t('login.registration') : t('login.connection')}</h2>
                <form className="w-100 d-flex flex-column align-center">
                    {
                        authType === route.register ?
                            <InputLabel label={t('login.username')} type="text" id="username" onInputChange={setUsername}/> : null
                    }
                    <InputLabel label={authType === route.register ? t('login.email') : t('login.email-username')} type="text" id="email" onInputChange={setLogin}/>
                    <InputLabel label={t('login.password')} type="password" id="password" onInputChange={setPassword}/>
                    <Button children={authType === route.register ? t('login.register'):t('login.connect')} styles={"button black"}  handleClick={authType === route.register ? register : connected}/>
                </form>
            </div>
        </div>
    )
}

export default AuthPage