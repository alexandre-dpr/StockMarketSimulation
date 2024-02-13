import React, {useEffect, useState} from 'react'
import './Market.scss'
import { useTranslation } from 'react-i18next';
import market from "../../modele/stocks-list.json"
import  "../../request/RequestMarket";
import StickyHeadTable from "../../containers/Table/StickyHeadTable";
import {getStocksList} from "../../request/RequestMarket";


function Market() {
    const { t } = useTranslation();

    const COLUMNS = market.table

   const [nodes, setNodes] = useState([])

    useEffect(() => {
        (async () => {
            const result = await getStocksList(1); // Utilisez 1 ou tout autre numéro de page
            if (result.data) {
                setNodes(result.data.content)
            } else if (result.error) {
                console.error('Erreur lors de la récupération des données :', result.error);
            }
        })();
        }, []);

    const data = { nodes };

    return (
        <div className='containerPage'>
            <h1>
                {t('market.market')}
            </h1>
            <StickyHeadTable data={nodes} columns={COLUMNS} keyInter={"market.table"}/>
        </div>
    )
}

export default Market