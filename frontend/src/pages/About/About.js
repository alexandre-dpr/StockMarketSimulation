import React from 'react'
import logoFac from '../../assets/img/logo-fac.svg'
import arezki from '../../assets/img/arezki.png'
import vincent from '../../assets/img/vincent.png'
import alex from '../../assets/img/alex.png'
import damien from '../../assets/img/damien.png'
import logoReact from "../../assets/img/logoReact.png"
import logoNet from "../../assets/img/logoNet.png"
import logoDocker from "../../assets/img/logoDocker.png"
import logoSpring from "../../assets/img/logoSpring.png"
import logoIn from "../../assets/img/in.png"
import logoGH from "../../assets/img/github.png"

import "./About.scss"
import {useTranslation} from "react-i18next";


function About() {

    const {t} = useTranslation();

    return (
        <div className="containerPage d-flex">
            <div className="pageAbout w-100 text-center">
                <div className="w-100 simpletype">BOURSEPLAY</div>
                <div className="team">
                    <h1>{t('about.team')}</h1>
                    <div
                        className="mt-3 mb-3 d-flex w-50 ml-25 justify-center text-justify">{t('about.paragraphTeam')}</div>
                    <div className="d-flex w-100 justify-evenly">
                        <div className="d-flex flex-column align-center ">
                            <img src={alex} alt="" className="pointer"/>
                            <h2>DUPRE Alexandre</h2>
                            <div className="d-flex w-100 justify-center pointer">
                                <img className="mr-2" src={logoIn} alt=""/>
                                <img className="ml-2" src={logoGH} alt=""/>
                            </div>
                        </div>
                        <div className="d-flex flex-column align-center ">
                            <img src={arezki} alt="" className="pointer"/>
                            <h2>AID Arezki</h2>
                            <div className="d-flex w-100 justify-center pointer">
                                <img className="mr-2" src={logoIn} alt=""/>
                                <img className="ml-2" src={logoGH} alt=""/>
                            </div>
                        </div>
                        <div className="d-flex flex-column align-center pointer ">
                            <img src={damien} alt="" className="pointer"/>
                            <h2>CRESPEAU Damien</h2>
                            <div className="d-flex w-100 justify-center pointer">
                                <img className="mr-2" src={logoIn} alt=""/>
                                <img className="ml-2" src={logoGH} alt=""/>
                            </div>
                        </div>
                        <div className="d-flex flex-column align-center pointer ">
                            <img src={vincent} alt=" " className="pointer"/>
                            <h2>DEBERNARDI Vincent</h2>
                            <div className="d-flex w-100 justify-center">
                                <img className="mr-2" src={logoIn} alt=""/>
                                <img className="ml-2" src={logoGH} alt=""/>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="ourMission mt-5">
                    <h1>{t('about.ourMission')}</h1>
                    <div
                        className="mt-3 mb-3 d-flex w-50 ml-25 justify-center text-justify">{t('about.paragraphMission')}</div>
                    <div className="d-flex w-100 justify-evenly">
                        <div className="d-flex flex-column align-center ">
                            <img src={logoReact} alt="" className="pointer"/>
                        </div>
                        <div className="d-flex flex-column align-center ">
                            <img src={logoSpring} alt="" className="pointer"/>
                        </div>
                        <div className="d-flex flex-column align-center ">
                            <img src={logoNet} alt="" className="pointer"/>
                        </div>
                        <div className="d-flex flex-column align-center ">
                            <img src={logoDocker} alt=" " className="pointer"/>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default About