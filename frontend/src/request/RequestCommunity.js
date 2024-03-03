import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestCommunity {
    static COMMENT = "/comment";
    static ACTION = "/action";
    axios = Interceptor.getInstance();

    async getAllComment(tickers) {
        try {
            return await this.axios.get(constants.url_community + RequestCommunity.ACTION + "/"+tickers);
        } catch (error) {
            throw error;
        }
    }

    async addComment(username,comment,tickers) {
        try {
            return await this.axios.post(constants.url_community + RequestCommunity.COMMENT ,
                {
                    "userId": username,
                    "content": comment,
                    "action" : tickers
                });
        } catch (error) {
            throw error;
        }
    }


}