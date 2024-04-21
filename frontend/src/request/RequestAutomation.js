import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestAutomation {
    static COMMENT = "/comment";
    static INTERACTION = "/interaction";

    axios = Interceptor.getInstance();

    async getAutomation() {
        try {
            return await this.axios.get(constants.url_api_automation);
        } catch (error) {
            console.error(error);
        }
    }

    async deleteAutomation(idAutomation) {
        try {
            return await this.axios.delete(constants.url_api_automation + "?idAutomation=" +idAutomation);
        } catch (error) {
            console.error(error);
        }
    }

    async dcaAutomation(symbole, quantite,frequence,transactionType){
        try {
            return await this.axios.post(constants.url_api_automation + "/dca",{
                "symbole":symbole,
                "quantite":quantite,
                "frequence": frequence,
                "transactionType": transactionType
            });
        } catch (error) {
            throw error;
        }
    }

    async pricethresholdAutomation(ticker,thresholdPrice,transactionType,thresholdType,quantity){
        try {
            return await this.axios.post(constants.url_api_automation + "/pricethreshold",
                {
                    "ticker": ticker,
                    "thresholdPrice" : thresholdPrice,
                    "transactionType" : transactionType,
                    "thresholdType" : thresholdType,
                    "quantity" : quantity
                });
        } catch (error) {
            throw error;
        }
    }


}