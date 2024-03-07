import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";
import endpoints from "../utils/endpoints.json";

const axios = Interceptor.getInstance();
const url_api = constants.url_api_bourse;


async function getStocksList(pageNumber) {

    const ENDPOINT = endpoints.stocksList;

    try {
        const response = await axios.get(`${url_api}${ENDPOINT}`, {
            params: {
                page: pageNumber,
            },
        });
        return { data: response.data };
    } catch (error) {
        console.error('Erreur lors de la requête :', error);
        return { error: error };
    }
}


async function findTickerByName(name,page) {

    const ENDPOINT = endpoints.tickerAutocomplete;

    try {
        const response = await axios.post(`${url_api}${ENDPOINT}`, null, {
            params: {
                name: name,
                page:page
            },
        });
        return { data: response.data };
    } catch (error) {
        console.error('Erreur lors de la requête :', error);
        return { error: error };
    }
}

async function getTrends(){
    const ENDPOINT = endpoints.trends;

    try {
        const response = await axios.get(`${url_api}${ENDPOINT}`);
        return { data: response.data };
    } catch (error) {
        console.error('Erreur lors de la requête :', error);
        return { error: error };
    }
}


async function getStock(ticker,range){
    const ENDPOINT = endpoints.stock;

    try {
        const response = await axios.get(`${url_api}${ENDPOINT}/${ticker}`, {
            params: {
                range: range
            },
        });
        return { data: response.data };
    } catch (error) {
        console.error('Erreur lors de la requête :', error);
        return { error: error };
    }
}



export { getStocksList, findTickerByName, getTrends, getStock };
