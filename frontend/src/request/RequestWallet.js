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
            console.error(error);
        }
    }

    async getHistorique() {
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.historique}`);
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

    async getClassement(username){
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.leaderboard}?username=${username}`);
        } catch (error) {
            throw error;
        }
    }

    async getStockPerformance(ticker){
        try {
            return await this.axios.get(`${constants.url_api_portefeuille}${endpoints.stock}/${ticker}`);
        } catch (error) {
            if(error.response.status === 404){
                return 0;
            }else{
                console.error(error);
            }
        }
    }

    async addFavori(ticker){
        try{
            return await this.axios.post(`${constants.url_api_portefeuille}${endpoints.favori}/${ticker}`);
        }catch (error){
            console.error(error)
        }
    }

    async delFavori(ticker){
        try{
            return await this.axios.delete(`${constants.url_api_portefeuille}${endpoints.favori}/${ticker}`);
        }catch (error){
            console.error(error)
        }
    }

}