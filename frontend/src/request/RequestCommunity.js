import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestCommunity {
    static COMMENT = "/comment";
    axios = Interceptor.getInstance();

    async getAllComment(tickers) {
        try {
            return await this.axios.get(constants.url_community  + "/"+tickers);
        } catch (error) {
            throw error;
        }
    }

    async addComment(username,comment,tickers) {
        try {
            return await this.axios.post(constants.url_community + "/"+tickers ,
                {
                    "content": comment
                });
        } catch (error) {
            throw error;
        }
    }

    async likeComment(idComment,username){
        try {
            return await this.axios.post(constants.url_community + RequestCommunity.COMMENT + "/"+idComment);
        } catch (error) {
            throw error;
        }
    }

    async deleteComment(idComment){
        try {
            return await this.axios.delete(constants.url_community + RequestCommunity.COMMENT + "/"+idComment);
        } catch (error) {
            throw error;
        }
    }


}