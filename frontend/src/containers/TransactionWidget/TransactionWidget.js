import React, {useState, useRef, useEffect} from 'react';
import './TransactionWidget.scss';
import {useTranslation} from 'react-i18next';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import {RequestWallet} from "../../request/RequestWallet";
import {Auth} from "../../utils/Auth";
import {isValidDecimal, isValidInteger, round} from "../../utils/services";
import routes from "../../utils/routes.json"
import {useNavigate} from "react-router-dom";
import Select from "../../components/Select/Select";
import InputInteger from "../../components/Input/InputInteger/InputInteger";
import InputDecimal from "../../components/Input/InputDecimal/InputDecimal";

const TransactionWidget = ({ticker, price}) => {
    const {t} = useTranslation();
    const navigate = useNavigate()
    const requestWallet = new RequestWallet();
    const [dataStock, setDataStock] = useState({})
    const [wallet, setWallet] = useState({})
    const [ownedQuantity, setOwnedQuantity] = useState(-1)
    const [quantity, setQuantity] = useState(0);
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
    const [isReadyReview, setReadyReview] = useState(false)

    async function fetchData() {
        const stockPerformance = await requestWallet.getStockPerformance(ticker);
        const getWallet = await requestWallet.getWallet();
        setWallet(getWallet.data)
        if (stockPerformance === 0) {
            setOwnedQuantity(0)
            setTransactionType(transactionTypes.BUY)
        } else {
            setOwnedQuantity(-1)
            setDataStock(stockPerformance.data)
        }
        console.log(stockPerformance.data)
    }

    useEffect(() => {
        if (auth.isLoggedIn()) {
            setIsAuth(true)
            fetchData()
        } else {
            setIsAuth(false)
        }

    }, []);

    useEffect(() => {
        if (orderType === orderTypes.ORDER_STOP) {
            setReadyReview(isValidInteger(quantity) && isValidDecimal(stopPrice))
        } else {
            setReadyReview(isValidInteger(quantity))
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

    async function transaction() {
        if (orderType === orderTypes.ORDER_MARKET) {
            if (transactionType === transactionTypes.BUY) {
                await requestWallet.acheter(ticker, quantity)
            } else {
                await requestWallet.vendre(ticker, quantity);
            }
            window.location.reload()
        }

    }

    const handleInputQttChange = (value) => {
        setQuantity(value)
        if (orderType === orderTypes.ORDER_MARKET) {
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

    return (
        <div className="w-100 containerTransactionWidget">
            {isAuth ?
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
                        <InputInteger onInputChange={handleInputQttChange} label={t('transactionWidget.quantity')}/>
                    </div>
                    {
                        orderType === orderTypes.ORDER_STOP &&

                        <div className="containerInput mt-10">
                            <InputDecimal onInputChange={handleInputStopPriceChange}
                                          label={t('transactionWidget.stopPrice')}
                            />
                        </div>
                    }

                    {orderType !== orderTypes.INVESTMENT_PLANNING &&
                        <div className="equivalentAmount d-flex">
                            <div className="flex-item-1"> {t('transactionWidget.amount')}</div>
                            <div className="flex-item-1 text-right"> {`${calcAmount}$`}</div>
                        </div>
                    }

                    <div className="mt-8 containerButton w-100 d-flex justify-center">
                        <Button styles={isReadyReview ? "button black" : "button notValid"}
                                handleClick={isReadyReview ? transaction : null}
                                children={t('transactionWidget.reviewOrder')}/>
                    </div>

                    {
                        ownedQuantity !== 0 &&
                        <div className={"containerPosition mt-30"}>
                            <h2>{t('transactionWidget.position')} </h2>
                            <div className="d-flex">
                                <div className="flex-item-1 Gabarito-Bold">{t('transactionWidget.total')}</div>
                                <div className="flex-item-1 Gabarito-Bold">{t('transactionWidget.performance')}</div>
                            </div>
                            <div className="d-flex">
                                <div className="flex-item-1 mt-4">{dataStock.buyPrice * dataStock.quantity}</div>
                                <div
                                    className="flex-item-1 mt-4">{dataStock.performance !== undefined && dataStock.performance.percentage}</div>
                            </div>
                            <div className="mt-4 Gabarito-Bold">{t('transactionWidget.quantity')}</div>
                            <div className="mt-4">{dataStock.quantity}</div>
                            <div className="mt-4 Gabarito-Bold">{t('transactionWidget.buyIn')}</div>
                            <div className="mt-4">{`${dataStock.buyPrice}$`}</div>
                        </div>
                    }

                </> :
                <div className="mt-30 logInSentence">
                    <h1 onClick={() => navigate(routes.auth)}>{t('transactionWidget.logInSentence')}</h1>
                </div>
            }

        </div>
    );
};

export default TransactionWidget;
