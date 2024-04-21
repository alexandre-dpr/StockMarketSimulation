import React, {useEffect, useRef, useState} from 'react';
import './TransactionWidget.scss';
import {useTranslation} from 'react-i18next';
import Button from "../../components/Buttons/Button/Button";
import {RequestWallet} from "../../request/RequestWallet";
import {Auth} from "../../utils/Auth";
import {isValidDecimal, isValidInteger, round} from "../../utils/services";
import routes from "../../utils/routes.json"
import {useNavigate} from "react-router-dom";
import Select from "../../components/Select/Select";
import InputInteger from "../../components/Input/InputInteger/InputInteger";
import InputDecimal from "../../components/Input/InputDecimal/InputDecimal";
import OrderValidation from "../OrderValidation/OrderValidation";
import {RequestAutomation} from "../../request/RequestAutomation";

const TransactionWidget = ({ticker, price}) => {
    const {t} = useTranslation();
    const navigate = useNavigate()
    const requestAutomation = new RequestAutomation();
    const requestWallet = new RequestWallet();
    const [dataStock, setDataStock] = useState({})
    const [wallet, setWallet] = useState({})
    const [ownedQuantity, setOwnedQuantity] = useState(-1)
    const [quantity, setQuantity] = useState(0);
    const [openModal, setOpenModal] = useState(false)
    const [summaryData, setSummaryData] = useState({})
    const indicatorRef = useRef(null);
    const transactionTypes = {
        BUY: t('transactionWidget.buy'),
        SELL: t('transactionWidget.sell')
    };
    const [transactionType, setTransactionType] = useState(transactionTypes.BUY);
    const [isAuth, setIsAuth] = useState(false)
    const auth = new Auth()
    const [calcAmount, setCalcAmount] = useState(0)
    const orderTypes = {
        ORDER_MARKET: t('transactionWidget.marketOrder'),
        ORDER_STOP: t('transactionWidget.stopOrder'),
        INVESTMENT_PLANNING: t('transactionWidget.investmentPlanning')

    }
    const [orderType, setOrderType] = useState(orderTypes.ORDER_MARKET)
    const [stopPrice, setStopPrice] = useState(0)
    const recurrences = {
        WEEKLY: t('transactionWidget.weekly'),
        MONTHLY: t('transactionWidget.monthly')
    }
    const [recurrence, setRecurrence] = useState(recurrences.WEEKLY);
    const typeOrders = {
        ABOVE: t('transactionWidget.above'),
        BELOW: t('transactionWidget.below')
    }
    const [typeOrder, setTypeOrder] = useState(typeOrders.ABOVE);
    const [isReadyReview, setReadyReview] = useState(false)
    const errors = {
        ERROR_BALANCE: t('transactionWidget.errors.insufficient_balance'),
        ERROR_TITLES: t('transactionWidget.errors.insufficient_titles'),
    }
    const [error, setError] = useState()
    const [automations, setAutomations] = useState([])

    async function fetchData() {
        const stockPerformance = await requestWallet.getStockPerformance(ticker);
        const getWallet = await requestWallet.getWallet();
        setWallet(getWallet.data)
        if (stockPerformance === 0) {
            setOwnedQuantity(0)
            setTransactionType(transactionTypes.BUY)
        } else {
            setOwnedQuantity(stockPerformance.data.quantity)
            setDataStock(stockPerformance.data)
        }
    }

    async function fetchAutomation(){
        const resp = await requestAutomation.getAutomation();
        if (resp.data){
            setAutomations(resp.data.automations);
        }
        console.log(resp.data)
    }

    useEffect(() => {
        if (auth.isLoggedIn()) {
            setIsAuth(true)
            fetchData()
            fetchAutomation()
        } else {
            setIsAuth(false)
        }

    }, []);

    useEffect(() => {
        if (transactionType === transactionTypes.BUY) {
            if (orderType === orderTypes.ORDER_STOP) {
                setReadyReview(isValidInteger(quantity) && isValidDecimal(stopPrice) && calcAmount <= wallet.solde)
            } else if (orderType === orderTypes.ORDER_MARKET) {
                setReadyReview(isValidInteger(quantity) && calcAmount <= wallet.solde)
            } else if (orderType === orderTypes.INVESTMENT_PLANNING) {
                setReadyReview(isValidInteger(quantity) && calcAmount <= wallet.solde)
            }
            if (calcAmount > wallet.solde) {
                setError(errors.ERROR_BALANCE)
            } else {
                setError("")
            }
        } else {
            if (orderType === orderTypes.ORDER_STOP) {
                setReadyReview(isValidInteger(quantity) && isValidDecimal(stopPrice) && ownedQuantity >= quantity)
            } else if (orderType === orderTypes.ORDER_MARKET) {
                setReadyReview(isValidInteger(quantity) && ownedQuantity >= quantity)
            } else if (orderType === orderTypes.INVESTMENT_PLANNING) {
                setReadyReview(isValidInteger(quantity) && ownedQuantity >= quantity)
            }
            if (ownedQuantity < quantity) {
                setError(errors.ERROR_TITLES)
            } else {
                setError("")
            }
        }

    }, [quantity, stopPrice])

    const handleClick = (type) => {
        setTransactionType(type);
        if (type === transactionTypes.BUY) {
            indicatorRef.current.style.transform = 'translateX(0%)';
        } else {
            indicatorRef.current.style.transform = 'translateX(100%)';
        }
    };

    const handleInputQttChange = (value) => {
        setQuantity(value)
        if (orderType === orderTypes.ORDER_MARKET || orderType === orderTypes.INVESTMENT_PLANNING) {
            setCalcAmount(round(value * price, 2))
        } else {
            setCalcAmount(round(value * stopPrice, 2))
        }
    }

    const handleInputStopPriceChange = (value) => {
        setStopPrice(value)
        setCalcAmount(round(quantity * value, 2))

    }
    const handleSelectChange = (value) => {
        setOrderType(value)
    }

    const handleRadioChange = (event) => {
        setRecurrence(event.target.value);
    };

    const handleSelectOrderChange = (event) => {
        setTypeOrder(event);
    };


    async function transaction() {
        if (orderType === orderTypes.ORDER_MARKET) {
            if (transactionType === transactionTypes.BUY) {
                await requestWallet.acheter(ticker, quantity)
            } else {
                await requestWallet.vendre(ticker, quantity);
            }
            window.location.reload()
        } else if (orderType === orderTypes.INVESTMENT_PLANNING) {
            await requestAutomation.dcaAutomation(ticker, parseFloat(quantity), recurrence === recurrences.WEEKLY ? "Weekly" : "Monthly", transactionType === transactionTypes.BUY ? "Buy" : "Sell");
            window.location.reload()

        } else if (orderType === orderTypes.ORDER_STOP) {
            await requestAutomation.pricethresholdAutomation(ticker, parseFloat(stopPrice), transactionType === transactionTypes.BUY ? "Buy" : "Sell", typeOrder === typeOrders.ABOVE ? "Above" : "Below", parseFloat(quantity));
            window.location.reload()

        }
    }


    function getSummary() {
        setOpenModal(true)
        let newSummaryData = {
            [t('transactionWidget.transactionType')]: transactionType,
            [t('transactionWidget.orderType')]: orderType,
            [t('transactionWidget.quantity')]: quantity
        }
        if (orderType === orderTypes.ORDER_MARKET) {
            newSummaryData[t('transactionWidget.amount')] = calcAmount + "$"
        } else if (orderType === orderTypes.ORDER_STOP) {
            newSummaryData[t('transactionWidget.stopOrder')] = stopPrice
            newSummaryData[t('transactionWidget.amount')] = calcAmount + "$"

        } else {
            newSummaryData[t('transactionWidget.recurrence')] = recurrence
        }
        setSummaryData(newSummaryData)
    }

    function resetValues() {
        setQuantity(0)
        setStopPrice(0)
        setCalcAmount(0)
        setOrderType(orderTypes.ORDER_MARKET)
    }

    function cancelTransaction() {
        setOpenModal(false)
        resetValues()
    }

    return (
        <div>
            <div className="w-100 containerTransactionWidget">
                {isAuth ?
                    openModal ? <OrderValidation data={summaryData} cancel={cancelTransaction}
                                                 transaction={transaction}/> :
                        <>
                            <div className="headerTransactionWidget d-flex position-relative w-100">
                                <div className="buyCell"
                                     onClick={() => handleClick(transactionTypes.BUY)}> {transactionTypes.BUY}</div>
                                {
                                    ownedQuantity !== 0 && <div className="sellCell"
                                                                onClick={() => handleClick(transactionTypes.SELL)}> {transactionTypes.SELL}</div>
                                }
                                <div ref={indicatorRef} className="indicator"></div>
                            </div>
                            <div className={"mt-3"}>
                                {transactionType === transactionTypes.BUY ?
                                    <div>{`${round(wallet.solde, 2)}$ ${t('transactionWidget.available')}.`}</div>
                                    :
                                    ownedQuantity !== 0 &&
                                    <div>{`${dataStock.quantity} ${t('transactionWidget.sharesOwned')}.`}</div>
                                }
                            </div>

                            <div className={"orderChoice mt-7"}>
                                <Select options={Object.values(orderTypes)} onSelect={handleSelectChange}/>
                            </div>

                            {
                                orderType === orderTypes.INVESTMENT_PLANNING &&
                                <form className="mt-8">
                                    <div>
                                        <label>
                                            <input
                                                type="radio"
                                                value={recurrences.WEEKLY}
                                                checked={recurrence === recurrences.WEEKLY}
                                                onChange={handleRadioChange}
                                            />
                                            {t('transactionWidget.weekly')}
                                        </label>
                                    </div>
                                    <div className="mt-3">
                                        <label>
                                            <input
                                                type="radio"
                                                value={recurrences.MONTHLY}
                                                checked={recurrence === recurrences.MONTHLY}
                                                onChange={handleRadioChange}
                                            />
                                            {t('transactionWidget.monthly')}
                                        </label>
                                    </div>

                                </form>
                            }

                            <div className="containerInput mt-10">
                                <InputInteger onInputChange={handleInputQttChange}
                                              label={t('transactionWidget.quantity')}/>
                            </div>
                            {
                                orderType === orderTypes.ORDER_STOP &&


                                <div className="containerInput mt-5">

                                    <Select options={Object.values(typeOrders)} onSelect={handleSelectOrderChange}/>

                                    <InputDecimal onInputChange={handleInputStopPriceChange}
                                                  label={t('transactionWidget.stopPrice')}
                                    />
                                </div>
                            }

                            {orderType !== orderTypes.INVESTMENT_PLANNING &&
                                <div className="equivalentAmount d-flex">
                                    <div className="flex-item-1"> {t('transactionWidget.amount')}:</div>
                                    <div className="flex-item-1 text-right"> {`${calcAmount}$`}</div>
                                </div>
                            }


                            <div className="mt-8 containerButton w-100 d-flex flex-column align-center justify-center">
                                <div className="error mt-2 mb-1">{error}</div>
                                <Button styles={isReadyReview ? "button black" : "button notValid"}
                                        handleClick={isReadyReview ? getSummary : null}
                                        children={t('transactionWidget.reviewOrder')}/>
                            </div>

                            {
                                orderType === orderTypes.ORDER_STOP &&
                                <div className="thresholdDef">
                                    {t('transactionWidget.thresholdDef')}
                                </div>
                            }

                            {
                                ownedQuantity !== 0 &&
                                <div className={"containerPosition mt-5"}>
                                    <h2>{t('transactionWidget.position')} </h2>
                                    <div className="d-flex">
                                        <div
                                            className="flex-item-1 Gabarito-Bold">{t('transactionWidget.total')}</div>
                                        <div
                                            className="flex-item-1 Gabarito-Bold">{t('transactionWidget.performance')}</div>
                                    </div>
                                    <div className="d-flex">
                                        <div
                                            className="flex-item-1 mt-4">{dataStock.buyPrice * dataStock.quantity}</div>
                                        <div
                                            className="flex-item-1 mt-4">{dataStock.performance !== undefined && dataStock.performance.percentage}</div>
                                    </div>
                                    <div className="mt-4 Gabarito-Bold">{t('transactionWidget.quantity')}</div>
                                    <div className="mt-4">{dataStock.quantity}</div>
                                    <div className="mt-4 Gabarito-Bold">{t('transactionWidget.buyIn')}</div>
                                    <div className="mt-4">{`${dataStock.buyPrice}$`}</div>
                                </div>
                            }

                            {
                                automations.length !== 0 &&
                                <div className="mt-10">
                                    <h2>{t('wallet.ordersPlaced')}</h2>
                                    {automations.map(item =>
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
                                        </div>
                                    )
                                    }
                                </div>
                            }

                        </> :
                    <div className="mt-30 logInSentence">
                        <h1 onClick={() => navigate(routes.auth)}>{t('transactionWidget.logInSentence')}</h1>
                    </div>
                }

            </div>
        </div>

    );
};

export default TransactionWidget;
