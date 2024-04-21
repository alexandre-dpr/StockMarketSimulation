import React, {useEffect, useState} from 'react'
import './Stock.scss'
import {useParams} from 'react-router-dom';
import StocksChat from "../../containers/StocksChat/StocksChat";
import {Auth} from "../../utils/Auth";
import {getStock} from "../../request/RequestMarket";
import utils from "../../utils/utils.json"
import CustomImage from "../../components/CustomImage/CustomImage";
import {formatCurrency, getStockLogo, percentageDiff, timestampToDDMMYY, timestampToHHMM} from "../../utils/services";
import {useTranslation} from 'react-i18next';
import logoCeo from "../../assets/img/user.png"
import logoWeb from "../../assets/img/web.png"
import logoExchange from "../../assets/img/exchange.png"
import LineChart from "../../components/Charts/LineChart/LineChart";
import TransactionWidget from "../../containers/TransactionWidget/TransactionWidget";
import Spinner from "../../components/Spinner/Spinner";
import constants from "../../utils/constants.json"
import {RequestWallet} from "../../request/RequestWallet";
import star_fill from "../../assets/img/star_fill.png";
import star_empty from "../../assets/img/star_empty.png";

function Stock() {
    const requestWallet = new RequestWallet();
    const {t} = useTranslation();
    const {ticker} = useParams();
    const [username, setUsername] = useState(null);
    const [data, setData] = useState(null);
    const ONE_DAY = utils.rangeHistoryStock.ONE_DAY
    const ONE_YEAR = utils.rangeHistoryStock.ONE_YEAR
    const [range, setRange] = useState(ONE_DAY[0]);
    const [history, setHistory] = useState([])
    const [timeStamp, setTimeStamp] = useState([])
    const [performance, setPerformance] = useState(0);
    const [isLoading, setIsLoading] = useState(true)
    const [isFav, setFav] = useState(false)
    const auth = new Auth();

    async function fetchData() {
        if (auth.isLoggedIn()) {
            const result_wallet = await requestWallet.getWallet();
            if (result_wallet.data && result_wallet.data.favoris.length > 0) {
                const favsTickers = result_wallet.data.favoris.map(stock => stock.ticker)
                setFav(favsTickers.includes(ticker))
            } else {
                setFav(false)
            }
        }

        const result = await getStock(ticker, range);
        setData(result.data)
        if (result.data) {
            if (result.data.history.chart.result[0].timestamp !== undefined && result.data.history.chart.result[0].indicators.quote[0].high !== undefined) {
                if (range === ONE_DAY[0]) {
                    setTimeStamp(timestampToHHMM(result.data.history.chart.result[0].timestamp))
                } else {
                    setTimeStamp(timestampToDDMMYY(result.data.history.chart.result[0].timestamp))
                }
                setHistory(result.data.history.chart.result[0].indicators.quote[0].high)
            }
            setIsLoading(false)
        }
    }

    useEffect(() => {
        async function fetchUsername() {
            const auth = new Auth();
            const name = await auth.getUsername();
            await setUsername(name);
        }

        fetchUsername();
        fetchData()
    }, [range]);


    useEffect(() => {
        setPerformance(percentageDiff(history[0], history[history.length - 1]))
    }, [history, range]);


    const handleChangeRange = (newRange) => {
        setRange(newRange)
    }

    async function manageFav() {
        if (isFav) {
            const del = await requestWallet.delFavori(ticker)
        } else {
            const add = await requestWallet.addFavori(ticker)
        }
        fetchData()
    }


    return (
        <div className='containerPage'>
            {
                isLoading ?
                    <div className="mt-10">
                        <Spinner/>
                    </div>
                    :
                    <div className={"pageStock"}>
                        <div className="d-flex w-100 containerSections ">
                            <div className={"containerStockGraph "}>
                                <div className="leftSide w-100">
                                    <div className={"d-flex align-center"}>
                                        <CustomImage src={getStockLogo(ticker)} alt={""}
                                                     style={{width: "50px", height: "50px"}}/>
                                        <div className="d-flex align-end w-100">
                                            <h1 className="ml-2">{data?.name} </h1>
                                            <h1 className="ml-1 ticker"> {ticker}</h1>
                                        </div>
                                        {auth.isLoggedIn() &&
                                            <div><img onClick={manageFav} src={isFav ? star_fill : star_empty} alt={""}
                                                      style={{width: "30px", cursor: "pointer"}}/></div>
                                        }
                                    </div>
                                    <div className="d-flex w-100">
                                        <div className="lineChart w-100">
                                            <div className="d-flex justify-between align-center headerLineChart">
                                                <div className="d-flex align-center w-100">
                                                    <h1> {`${data?.price} ${data?.currency}`}</h1>
                                                    <h3 className="ml-3"> {performance[1]}</h3>
                                                </div>
                                                <div className="justify-end d-flex w-50">
                                                    <div onClick={() => handleChangeRange(ONE_DAY[0])}
                                                         className={range === ONE_DAY[0] ? "focus boxRange mr-2" : "boxRange mr-2"}> {ONE_DAY[1]} </div>
                                                    <div onClick={() => handleChangeRange(ONE_YEAR[0])}
                                                         className={range === ONE_YEAR[0] ? "focus boxRange" : "" + ` boxRange`}> {ONE_YEAR[1]} </div>
                                                </div>
                                            </div>
                                            {<div className="mb-5 containerLineChart w-100">
                                                <LineChart
                                                    style={{height: "500px"}}
                                                    data={history}
                                                    labels={timeStamp}
                                                    intervalLabelsCount={10}
                                                    lineColor={performance[0] > 0 ? constants.green : constants.red}
                                                />
                                            </div>}
                                        </div>

                                    </div>
                                </div>

                                <div className="statistics">
                                    <h3> {t('stock.statistics')}</h3>
                                    <div className="d-flex w-100">
                                        <div className="marketCap d-flex w-50">
                                            <div className="label">{`${t('stock.marketCap')}:`}</div>
                                            <div
                                                className="ml-3"> {`${formatCurrency(data?.marketCap)} ${data?.currency}`} </div>
                                        </div>
                                    </div>

                                </div>

                                <div className="about mt-3">
                                    <h3>{`${t('stock.about')} ${data?.name}`}</h3>
                                    <div>
                                        <div className="infos mt-5 mb-5 w-100 d-flex flex-column">
                                            {data?.ceo !== null && data?.ceo !== "" &&
                                                <div className="ceo d-flex align-center">
                                                    <img src={logoCeo} style={{width: "40px"}}/>
                                                    <div className="ml-3">{data?.ceo}</div>
                                                </div>}
                                            {data?.website !== null && data?.website !== "" &&
                                                <div className="website d-flex  align-center mt-3">
                                                    <img src={logoWeb} style={{width: "40px"}}/>
                                                    <a href={data?.website} target="_blank" rel="noopener noreferrer"
                                                       className="ml-3">{data?.website}</a>
                                                </div>}
                                            {data?.exchange !== null && data?.exchange !== "" &&
                                                <div className="exchange d-flex align-center mt-3">
                                                    <img src={logoExchange} style={{width: "40px"}}/>
                                                    <div className="ml-3">{data?.exchange}</div>
                                                </div>}

                                        </div>
                                        {data?.description !== null && data?.description !== "" &&
                                            <div className="stockDescription w-100">
                                                {data?.description}
                                            </div>}
                                    </div>

                                </div>
                            </div>
                            <div className="scrollbar-none containerWidget">
                                <TransactionWidget price={data?.price} ticker={ticker}/>
                            </div>
                        </div>
                        {username ? <StocksChat stocks={ticker} username={username}/> : <></>}
                    </div>

            }
        </div>)
}

export default Stock