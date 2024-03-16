import React, {useState, useRef, useEffect} from 'react';
import './TransactionWidget.scss';
import { useTranslation } from 'react-i18next';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import {RequestWallet} from "../../request/RequestWallet";


const TransactionWidget = ({ticker}) => {
    const { t } = useTranslation();
    const requestWallet = new RequestWallet();
    const [dataStock,setDataStock] = useState({})
    const [wallet,setWallet] = useState({})
    const [ownedQuantity,setOwnedQuantity] = useState(-1)
    const [quantity, setQuantity] = useState(0);
    const indicatorRef = useRef(null);
    const transactionTypes = {
        BUY: t('transactionWidget.buy'),
        SELL: t('transactionWidget.sell')
    };
    const [transactionType, setTransactionType] = useState(transactionTypes.BUY);


    async function fetchData () {
        const stockPerformance = await requestWallet.getStockPerformance(ticker);
        const getWallet = await requestWallet.getWallet();
        setWallet(getWallet.data)
        if(stockPerformance === 0 ){
            setOwnedQuantity(0)
            setTransactionType(transactionTypes.BUY)
        }else{
            setOwnedQuantity(-1)
            setDataStock(stockPerformance.data)
        }
        console.log(stockPerformance.data)
    }

    useEffect(() => {
        fetchData()
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
        if(transactionType === transactionTypes.BUY){
            await requestWallet.acheter(ticker, quantity);
        }else {
            await requestWallet.vendre(ticker, quantity);
        }

        window.location.reload()
    }


    return (
        <div className="w-100 containerTransactionWidget">
            <div className="headerTransactionWidget d-flex position-relative w-100">
                <div className="buyCell" onClick={() => handleClick(transactionTypes.BUY)}> {transactionTypes.BUY}</div>
                {
                   ownedQuantity !== 0 && <div className="sellCell" onClick={() => handleClick(transactionTypes.SELL)}> {transactionTypes.SELL}</div>
                }
                <div ref={indicatorRef} className="indicator"></div>
            </div>
            <div className={"mt-3"}>
                {transactionType === transactionTypes.BUY ?
                    <div>{`${wallet?.solde}$ disponibles.`}</div>
                :
                    ownedQuantity !== 0 &&
                    <div>{`${dataStock.quantity} action(s) possédée(s).`}</div>
                }
            </div>
            <div className="containerInput">
                <InputLabel onInputChange={setQuantity} label={t('transactionWidget.quantity')} type={"number"}/>
            </div>
            <div className="containerButton w-100 d-flex justify-center">
                <Button styles={"button black"} handleClick={transaction}  children={t('transactionWidget.reviewOrder')} />
            </div>
        </div>
    );
};

export default TransactionWidget;
