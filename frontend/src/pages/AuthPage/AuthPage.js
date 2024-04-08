import React, { useEffect, useState } from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import {useLocation, useNavigate, useRoutes} from 'react-router-dom';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import tablette from "../../assets/img/tablette.png";
import route from "../../utils/routes.json";
import {RequestAuth} from "../../request/RequestAuth";
import {Auth} from "../../utils/Auth";
import routes from "../../utils/routes.json";
import {jwtDecode} from "jwt-decode";
import {RequestWallet} from "../../request/RequestWallet";


function AuthPage({setIsAuth}) {
    const router = useNavigate();
    const { t } = useTranslation();
    const requestAuth = new RequestAuth();
    const requestWallet = new RequestWallet();
    const [authType, setAuthType] = useState();
    const location = useLocation();
    const[username, setUsername] = useState('');
    const[login,setLogin] = useState('');
    const[password, setPassword] = useState('');
    const [confirmPassword,setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const auth = new Auth();

    useEffect(() => {
        async function verifierToken() {
            const token = await auth.getJwtToken();
            if (token !== null) {
                const dcode = jwtDecode(token);
                const currentTime = Math.floor(Date.now() / 1000);
                const tokenValide = currentTime < dcode.exp;
                if (tokenValide) {
                    router(routes.home);
                }
            }
        }
        verifierToken();
    }, []);

    useEffect(() => {
        setAuthType(location.state)
    }, [location.state]);



    async function connected(){
        try {
            const resp = await requestAuth.login(login, password);
            await auth.setJwtToken(resp.data);
            router(routes.home);
            setIsAuth(true)
        }catch (e) {
            setError(e.message);
        }

    }

    async function register(){
        try {
            if (password === confirmPassword && password.length >= 8){
                const resp = await requestAuth.register(username, login, password);
                await auth.setJwtToken(resp.data);
                router(routes.home);
                setIsAuth(true)
            }
        }catch (e) {
            setError(e.message);
        }

    }

    return (
        <div className="login-container">
            <div className="w-50">
                <img src={tablette} className="w-80" alt="Image login" />
            </div>
            <div className="form-container">
                <p>{error}</p>
                <h2>{authType === route.register ? t('login.registration') : t('login.connection')}</h2>
                <form className="w-100 d-flex flex-column align-center">
                    {
                        authType === route.register ?
                            <InputLabel label={t('login.username')} type="text" id="username" onInputChange={setUsername}/> : null
                    }
                    <InputLabel label={authType === route.register ? t('login.email') : t('login.email-username')} type="text" id="email" onInputChange={setLogin}/>
                    <InputLabel label={t('login.password')} type="password" id="password" onInputChange={setPassword}/>
                    {
                        authType === route.register ?
                            <InputLabel label={t('login.confirmPassword')} type="password" id="confirm-password" onInputChange={setConfirmPassword}/> : null
                    }
                    <Button children={authType === route.register ? t('login.register'):t('login.connect')} styles={"button black"}  handleClick={authType === route.register ? register : connected}/>
                </form>
            </div>
        </div>
    )
}

export default AuthPage