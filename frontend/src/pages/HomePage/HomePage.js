import React, {useState} from 'react'
import './HomePage.scss'
import { useTranslation } from 'react-i18next';
import bg_pc from "../../assets/img/bg-pc.png"
import AnimatedLineChart from '../../containers/Charts/AnimatedLineChart';
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {getStocksList} from "../../request/RequestMarket";
import {get} from "axios";

function HomePage() {
    const location = useLocation();

    useEffect(() => {
        const isReloaded = localStorage.getItem('isReloaded');
        if (!isReloaded) {
            document.body.classList.add('initial-load-animation');
            localStorage.setItem('isReloaded', true);
        }

        return () => localStorage.removeItem('isReloaded');
    }, [location.pathname]);


    const { t } = useTranslation();

    return (
        <div>

            <div className="paper paper-1">
                <AnimatedLineChart/>
            </div>


            <svg width="1800" height="900" viewBox="0 0 1800 1080" fill="none" className="bg-svg">
                <path d="M0 -217L2160.5 1200L2303.5 -158.5L0 -217Z" fill="url(#paint0_linear)"></path>
                <defs>
                    <linearGradient id="paint0_linear" x1="611.5" y1="564" x2="2230.5" y2="276.5" gradientUnits="userSpaceOnUse">
                        <stop stop-color="#36A2EB" stop-opacity="0.15"></stop> <stop offset="2" stop-color="#FF6384" stop-opacity="0.35"></stop>
                     </linearGradient>
                </defs>
            </svg>

            <div className='bg-pc'>
                <img className='imgTablette' src={bg_pc} />
            </div>


            <div className='homePageFooter'>
                <div className='slogan'>
                    {t('home.slogan')}
                </div>
                <div className='phraseAccroche'>
                    {t('home.trigger')}
                </div>
            </div>
        </div>
    )
}

export default HomePage