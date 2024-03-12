import React, { useState, useRef } from 'react';
import './TransactionWidget.scss';
import { useTranslation } from 'react-i18next';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import Button from "../../components/Buttons/Button/Button";
import {getStock} from "../../request/RequestMarket";
import {timestampToDDMMYY, timestampToHHMM} from "../../utils/services";
import {RequestWallet} from "../../request/RequestWallet";
import {Auth} from "../../utils/Auth";


const TransactionWidget = ({ticker}) => {
    const { t } = useTranslation();
    const requestWallet = new RequestWallet();
    const auth = new Auth();

    const transactionTypes = {
        BUY: "buy",
        SELL: "sell"
    };

    const [transactionType, setTransactionType] = useState(transactionTypes.BUY);
    const [quantity, setQuantity] = useState(0);

    const indicatorRef = useRef(null);


    const handleClick = (type) => {
        setTransactionType(type);
        if (type === transactionTypes.BUY) {
            indicatorRef.current.style.transform = 'translateX(0%)';
        } else {
            indicatorRef.current.style.transform = 'translateX(110%)';
        }
    };


    async function transaction() {
        if(transactionType === transactionTypes.BUY){
            await requestWallet.acheter(ticker, quantity,auth.getUsername());
        }else {
            await requestWallet.vendre(ticker, quantity,auth.getUsername());
        }
    }

    return (
        <div className="w-100 containerTransactionWidget">
            <div className="headerTransactionWidget d-flex position-relative w-80">
                <div className="buyCell" onClick={() => handleClick(transactionTypes.BUY)}> {t('transactionWidget.buy')}</div>
                <div className="sellCell" onClick={() => handleClick(transactionTypes.SELL)}> {t('transactionWidget.sell')}</div>
                <div ref={indicatorRef} className="indicator"></div>
            </div>
            <div className="containerInput">
                <InputLabel onInputChange={setQuantity} label={"Montant"} type={"text"}/>
            </div>
            <div className="containerButton">
                <Button styles={"button black"} handleClick={transaction}  children={transactionType} />
            </div>
        </div>
    );
};

export default TransactionWidget;
