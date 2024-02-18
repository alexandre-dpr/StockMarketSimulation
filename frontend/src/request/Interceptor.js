import axios from 'axios';
import { Auth } from "../utils/Auth";
import { RequestAuth } from "./RequestAuth";

class Interceptor {

    static instance;
    apiClient = axios.create();
    auth = new Auth();

    static getInstance() {
        if (this.instance) {
            return this.instance.apiClient
        }
        return new Interceptor().apiClient
    }

    constructor() {
        this.apiClient.interceptors.request.use(
            async (config) => {
                if (!config.url?.includes(RequestAuth.ENDPOINT)) {
                    const token = await this.auth.getJwtToken();
                    if (token) {
                        config.headers.Authorization = `Bearer ${token}`;
                    }
                }
                return config;
            },
            (error) => Promise.reject(error)
        );

    }
}

export default Interceptor;
