import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestCommunity {
    static COMMENT = "/comment";
    static INTERACTION = "/interaction";

    axios = Interceptor.getInstance();

    async getAllComment(tickers) {
        try {
            return await this.axios.get(constants.url_community  + "/"+tickers);
        } catch (error) {
            throw error;
        }
    }

    async addComment(comment,tickers) {
        try {
            return await this.axios.post(constants.url_community + "/" +RequestCommunity.COMMENT ,
                {
                    "content": comment,
                    "tickers": tickers
                });
        } catch (error) {
            throw error;
        }
    }

    async likeComment(idComment, interaction){
        try {
            return await this.axios.post(constants.url_community + RequestCommunity.INTERACTION,{
                "idComment":idComment,
                "interaction":interaction
            });
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

    async editComment(idComment,content){
        try {
            return await this.axios.patch(constants.url_community + RequestCommunity.COMMENT + "/"+idComment,{
                "content": content
            });
        } catch (error) {
            throw error;
        }
    }


}