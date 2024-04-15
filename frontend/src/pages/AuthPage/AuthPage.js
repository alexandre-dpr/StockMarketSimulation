import React, {useEffect, useState} from 'react'
import './AuthPage.scss'
import {useTranslation} from 'react-i18next';
import {useLocation, useNavigate} from 'react-router-dom';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import route from "../../utils/routes.json";
import {RequestAuth} from "../../request/RequestAuth";
import {Auth} from "../../utils/Auth";
import routes from "../../utils/routes.json";
import {jwtDecode} from "jwt-decode";
import tradeCenter from "../../assets/img/pexels-nadi-lindsay-5320288.jpg"

function AuthPage({setIsAuth}) {
    const router = useNavigate();
    const {t} = useTranslation();
    const requestAuth = new RequestAuth();
    const [authType, setAuthType] = useState();
    const location = useLocation();
    const [username, setUsername] = useState('');
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
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
        setError("")
    }, [authType]);
    useEffect(() => {
        setAuthType(location.state)
    }, [location.state]);

    async function connected() {
        try {
            const resp = await requestAuth.login(login, password);
            await auth.setJwtToken(resp.data);
            router(routes.home);
            setIsAuth(true)
        } catch (e) {
            let msgError = ""
            if (e.response.status === 401) {
                msgError = t('errors.invalidLoginForm')
            } else {
                msgError = t('errors.errorLogin')
            }
            setError(msgError);
        }
    }

    async function register() {
        try {
            if (password === confirmPassword && password.length >= 8) {
                const resp = await requestAuth.register(username, login, password);
                await auth.setJwtToken(resp.data);
                router(routes.home);
                setIsAuth(true)
            } else {
                password !== confirmPassword ? setError(t('errors.incorrectPwdConfirm')) : setError(t('errors.pwdSize'))
            }
        } catch (e) {
            let msgError = ""
            if (e.response.status === 409) {
                msgError = t('errors.userAlreadyRegistered')
            } else if (e.response.status === 401) {
                msgError = t('errors.invalidRegistrationForm')
            } else {
                msgError = t('errors.errorRegistering')
            }
            setError(msgError);
        }
    }

    return (
        <div className="login-container">
            <div className="leftSideAuth w-40">
                <img src={tradeCenter} className="imageBg" alt="Image2 login"/>
            </div>
            <div className="form-container w-60">
                <h2>{authType === route.register ? t('login.registration') : t('login.connection')}</h2>
                <form className="w-100 d-flex flex-column justify-center align-center">
                    {
                        authType === route.register ?
                            <div className="w-50">
                                <InputLabel label={t('login.username')} type="text" id="username"
                                            onInputChange={setUsername}/>
                            </div>
                            : null
                    }
                    <div className="w-50">
                        <InputLabel label={authType === route.register ? t('login.email') : t('login.email-username')}
                                    type="text" id="email" onInputChange={setLogin}/>
                    </div>
                    <div className="w-50">
                        <InputLabel label={t('login.password')} type="password" id="password"
                                    onInputChange={setPassword}/>
                    </div>
                    {
                        authType === route.register ?
                            <div className="w-50">
                                <InputLabel label={t('login.confirmPassword')} type="password" id="confirm-password"
                                            onInputChange={setConfirmPassword}/>
                            </div> : null
                    }
                    <div className="error mt- mb-1">{error}</div>
                    <Button children={ authType === route.login ? t('login.connect') : t('login.register')}
                            styles={"button black"} handleClick={authType === route.login ? connected :  register}/>
                    <Button children={<div style={{fontSize:"0.8em"}}> {authType !== route.login ? t('login.connect') : t('login.register')}</div>}
                            styles={"button mt-3"} handleClick={authType === route.register ? () => setAuthType(route.login) : () => setAuthType(route.register)}/>
                </form>
            </div>
        </div>
    )
}

export default AuthPage