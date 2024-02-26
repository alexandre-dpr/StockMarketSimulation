import React, {useEffect, useRef} from 'react';
import "./Wallet.scss";
import {Line} from "react-chartjs-2";
import logo from "../../../assets/img/logo.png";


function Wallet() {
    const chartRef = useRef(null);

        const chartData = {
            labels: ["1 Dec", "8 Dec", "16 Dec", "31 Dec"],
            datasets: [
                {

                    data: [3,2,7, 4],
                    borderColor: ["#03A9F5"],

                },
            ],legend: {
                display: false // Cela cache la légende
            },
            tooltips: {
                enabled: false
            }
        };

        const options = {
            legend: {
                display: false // Cela cache la légende
            },
            tooltips: {
                enabled: false
            },
            scales: {
                x: {
                    title: {
                        display: true,
                    },
                    grid: {
                        display: false
                    },
                },
                y: {
                    title: {
                        display: true,
                    },
                    grid: {
                        display: false
                    }
                },
            }
        };

    return (
        <div className="containerPage">
            <div className="cards d-flex">
                <div className="d-flex flex-column w-65">
                    <h1>Portefeuille de Damien Crespeau</h1>
                    <h1>10000,43 $</h1>
                    <p>+ 1,78%</p>
                    <Line id="graphique" data={chartData} options={options} />
                </div>
                <div className="d-flex flex-column  w-35">
                    <div className="h-50 d-flex flex-column">
                        <h3>Vous possédez 3 actions :</h3>
                        <div className="d-flex flex-column w-100 overflow-auto">
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                        <p>Apple</p>
                                        <p>432,5$</p>
                                    </div>
                                <div>
                                    <p>1,41 %</p>
                                </div>
                            </div>
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <p>1,41 %</p>
                                </div>
                            </div>
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <p>1,41 %</p>
                                </div>
                            </div>
                            
                        </div>
                    </div>
                    <div className="h-50 w-100">
                        <h3>Mes Favoris</h3>
                        <div className="d-flex flex-column w-100">
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <button id="favorite-button" className="favorite">
                                        <span id="star">★</span>
                                    </button>
                                </div>
                            </div>
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <button id="favorite-button" className="favorite">
                                        <span id="star">★</span>
                                    </button>
                                </div>
                            </div>
                            <div className="actions-items">
                                <div>
                                    <img src={logo} style={{width: 30}}/>
                                </div>
                                <div className="content">

                                    <p>Apple</p>
                                    <p>432,5$</p>
                                </div>
                                <div>
                                    <button id="favorite-button" className="favorite">
                                        <span id="star">★</span>
                                    </button>
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