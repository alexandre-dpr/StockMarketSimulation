import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";
import endpoints from "../utils/endpoints.json";

async function getStocksList(pageNumber) {
    const axios = Interceptor.getInstance();
    const url_api = constants.url_api_bourse;
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

export { getStocksList };
