import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

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
}