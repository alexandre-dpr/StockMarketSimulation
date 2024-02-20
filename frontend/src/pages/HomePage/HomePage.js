import React, {useState} from 'react'
import './HomePage.scss'
import {useTranslation} from 'react-i18next';
import bg_pc from "../../assets/img/bg-pc-resize-tiny.png"
import AnimatedLineChart from '../../containers/Charts/AnimatedLineChart';
import {useEffect} from 'react';
import {useLocation} from 'react-router-dom';
import {Chart} from "react-chartjs-2";
import styles from "../../index.scss"

function HomePage() {
    const location = useLocation();

    useEffect(() => {
        const isReloaded = localStorage.getItem('isReloaded');
        if (!isReloaded) {
            /*
            const baliseBgPc = document.getElementById("bg-pc")
            const baliseFooter = document.getElementById("homePageFooter")
            baliseBgPc.classList.add('initial-load-animation');
            baliseFooter.classList.add('initial-load-animation');
            localStorage.setItem('isReloaded', true);
            */

        }

        return () => {
            localStorage.removeItem('isReloaded');
        }
    }, [location.pathname]);


    useEffect(() => {
        document.body.style.backgroundColor = styles.primaryColor;
        return () => {
            document.body.style.backgroundColor = '';
        };
    }, []);

    const {t} = useTranslation();


    const data = {
        labels: [
            'Red',
            'Blue',
            'Yellow'
        ],
        datasets: [{
            data: [300, 50, 100],
            backgroundColor: [
                'rgb(255, 99, 132)',
                'rgb(54, 162, 235)',
                'rgb(132,104,239)'
            ],
            hoverOffset: 4
        }]
    };


    const data2 = {
        labels: [
            'Red',
            'Blue',
            'Purple'
        ],
        datasets: [{
            data: [30, 140, 70],
            backgroundColor: [
                'rgb(255, 99, 132)',
                'rgb(54, 162, 235)',
                'rgb(132,104,239)'
            ],
            hoverOffset: 4
        }]
    };

    const options = {
        responsive: true,
            plugins: {
            legend: false,
                tooltip: {
                enabled: false,
            },
        }
    }


    return (
        <div className={"homePage"}>

            <div className="paper paper-1">
                <AnimatedLineChart/>
            </div>

            <div className="paper paper-2">
                <Chart  options={options} className={"doughnutChart"} type={"doughnut"} data={data} />
                <Chart options={options} className={"doughnutChart"} type={"doughnut"} data={data2} />
            </div>

            <svg width="1800" height="900" viewBox="0 0 1800 1080" fill="none" className="bg-svg">
                <path d="M0 -217L2160.5 1200L2303.5 -158.5L0 -217Z" fill="url(#paint0_linear)"></path>
                <defs>
                    <linearGradient id="paint0_linear" x1="611.5" y1="564" x2="2230.5" y2="276.5"
                                    gradientUnits="userSpaceOnUse">
                        <stop stop-color="#36A2EB" stop-opacity="0.15"></stop>
                        <stop offset="2" stop-color="#FF6384" stop-opacity="0.35"></stop>
                    </linearGradient>
                </defs>
            </svg>

            <div id="bg-pc" className='bg-pc'>
                <img loading="lazy" className='imgTablette' src={bg_pc}/>
            </div>


            <div id="homePageFooter" className='homePageFooter'>
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