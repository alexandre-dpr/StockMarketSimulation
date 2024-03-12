import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";
import endpoints from "../utils/endpoints.json";

export class RequestWallet {
    axios = Interceptor.getInstance();

    async createWallet(username) {
        try {
            return await this.axios.post(constants.url_api_portefeuille,
                {
                    "username": username
                });
        } catch (error) {
            throw error;
        }
    }

    async getWallet(username) {
        try {
            return await this.axios.get(constants.url_api_portefeuille, {
                body: {
                    username: username
                }
            });
        } catch (error) {
            throw error;
        }
    }

    async acheter(ticker, quantity,username) {
        try {
            return await this.axios.post(`${constants.url_api_portefeuille}${endpoints.achat}`, {
                body: {
                    username: username,
                    ticker: ticker,
                    quantity: quantity
                }
            });
        } catch (error) {
            throw error;
        }
    }

    async vendre(ticker, quantity,username) {
        try {
            return await this.axios.post(`${constants.url_api_portefeuille}${endpoints.vente}`, {
                body: {
                    username: username,
                    ticker: ticker,
                    quantity: quantity
                }
            });
        } catch (error) {
            throw error;
        }
    }
}