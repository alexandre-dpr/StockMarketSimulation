import React, {useEffect, useRef, useState} from 'react';
import "./Wallet.scss";
import Graph from "./Graph/Graph";
import logo from "../../../assets/img/logo.png";
import {RequestWallet} from "../../../request/RequestWallet";
import {Auth} from "../../../utils/Auth";
import routes from "../../../utils/routes.json";
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';
import LineChart from "../../../components/Charts/LineChart/LineChart";
import {timestampToDDMMYY} from "../../../utils/services";


function Wallet() {
    const { t } = useTranslation();
    const [data,setData] = useState(null);
    const [fav,setFav] = useState(null);
    const requestWallet = new RequestWallet();
    const auth = new Auth();
    const navigate = useNavigate();
    const [isPercent, setIsPercent] = useState();
    const [dataGraph, setDataGraph] = useState(null);
    const [dateGraph, setDateGraph] = useState(null);


    useEffect(()=>{
        initWallet();
    }, [])

    async function initWallet(){
        const resp = await requestWallet.getWallet();
        await setData(resp.data);
        await initGraph(resp.data);
    }



    async function initGraph(resp){
        let value = [];
        let date = [];
        resp.performanceHistory.forEach((item,index)=>{
            value.push(item.value);
            date.push(DDMMYYYY(item.date));
        })
        await setDateGraph(date);
        await setDataGraph(value)
    }


    function goTicker(ticker) {
        navigate(`${routes.stock_nav}/${ticker}`)
    }


    function DDMMYYYY(date){
        const dateToFormat = new Date(date);
        function addZero(number) {
            return number < 10 ? '0' + number : number;
        }
        const jour = addZero(dateToFormat.getDate());
        const mois = addZero(dateToFormat.getMonth() + 1);
        const année = dateToFormat.getFullYear();
        const dateFormatted = `${jour}/${mois}/${année}`;
        console.log(dateFormatted)
        return dateFormatted
    }



    return (
        <div className="containerPage">
            {
                data !== null?

                    <div className="cards">
                        <div className="d-flex flex-column h-100 w-65 w-sm-100-p">
                            <div className="d-flex align-center">

                                <h1>{t('wallet.wallet')}</h1>
                                <p className="ml-1-r"> Classement : {data.rank}</p>
                            </div>
                            <div className="d-flex align-center">
                                <h1>{data.totalValue} $ </h1>
                                <p className="green ml-r-1"> {data.performance.value} $ ({data.performance.percentage})</p>
                            </div>
                            {
                                dataGraph && dateGraph && <LineChart style={{height: "500px"}} data={dataGraph} labels={dateGraph}
                                           intervalLabelsCount={10}/>
                            }
                        </div>
                        <div className="d-flex flex-column h-100 w-35 w-sm-100-p">
                            <div className="h-50 d-flex flex-column">
                                <h3>{t('wallet.own')}{data.actions.length}{t('wallet.stocks')} :</h3>
                                <div className="d-flex flex-column w-100 overflow-scroll h-14-r">
                                    {
                                        data.actions.length > 0 ? <>
                                                {
                                                    data.actions.map((item, index) => (
                                                        <div className="actions-items " id={item.ticker}>
                                                            <div>
                                                                <img
                                                                    src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`}
                                                                    style={{width: 30}}/>
                                                            </div>
                                                            <div className="content pointer" onClick={() => {
                                                                goTicker(item.ticker)
                                                            }}>
                                                                <p>{item.ticker}</p>
                                                                <p>{item.price * item.quantity} $</p>
                                                            </div>
                                                            <div className="pointer" onClick={() => {
                                                                setIsPercent(!isPercent)
                                                            }}>
                                                                <p className="green">{isPercent ? item.performance.percentage : item.performance.value + " $"}</p>
                                                            </div>
                                                        </div>
                                                    ))
                                                }
                                            </>
                                            :
                                            <p>{t('wallet.own_nothing')}</p>
                                    }
                                </div>
                            </div>
                            <div className="h-50 w-100">
                                <h3>{t('wallet.favorites')}</h3>
                                <div className="d-flex flex-column w-100 overflow-scroll h-14-r">
                                    {
                                        data.favoris.length > 0 && data.favoris.map((item,index)=>(
                                            <div className="actions-items">
                                                <div>
                                                    <img src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`} style={{width: 30}}/>
                                                </div>
                                                <div className="content pointer" onClick={() => {
                                                    goTicker(item.ticker)
                                                }}>
                                                    <p>{item.ticker}</p>
                                                    <p>{item.price}</p>
                                                </div>
                                            </div>
                                        ))
                                    }
                                </div>
                            </div>
                        </div>
                    </div>
                    :
                    <></>
            }
        </div>

    );
}

export default Wallet;