import React, {useEffect, useState} from 'react'
import './Market.scss'
import { useTranslation } from 'react-i18next';
import market from "../../modele/stocks-list.json"
import  "../../request/RequestMarket";
import MarketTable from "../../containers/Table/MarketTable";
import {findTickerByName, getStocksList} from "../../request/RequestMarket";
import {getFormatStocks} from "../../utils/services";
import routes from "../../utils/routes.json";
import {useNavigate} from "react-router-dom";
import InputResearch from "../../components/Input/InputResearch/InputResearch";


function Market() {
    const { t } = useTranslation();
    const COLUMNS = market.table

    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [page, setPage] = useState(0);
    const [fromResearchData, setFromResearchData] = useState(false)
    const [nameResearch,setName] = useState('')
    const navigate = useNavigate();

    async function fetchData() {
        try {
            setData([])
            var result = []
            if(fromResearchData){
                result = await findTickerByName(nameResearch,page+1)
            }else{
                result = await getStocksList(page + 1);
            }
            if (result.data) {
                setData(await getFormatStocks(result.data.content));
                setTotalCount(result.data.totalElements);
            }
        } catch (error) {
            console.error("Erreur lors de la récupération des données", error);
        }
    }


    useEffect(() => {
        fetchData();
    }, [page,nameResearch,fromResearchData]);



    const handleSubmit = async (research) => {
        setName(research)
        setPage(0)
        if(research.length ===0 ){
            setFromResearchData(false)
        }else {
            setFromResearchData(true)
        }
    }

    const handleClickTicker = (ticker) =>{
        navigate(`${routes.stock_nav}/${ticker}`)
    }


    return (
        <div className='containerPage'>
            <div className={"pageMarket"}>

                <div className={"headerMarket"}>
                    <h1>
                        {t('market.trends')}
                    </h1>
                </div>

                <MarketTable
                    columns={COLUMNS}
                    keyInter={"market.table"}
                    data={[]}

                />

                <div className={"headerMarket"}>
                    <h1>
                        {t('market.market')}
                    </h1>
                    <div>
                        <InputResearch label={t('market.research')} onSubmit={handleSubmit}/>
                    </div>
                </div>

                <MarketTable
                    height={100}
                    handleSubmit={handleSubmit}
                    handleClickTicker={handleClickTicker}
                    columns={COLUMNS}
                    keyInter={"market.table"}
                    data={data}
                    totalCount={totalCount}
                    page={page}
                    setPage={setPage}
                    rowsPerPage={market.rowsPerPage}
                />
            </div>


        </div>
    )
}

export default Market