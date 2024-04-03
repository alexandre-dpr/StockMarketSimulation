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
import Spinner from "../../../components/Spinner/Spinner";
import HistoriqueTable from "../../../containers/Table/HistoriqueTable/HistoriqueTable";


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
    const [isLoading, setIsLoading] = useState(true)
    const [historique, setHistorique] = useState([]);


    useEffect(()=>{
        initWallet();
        initHistorique();
    }, [])

    async function initWallet(){
        const resp = await requestWallet.getWallet();
        await setData(resp.data);
        await initGraph(resp.data);
        setIsLoading(false)
    }

    async function initHistorique(){
        const resp = await requestWallet.getHistorique();
        await setHistorique(resp.data)
        console.log(resp.data)
    }



    async function initGraph(resp){
        let value = [];
        let date = [];
        resp.performanceHistory.forEach((item,index)=>{
            value.push(item.value+10000);
            date.push(DDMMYYYY(item.date));
        })
        date.push(getDate())
        value.push(resp.totalValue)
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
        return dateFormatted
    }

    function getDate() {
        const currentDate = new Date();
        const day = currentDate.getDate().toString().padStart(2, '0');
        const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
        const year = currentDate.getFullYear();

        return `${day}/${month}/${year}`;
    }

    function lastPointOnGraph(data,date,resp){

    }


    return (
        <div className="containerPage">
            {
                isLoading?
                    <div className="mt-10"><Spinner/></div>
                    :
                    <div className="cards">
                        <div className="align-center justify-center d-flex box-border w-100 flex-sm-column">
                            <div className="d-flex flex-column h-100 w-65 w-sm-100-p">
                                <div className="d-flex align-center">

                                    <h1>{t('wallet.wallet')}</h1>
                                    <p className="ml-1-r"> Classement : {data.rank}</p>
                                </div>
                                <div className="d-flex align-center">
                                    <h1>{data.totalValue.toFixed(2)} $ </h1>
                                    <p className="green ml-r-1"> {data.performance.value.toFixed(2)} $ ({data.performance.percentage})</p>
                                </div>
                                {
                                    dataGraph && dateGraph && <LineChart style={{height: "500px"}} data={dataGraph} labels={dateGraph}
                                                                         intervalLabelsCount={10}/>
                                }
                            </div>
                            <div className="d-flex flex-column h-100 w-35 w-sm-100-p">
                                <div className="h-50 d-flex flex-column">
                                    <h3>{t('wallet.own')}{data.actions.length}{t('wallet.stocks')} :</h3>
                                    <div className="d-flex flex-column w-100 overflow-scroll h-14-r wallet-resp">
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
                                                                    <p>{(item.price * item.quantity).toFixed(2)} $</p>
                                                                </div>
                                                                <div className="pointer" onClick={() => {
                                                                    setIsPercent(!isPercent)
                                                                }}>
                                                                    <p className="green">{isPercent ? item.performance.percentage : item.performance.value.toFixed(2) + " $"}</p>
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
                                    <div className="d-flex flex-column w-100 overflow-scroll h-14-r wallet-resp">
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
                        <div className="w-100 d-flex flex-column mt-3-r">
                            <h1>Historique :</h1>
                            <HistoriqueTable data={historique.mouvements}/>
                        </div>
                    </div>
            }

        </div>

    );
}

export default Wallet;