import React, {useEffect, useState} from 'react'
import './Stock.scss'
import {useParams} from 'react-router-dom';
import StocksChat from "../../containers/StocksChat/StocksChat";
import {Auth} from "../../utils/Auth";
import {getStock} from "../../request/RequestMarket";
import utils from "../../utils/utils.json"
import CustomImage from "../../components/CustomImage/CustomImage";
import {getStockLogo} from "../../utils/services";
import {useTranslation} from 'react-i18next';
import logoCeo from "../../assets/img/user.png"
import logoWeb from "../../assets/img/web.png"
import logoExchange from "../../assets/img/exchange.png"


function Stock() {
    const {t} = useTranslation();
    const {ticker} = useParams();
    const [username, setUsername] = useState(null);
    const [data, setData] = useState(null);
    const [range, setRange] = useState(utils.rangeHistoryStock.ONE_DAY);

    async function fetchData() {
        const result = await getStock(ticker, range);
        setData(result.data)
        console.log(result.data)
    }

    useEffect(() => {

        async function fetchUsername() {
            const auth = new Auth();
            const name = await auth.getUsername();
            await setUsername(name);
        }

        fetchUsername();
        fetchData()
    }, []);

    return (
        <div className='containerPage '>
            <div className="pageStock">
                <div className={"d-flex align-center"}>
                    <CustomImage src={getStockLogo(ticker)} alt={""} style={{width: "50px", height: "50px"}}/>
                    <div className="d-flex w-80 align-end">
                        <h1 className="ml-2">{data?.name} </h1>
                        <h1 className="ml-1 ticker"> {ticker}</h1>
                    </div>

                </div>
                <h1> {`${data?.price} ${data?.currency}`}</h1>

                {/*
                 ICI GRAPH A GAUCHE ET COMPOSANT ACHAT/VENTE A DROITE
                */}

                <div className="about">
                    <h1>{`${t('stock.about')} ${data?.name}`}</h1>
                    <div className="d-flex stockInfos">
                        <div className="infos w-30 d-flex justify-evenly flex-column">
                            <div className="ceo d-flex align-center">
                                <img src={logoCeo} style={{width: "40px"}}/>
                                <div className="ml-3">{data?.ceo}</div>

                            </div>
                            <div className="website d-flex  align-center">
                                <img src={logoWeb} style={{width: "40px"}}/>
                                <a href={data?.website} target="_blank" rel="noopener noreferrer"
                                   className="ml-3">{data?.website}</a>
                            </div>
                            <div className="exchange d-flex align-center">
                                <img src={logoExchange} style={{width: "40px"}}/>
                                <div className="ml-3">{data?.exchange}</div>
                            </div>


                        </div>
                        <div className="stockDescription w-70">
                            {data?.description}
                        </div>
                    </div>

                </div>

                {
                    username ?
                        <StocksChat stocks={ticker} username={username}/> : <></>
                }
            </div>

        </div>
    )
}

export default Stock