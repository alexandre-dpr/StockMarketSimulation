import React, {useEffect, useState} from 'react'
import './Market.scss'
import { useTranslation } from 'react-i18next';
import market from "../../modele/stocks-list.json"
import  "../../request/RequestMarket";
import StickyHeadTable from "../../containers/Table/StickyHeadTable";
import {getStocksList} from "../../request/RequestMarket";
import CustomImage from "../../components/CustomImage/CustomImage";


function Market() {
    const { t } = useTranslation();
    const COLUMNS = market.table

    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(50);


    useEffect(() => {
        async function fetchData() {
            try {
                const result = await getStocksList(page + 1); // Assurez-vous que cela correspond à la pagination attendue
                if (result.data) {
                    const newData = result.data.content.map(item => ({
                        ...item,
                        logo: <CustomImage
                            style={{width: "35px"}}
                            src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`}
                            alt={"img"}
                        />
                    }));
                    setData(newData);
                    setTotalCount(result.data.totalElements); // Mettez à jour en fonction de la structure de votre réponse API
                }
            } catch (error) {
                console.error("Erreur lors de la récupération des données", error);
            }
        }

        fetchData();
    }, [page, rowsPerPage]);





    return (
        <div className='containerPage'>
            <h1>
                {t('market.market')}
            </h1>
            <StickyHeadTable
                columns={COLUMNS}
                keyInter={"market.table"}
                data={data}
                totalCount={totalCount}
                page={page}
                setPage={setPage}
                rowsPerPage={rowsPerPage}
                setRowsPerPage={setRowsPerPage}
            />
        </div>
    )
}

export default Market