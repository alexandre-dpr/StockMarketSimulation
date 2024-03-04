import React, {useEffect, useRef, useState} from 'react';
import "./Wallet.scss";
import Graph from "./Graph/Graph";
import logo from "../../../assets/img/logo.png";
import {RequestWallet} from "../../../request/RequestWallet";
import {Auth} from "../../../utils/Auth";
import routes from "../../../utils/routes.json";
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';



function Wallet() {
    const { t } = useTranslation();
    const [data,setData] = useState({
        "username": "vincent",
        "solde": 640.0,
        "mouvements": [
            {
                "mouvementId": 2,
                "time": "2024-02-26T15:32:10.910347",
                "type": "ACHAT",
                "ticker": "NVIDIA",
                "price": 90.0,
                "quantity": 4
            }
        ]
    });
    const requestWallet = new RequestWallet();
    const auth = new Auth();
    const navigate = useNavigate();
    const [isPercent, setIsPercent] = useState();


    useEffect(()=>{
        initWallet()
    }, [])

    async function initWallet(){
        //const resp = await requestWallet.getWallet(await auth.getUsername())
        //setData(resp.data);

        console.log(data)
    }


    function goTicker(ticker) {
        navigate(`${routes.stock_nav}/${ticker}`)
    }



    return (
        <div className="containerPage">
            <div className="cards">
                <div className="d-flex flex-column h-100 w-65 w-sm-100-p">
                        <h1>{t('wallet.wallet')}</h1>
                    <div className="d-flex align-center">
                        <h1>{data.solde} $ </h1>
                        <p className="green ml-r-1"> 142 $ (1,78 %)</p>
                    </div>
                    <Graph/>
                </div>
                <div className="d-flex flex-column h-100 w-35 w-sm-100-p">
                    <div className="h-50 d-flex flex-column">
                        <h3>{t('wallet.own')}{data.mouvements.length}{t('wallet.stocks')} :</h3>
                        <div className="d-flex flex-column w-100 overflow-scroll h-14-r">
                            {
                                data.mouvements.length > 0 ?<>
                            {
                                data.mouvements.map((item,index)=>(
                                    <div className="actions-items " id={item.ticker} >
                                        <div>
                                            <img src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`} style={{width: 30}}/>
                                        </div>
                                        <div className="content pointer" onClick={()=>{goTicker(item.ticker)}}>
                                                <p>{item.ticker}</p>
                                                <p>{item.price * item.quantity} $</p>
                                            </div>
                                        <div className="pointer" onClick={()=>{setIsPercent(!isPercent)}}>
                                            <p className="green">{isPercent ? "4 %" : item.price + " $"}</p>
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
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content pointer" onClick={()=>{goTicker("Test")}}>

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div className="pointer" onClick={()=>{setIsPercent(!isPercent)}}>
                                    <p className="green" >{isPercent ? "1,42 %" : "4 $"}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default Wallet;