import React, { useEffect, useState } from 'react'
import './AuthPage.scss'
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import tablette from "../../assets/img/tablette.png";
import route from "../../utils/routes.json";




function AuthPage() {
    const { t } = useTranslation();
    const [authType, setAuthType] = useState();
    const location = useLocation();

    const username = (value) => {
        console.log('Valeur du champ d\'entrée :', value);

    };

    const login = (value) => {
        console.log('Valeur du champ d\'entrée :', value);

    };

    const password = (value) => {
        console.log('Valeur du champ d\'entrée :', value);

    };

    useEffect(() => {
        console.log(location.state)
        setAuthType(location.state)
    }, [location.state]);

    return (
        <div className="login-container">
            <div className="image-container">
                <img src={tablette} className="w-80" alt="Image login" />
            </div>
            <div className="form-container">
                <h2>Connexion</h2>
                <form>
                    {
                        authType == route.register ?
                            <InputLabel label="Nom utilisateur" type="text" id="username" onInputChange={username}/> : null
                    }
                    <InputLabel label="Email/Username" type="text" id="email" onInputChange={login}/>
                    <InputLabel label="Mot de passe" type="password" id="password" onInputChange={password}/>
                    {
                        authType == route.register ?
                            <Button children={t('header.register')} styles={"button black"} />
                            :
                            <Button children={t('header.login')} styles={"button black"} />
                    }
                </form>
            </div>
        </div>

    )
}

export default AuthPage