import constants from "../utils/constants.json";
import Interceptor from "./Interceptor";

export class RequestAuth {
    static ENDPOINT = "/auth"
    axios = Interceptor.getInstance();

    async login(login, password) {
        try {
            return await this.axios.post(constants.url_api + RequestAuth.ENDPOINT + "/login",
                {
                    "login": login,
                    "password": password
                });
        } catch (error) {
            throw error;
        }
    }

    async register(username, email, password) {
        try {
            return await this.axios.post(constants.url_api + RequestAuth.ENDPOINT + "/register",
                {
                    "email": email,
                    "username": username,
                    "password": password
                });

        } catch (error) {
            throw error;
        }
    }
}