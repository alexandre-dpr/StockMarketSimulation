import React, {useEffect, useRef, useState} from 'react';
import "./Wallet.scss";
import Graph from "./Graph/Graph";
import logo from "../../../assets/img/logo.png";
import {RequestWallet} from "../../../request/RequestWallet";
import {Auth} from "../../../utils/Auth";
import routes from "../../../utils/routes.json";
import {useNavigate} from "react-router-dom";


function Wallet() {
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
            <div className="cards d-flex">
                <div className="d-flex flex-column h-100 w-65">
                        <h1>Portefeuille de {data.username}</h1>
                    <div className="d-flex align-center">
                        <h1>{data.solde} $ </h1>
                        <p className="green ml-r-1"> 142 $ (1,78 %)</p>
                    </div>
                    <Graph/>
                </div>
                <div className="d-flex flex-column h-100 w-35">
                    <div className="h-50 d-flex flex-column">
                        <h3>Vous possédez {data.mouvements.length} actions :</h3>
                        <div className="d-flex flex-column w-100 overflow-auto">
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
                                        <div>
                                            <p className="green">{item.price} $</p>
                                        </div>
                                    </div>
                                ))
                            }
                        </div>
                    </div>
                    <div className="h-50 w-100">
                        <h3>Mes Favoris</h3>
                        <div className="d-flex flex-column w-100">
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content cursor" onClick={()=>{goTicker("Apple")}}>

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <p className="green"> 1,42 %</p>
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