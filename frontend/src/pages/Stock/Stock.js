import React, {useEffect, useState} from 'react'
import './Stock.scss'
import {useParams} from 'react-router-dom';
import StocksChat from "../../containers/StocksChat/StocksChat";
import {Auth} from "../../utils/Auth";
import {getStock} from "../../request/RequestMarket";
import utils from "../../utils/utils.json"
import CustomImage from "../../components/CustomImage/CustomImage";
import {getStockLogo, percentageDiff, timestampToDDMMYY, timestampToHHMM} from "../../utils/services";
import {useTranslation} from 'react-i18next';
import logoCeo from "../../assets/img/user.png"
import logoWeb from "../../assets/img/web.png"
import logoExchange from "../../assets/img/exchange.png"
import LineChart from "../../components/Charts/LineChart/LineChart";
import TransactionWidget from "../../containers/TransactionWidget/TransactionWidget";


function Stock() {
    const {t} = useTranslation();
    const {ticker} = useParams();
    const [username, setUsername] = useState(null);
    const [data, setData] = useState(null);
    const ONE_DAY = utils.rangeHistoryStock.ONE_DAY
    const ONE_YEAR = utils.rangeHistoryStock.ONE_YEAR
    const [range, setRange] = useState(ONE_DAY[0]);
    const [history, setHistory] = useState([])
    const [timeStamp, setTimeStamp] = useState([])

    async function fetchData() {
        const result = await getStock(ticker, range);
        setData(result.data)
        console.log(result.data)
        if (result.data.history.chart.result[0].timestamp !== undefined && result.data.history.chart.result[0].indicators.quote[0].high !== undefined) {
            if (range === ONE_DAY[0]) {
                setTimeStamp(timestampToHHMM(result.data.history.chart.result[0].timestamp))
            } else {
                setTimeStamp(timestampToDDMMYY(result.data.history.chart.result[0].timestamp))
            }
            setHistory(result.data.history.chart.result[0].indicators.quote[0].high)
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


    const handleChangeRange = (newRange) => {
        setRange(newRange)
    }


    return (<div className='containerPage'>
        <div className="pageStock">
            <div className="d-flex w-100">
                <div className="leftSide w-70">
                    <div className={"d-flex align-center"}>
                        <CustomImage src={getStockLogo(ticker)} alt={""} style={{width: "50px", height: "50px"}}/>
                        <div className="d-flex align-end w-100">
                            <h1 className="ml-2">{data?.name} </h1>
                            <h1 className="ml-1 ticker"> {ticker}</h1>
                        </div>

                    </div>
                    <div className="d-flex w-100">
                        <div className="lineChart w-100">
                            <div className="d-flex justify-between align-center headerLineChart">
                                <div className="d-flex align-center w-100">
                                    <h1> {`${data?.price} ${data?.currency}`}</h1>
                                    <h3 className="ml-3"> {percentageDiff(history[0], history[history.length - 1])}</h3>
                                </div>
                                <div className="justify-end d-flex w-50">
                                    <div onClick={() => handleChangeRange(ONE_DAY[0])}
                                         className={range === ONE_DAY[0] ? "focus boxRange mr-2" : "boxRange mr-2"}> {ONE_DAY[1]} </div>
                                    <div onClick={() => handleChangeRange(ONE_YEAR[0])}
                                         className={range === ONE_YEAR[0] ? "focus boxRange" : "" + ` boxRange`}> {ONE_YEAR[1]} </div>
                                </div>
                            </div>
                            {<div className="mb-5 containerLineChart w-100">
                                <LineChart style={{height: "500px"}} data={history} labels={timeStamp}
                                           intervalLabelsCount={10}/>
                            </div>}
                        </div>

                    </div>
                </div>

                <div className="rightSide ml-10 w-25">
                    <TransactionWidget ticker={ticker}/>
                </div>
            </div>

            <div className="statistics">
                <h1> {t('stock.statistics')}</h1>
                <div className="d-flex w-100">
                    <div className="marketCap d-flex w-50">
                        <div className="label">{`${t('stock.marketCap')}:`}</div>
                        <div className="ml-3"> {`${data?.marketCap} ${data?.currency}`} </div>
                    </div>
                </div>

            </div>

            <div className="about">
                <h1>{`${t('stock.about')} ${data?.name}`}</h1>
                <div className="d-flex p">
                    <div className="infos w-30 d-flex justify-evenly flex-column">
                        {data?.ceo !== null && data?.ceo !== "" && <div className="ceo d-flex align-center">
                            <img src={logoCeo} style={{width: "40px"}}/>
                            <div className="ml-3">{data?.ceo}</div>
                        </div>}
                        {data?.website !== null && data?.website !== "" &&
                            <div className="website d-flex  align-center">
                                <img src={logoWeb} style={{width: "40px"}}/>
                                <a href={data?.website} target="_blank" rel="noopener noreferrer"
                                   className="ml-3">{data?.website}</a>
                            </div>}
                        {data?.exchange !== null && data?.exchange !== "" &&
                            <div className="exchange d-flex align-center">
                                <img src={logoExchange} style={{width: "40px"}}/>
                                <div className="ml-3">{data?.exchange}</div>
                            </div>}

                    </div>
                    {data?.description !== null && data?.description !== "" && <div className="stockDescription w-70">
                        {data?.description}
                    </div>}
                </div>
            </div>
            {username ? <StocksChat stocks={ticker} username={username}/> : <></>}
        </div>
        <div className="h-5"></div>

    </div>)
}

export default Stock