import React, {useEffect, useState} from 'react';
import "./Wallet.scss";
import {RequestWallet} from "../../../request/RequestWallet";
import {Auth} from "../../../utils/Auth";
import routes from "../../../utils/routes.json";
import {useNavigate} from "react-router-dom";
import {useTranslation} from 'react-i18next';
import LineChart from "../../../components/Charts/LineChart/LineChart";
import Spinner from "../../../components/Spinner/Spinner";
import HistoriqueTable from "../../../containers/Table/HistoriqueTable/HistoriqueTable";
import Camembert from "../../../components/Camembert/Camembert";
import CustomImage from "../../../components/CustomImage/CustomImage";
import star_fill from "../../../assets/img/star_fill.png";
import constants from '../../../utils/constants.json'
import {RequestAutomation} from "../../../request/RequestAutomation";
import bean from "../../../assets/img/poubelle.png";

function Wallet() {
    const {t} = useTranslation();
    const [data, setData] = useState(null);
    const requestWallet = new RequestWallet();
    const requestAutomation = new RequestAutomation();
    const auth = new Auth();
    const navigate = useNavigate();
    const [isPercent, setIsPercent] = useState();
    const [dataGraph, setDataGraph] = useState(null);
    const [dateGraph, setDateGraph] = useState(null);
    const [dataCam, setDataCam] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [historique, setHistorique] = useState([]);
    const [switchBtn, setSwitchBtn] = useState(true);
    const [favsTickers, setFavsTickers] = useState([])
    const [dataAutomation, setDataAutomation] = useState([]);
    const transactionTypes = {
        BUY: t('transactionWidget.buy'),
        SELL: t('transactionWidget.sell')
    };

    useEffect(() => {
        const baliseHeader = document.getElementById("header");
        baliseHeader.classList.remove('h-home');
        initWallet();
        initHistorique();
        initAutomation();
    }, [])

    async function initWallet() {
        const resp = await requestWallet.getWallet();
        if (resp.data && resp.data.favoris.length > 0) {
            setFavsTickers(resp.data.favoris.map(stock => stock.ticker))
        }
        setData(resp.data);
        await initGraph(resp.data);
        await initCamembert(resp.data);
        setIsLoading(false)
    }

    async function initHistorique() {
        await sleep(1000)
        const resp = await requestWallet.getHistorique();
        if(resp.data){
            setHistorique(resp.data)
        }
    }

    async function initAutomation(){
        const resp = await requestAutomation.getAutomation();
        if (resp.data !== undefined){
            let liste_automations = []
            const liste_aux = resp.data.automations
            liste_aux.map((item)=>{
                let order = {}
                order["Ticker"] = item.ticker
                order.id = item.id
                if (item.type === "priceThreshold") {
                    order[t("transactionWidget.orderType")] = t("transactionWidget.stopOrder")
                    if (item.transactionType === "buy") {
                        order[t("transactionWidget.transactionType")] = transactionTypes.BUY
                    } else {
                        order[t("transactionWidget.transactionType")] = transactionTypes.SELL
                    }
                    order[t('transactionWidget.quantity')] = item.quantity
                    order[t('transactionWidget.thresholdType')] = t(`transactionWidget.${item.thresholdType}`)
                    order[t('transactionWidget.stopPrice')] = item.thresholdPrice
                }else{
                    order[t("transactionWidget.orderType")] =t("transactionWidget.investmentPlanning")
                    if(item.transactionType === "buy"){
                        order[t("transactionWidget.transactionType")] = transactionTypes.BUY
                    }else{
                        order[t("transactionWidget.transactionType")] = transactionTypes.SELL
                    }
                    order[t('transactionWidget.quantity')] = item.buyQuantity
                    order[t('transactionWidget.recurrence')] = t(`transactionWidget.${item.frequency}`)
                }
                liste_automations.push(order)
            })
            setDataAutomation(liste_automations);
        }
        console.log(resp.data)
    }

    async function delAutomation(id) {
        const resp = await requestAutomation.deleteAutomation(id);
        initAutomation();
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }


    async function initGraph(resp) {
        let value = [];
        let date = [];
        resp.performanceHistory.forEach((item, index) => {
            value.push(item.value + 10000);
            date.push(DDMMYYYY(item.date));
        })
        date.push(getDate())
        value.push(resp.totalValue)
        await setDateGraph(date);
        await setDataGraph(value)
    }

    async function initCamembert(resp) {
        let ticker = [];
        let percent = [];
        ticker.push("Solde");
        percent.push(parseFloat((100 * resp.solde / resp.totalValue).toFixed(2)))
        resp.actions.forEach((item) => {
            ticker.push(item.ticker);
            const percentage = 100 * (item.price * item.quantity) / resp.totalValue;
            percent.push(parseFloat(percentage.toFixed(2)));
        })

        await setDataCam({
            "label": ticker,
            "data": percent,
        })
    }


    function goTicker(ticker) {
        navigate(`${routes.stock_nav}/${ticker}`)
    }


    function DDMMYYYY(date) {
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

    async function manageFav(ticker){
        const  del = await requestWallet.delFavori(ticker)
        initWallet();
    }

    return (
        <div className="containerPage">
            {
                isLoading ?
                    <div className="mt-10"><Spinner/></div>
                    :
                    <div className="cards">
                        <div className=" justify-center d-flex box-border w-100 flex-sm-column ">
                            <div className="d-flex flex-column h-100 w-65 w-sm-100-p">
                                <div className="containerWalletName">
                                    <h1>{t('wallet.wallet')}</h1>
                                    <h3>Classement : {data.rank}</h3>
                                </div>
                                <div className="d-flex align-center">
                                    <h1>{data.totalValue.toFixed(2)} $ </h1>
                                    <h3 style={{color:data.performance.value>0 ? constants.green : constants.red,cursor:"default"}}  className="ml-r-1"> {data.performance.value.toFixed(2)} $
                                        ({data.performance.percentage})</h3>
                                </div>
                                <div className="d-flex justify-center mb-1-r">
                                    <div className="switch-container">
                                        <div className={switchBtn ? "switch-btn active" : "switch-btn"}
                                             onClick={(e) => setSwitchBtn(true)}>
                                            <p>Evolution</p>
                                        </div>
                                        <div className={switchBtn ? "switch-btn" : "switch-btn active"}
                                             onClick={(e) => setSwitchBtn(false)}>
                                            <p>Répartition</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="container-graph h-100-p">
                                    {
                                        dataGraph && dateGraph && (switchBtn ?
                                            <LineChart style={{height: "500px"}} data={dataGraph} labels={dateGraph}
                                                       intervalLabelsCount={10} lineColor={data.performance.value>0 ? constants.green : constants.red}/> :
                                            <div style={{height: "450px", paddingBlock: "25px"}}><Camembert data={dataCam}/>
                                            </div>)
                                    }
                                </div>
                            </div>
                            <div className= "d-flex flex-column h-100 w-sm-100-p h-100 rightSide scrollbar-none">
                                <div className="d-flex flex-column mt-3">
                                    <h3>{t('wallet.own')}{data.actions.length}{t('wallet.stocks')} :</h3>
                                    <div
                                        className="d-flex flex-column wallet-resp">
                                        {
                                            data.actions.length > 0 ? <>
                                                    {
                                                        data.actions.map((item, index) => (
                                                            <div className="actions-items " id={item.ticker}>
                                                                <div>
                                                                    <CustomImage
                                                                        style={{width: 30}}
                                                                        src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`}/>
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
                                                                    <h4 style={{color:item.performance.value>0 ? constants.green : constants.red}}>{isPercent ? item.performance.percentage : item.performance.value.toFixed(2) + " $"}</h4>
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
                                <div className="">
                                    <h3>{t('wallet.favorites')}</h3>
                                    <div
                                        className="d-flex flex-column wallet-resp scrollbar-none">
                                        {
                                            data.favoris.length > 0 && data.favoris.map((item, index) => (
                                                <div className="actions-items">
                                                    <div>
                                                        <CustomImage
                                                            style={{width: 30}}
                                                            src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`}/>
                                                    </div>
                                                    <div className="content pointer" onClick={() => {
                                                        goTicker(item.ticker)
                                                    }}>
                                                        <p>{item.ticker}</p>
                                                        <p>{item.price}</p>
                                                    </div>
                                                    {auth.isLoggedIn() &&
                                                        <div><img onClick={() => manageFav(item.ticker)} src={favsTickers.includes(item.ticker) ? star_fill : null}
                                                                  alt={""} style={{width: "30px", cursor: "pointer"}}/>
                                                        </div>
                                                    }
                                                </div>
                                            ))
                                        }
                                        {
                                            data.favoris.length == 0 ?
                                                <p>{t('wallet.fav_nothing')}</p>
                                                :
                                                <></>

                                        }
                                    </div>
                                </div>
                                <div>
                                    <h3>{t('wallet.ordersPlaced')}</h3>
                                    {dataAutomation.length !== 0 ? dataAutomation.map(item =>
                                            <div className="orderLoop">
                                                {
                                                    Object.keys(item).map(
                                                        (auto) =>
                                                            <div className="d-flex w-100 mt-3">
                                                                <div className="w-50 Gabarito-Bold"> {`${auto}: `}</div>
                                                                <div className="w-50"> {item[auto]}</div>
                                                            </div>
                                                    )
                                                }
                                                <div className="mt-5 w-100 d-flex justify-end">
                                                    <img onClick={() => delAutomation(item.id)} style={{width: 20}} src={bean}/>
                                                </div>
                                            </div>
                                        ) :
                                        <p>{t('wallet.order_nothing')}</p>

                                    }
                                </div>
                            </div>
                        </div>
                        <div className="w-100 d-flex flex-column mt-3-r">
                            <h1>{t('wallet.historical')}</h1>
                            <HistoriqueTable data={historique?.mouvements}/>
                        </div>
                    </div>
            }

        </div>

    );
}

export default Wallet;