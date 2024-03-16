import React, {useState, useRef, useEffect} from 'react';
import './TransactionWidget.scss';
import {useTranslation} from 'react-i18next';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import {RequestWallet} from "../../request/RequestWallet";
import {Auth} from "../../utils/Auth";
import {round} from "../../utils/services";


const TransactionWidget = ({ticker, price}) => {
    const {t} = useTranslation();
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


    const handleClick = (type) => {
        setTransactionType(type);
        if (type === transactionTypes.BUY) {
            indicatorRef.current.style.transform = 'translateX(0%)';
        } else {
            indicatorRef.current.style.transform = 'translateX(100%)';
        }
    };

    async function transaction() {
        if (transactionType === transactionTypes.BUY) {
            await requestWallet.acheter(ticker, quantity);
        } else {
            await requestWallet.vendre(ticker, quantity);
        }
        window.location.reload()
    }

    const handleChange = (value) => {
        setQuantity(value)
        setCalcAmount(round(value * price, 2))
    }


    return (
        <div className="w-100 containerTransactionWidget">
            {isAuth &&
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
                    <div className="containerInput">
                        <InputLabel onInputChange={handleChange} label={t('transactionWidget.quantity')}
                                    type={"number"}/>
                    </div>

                    <div className="equivalentAmount d-flex">
                        <div className="flex-item-1"> {t('transactionWidget.amount')}</div>
                        <div className="flex-item-1 text-right"> {`${calcAmount}$`}</div>
                    </div>

                    <div className="mt-8 containerButton w-100 d-flex justify-center">
                        <Button styles={"button black"} handleClick={transaction}
                                children={t('transactionWidget.reviewOrder')}/>
                    </div>

                    <div className={"containerPosition mt-30"}>
                        <h2>{t('transactionWidget.position')} </h2>
                        <div className="d-flex">
                            <div className="flex-item-1 Gabarito-Bold">{t('transactionWidget.total')}</div>
                            <div className="flex-item-1 Gabarito-Bold">{t('transactionWidget.performance')}</div>
                        </div>
                        <div className="d-flex">
                            <div className="flex-item-1 mt-4">{dataStock.buyPrice * dataStock.quantity}</div>
                            <div className="flex-item-1 mt-4">{dataStock.performance !== undefined && dataStock.performance.percentage}</div>
                        </div>
                        <div className="mt-4 Gabarito-Bold">{t('transactionWidget.quantity')}</div>
                        <div className="mt-4">{dataStock.quantity}</div>
                        <div className="mt-4 Gabarito-Bold">{t('transactionWidget.buyIn')}</div>
                        <div className="mt-4">{`${dataStock.buyPrice}$`}</div>
                    </div>
                </>
            }

        </div>
    );
};

export default TransactionWidget;
