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

    async getWallet() {
        try {
            return await this.axios.get(constants.url_api_portefeuille);
        } catch (error) {
            throw error;
        }
    }

    async getFav() {
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.favori}`);
        } catch (error) {
            throw error;
        }
    }

    async acheter(ticker, quantity) {
        const qtt = parseInt(quantity)
        try {
            return await this.axios.post(`${constants.url_api_portefeuille}${endpoints.achat}`, {
                ticker: ticker,
                quantity: qtt
            });
        } catch (error) {
            throw error;
        }
    }

    async vendre(ticker, quantity) {
        const qtt = parseInt(quantity)
        try {
            return await this.axios.post(`${constants.url_api_portefeuille}${endpoints.vente}`, {
                ticker: ticker,
                quantity: qtt
            });
        } catch (error) {
            throw error;
        }
    }

    async getClassement(){
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.leaderboard}`);
        } catch (error) {
            throw error;
        }
    }

    async getStockPerformance(ticker){
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.stock}/${ticker}`);
        } catch (error) {
            return 0;
        }
    }



}