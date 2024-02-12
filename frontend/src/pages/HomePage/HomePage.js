import React from 'react'
import './HomePage.scss'
import { useTranslation } from 'react-i18next';
import tablette from "../../assets/img/tablette.png"
import AnimatedLineChart from '../../containers/Charts/AnimatedLineChart';


function HomePage() {
    const { t } = useTranslation();

    return (
        <div>
            <div className='animatedLineCharts'>
                <AnimatedLineChart />
            </div>

            <div className='tablette'>
                <img className='imgTablette' src={tablette} />
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