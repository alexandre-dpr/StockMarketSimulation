import React, {useEffect, useState} from 'react'
import './Market.scss'
import {useTranslation} from 'react-i18next';
import utils from "../../utils/utils.json"
import "../../request/RequestMarket";
import MarketTable from "../../containers/Table/MarketTable/MarketTable";
import {findTickerByName, getStocksList, getTrends} from "../../request/RequestMarket";
import {getFormatStocks} from "../../utils/services";
import routes from "../../utils/routes.json";
import {useNavigate} from "react-router-dom";
import InputResearch from "../../components/Input/InputResearch/InputResearch";
import TrendsTable from "../../containers/Table/TrendsTable/TrendsTable";
import Spinner from "../../components/Spinner/Spinner";
import constants from "../../utils/constants.json"


function Market() {
    const {t} = useTranslation();
    const market_cols = utils.market_table
    const labelsToExclude = ["variation", "name", "changeAmount", "currency", "volume", "changePercentage"];
    const trends_cols = utils.gainers.filter(gainer => !labelsToExclude.includes(gainer.label));
    const [gainers, setGainers] = useState([]);
    const [loosers, setLoosers] = useState([]);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [page, setPage] = useState(0);
    const [fromResearchData, setFromResearchData] = useState(false)
    const [nameResearch, setName] = useState('')
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true)

    async function fetchData() {
        setData([])
        var result = []
        if (fromResearchData) {
            result = await findTickerByName(nameResearch, page + 1)
        } else {
            result = await getStocksList(page + 1);
        }
        if (result.data) {
            setData(await getFormatStocks(result.data.content));
            setTotalCount(result.data.totalElements);
        }

        const gainers_result = await getTrends()
        setGainers(await getFormatStocks(gainers_result.data.gainersJson));
        const loosers_result = await getTrends()
        setLoosers(await getFormatStocks(loosers_result.data.loosersJson));
        setIsLoading(false)

    }

    useEffect(() => {
        fetchData();
    }, [page, nameResearch, fromResearchData]);


    const handleSubmit = async (research) => {
        setName(research)
        setPage(0)
        if (research.length === 0) {
            setFromResearchData(false)
        } else {
            setFromResearchData(true)
        }
    }

    const handleClickTicker = (ticker) => {
        navigate(`${routes.stock_nav}/${ticker}`)
    }


    return (
        <div className='containerPage'>
            <div className={"pageMarket"}>
                <div className={"trends"}>
                    <div className={"headerMarket"}>
                        <h1>
                            {t('market.trends')}
                        </h1>
                    </div>

                    <div className="d-flex containerTrends">
                        {
                            isLoading ?
                                <Spinner/>
                                :
                                <div className={"boxTrends"}>
                                    <TrendsTable
                                        colorPrice={constants.green}
                                        keyInter={"market.gainers_table"}
                                        columns={trends_cols}
                                        data={gainers}
                                        sign={utils.plus}
                                        handleClickTicker={handleClickTicker}
                                    />
                                </div>

                        }
                        {
                            isLoading ?
                                <Spinner/>
                                :
                                <div className={"boxTrends"}>
                                    <TrendsTable
                                        colorPrice={constants.red}
                                        keyInter={"market.gainers_table"}
                                        columns={trends_cols}
                                        data={loosers}
                                        sign={utils.minus}
                                        handleClickTicker={handleClickTicker}
                                    />
                                </div>
                        }


                    </div>
                </div>
                <div className={"market"}>
                    <div className={"headerMarket"}>
                        <h1>
                            {t('market.market')}
                        </h1>
                        <div className="ml-2">
                            <InputResearch label={t('market.research')} onSubmit={handleSubmit}/>
                        </div>
                    </div>
                    {
                        isLoading ?
                            <Spinner/>
                            :
                            <MarketTable
                                handleSubmit={handleSubmit}
                                handleClickTicker={handleClickTicker}
                                columns={market_cols}
                                keyInter={"market.table"}
                                data={data}
                                totalCount={totalCount}
                                page={page}
                                setPage={setPage}
                                rowsPerPage={utils.rowsPerPage}
                                pagination={true}
                            />
                    }

                </div>
            </div>
            <div className="h-5"></div>
        </div>
    )
}

export default Market