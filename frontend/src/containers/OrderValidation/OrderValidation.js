import "./OrderValidation.scss"

import React from 'react';
import Button from "../../components/Buttons/Button/Button";
import {useTranslation} from "react-i18next";


const OrderValidation = ({transaction, cancel, data}) => {

    const {t} = useTranslation();

    return (
        <div className="">
            <h1> {t('orderValidation.summary')} </h1>
            {Object.keys(data).map(
                (item) =>
                    <div className="d-flex w-100 mt-3">
                        <div className="w-50 Gabarito-Bold"> {`${item}: `}</div>
                        <div className="w-50"> {data[item]}</div>
                    </div>
            )}

            <div className="d-flex flex-column align-center w-100 mt-8">
                <Button handleClick={transaction} styles="button black" children={t('confirm')}/>
                <Button handleClick={cancel} styles="button mt-8" children={t('cancel')}/>
            </div>

        </div>
    );
};

export default OrderValidation;
