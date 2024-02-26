import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestWallet {
    axios = Interceptor.getInstance();

    async createWallet(username) {
        try {
            return await this.axios.post(constants.url_api_portefeuille,
                {
                    "username": "alexandre"
                });
        } catch (error) {
            throw error;
        }
    }
}