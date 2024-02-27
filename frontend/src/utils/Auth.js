import CryptoJS from 'crypto-js';
import {jwtDecode} from "jwt-decode";


/**
 * @typedef {Object} Token
 * @property {string} sub - Subject
 * @property {number} exp - Expiration
 * @property {number} iat - Issued at
 * @property {string[]} roles - Roles
 */

export class Auth {
    async getEmail() {
        try {
            return localStorage.getItem("email");
        } catch (error) {
            console.error("Error while retrieving email:", error);
            return null;
        }
    }

    async setEmail(email) {
        try {
            localStorage.setItem("email", email);
        } catch (error) {
            console.error("Error while saving email:", error);
        }
    }


    async getJwtToken() {
        try {
            return  localStorage.getItem("jwt");
        } catch (error) {
            console.error("Error while retrieving JWT token:", error);
            return null;
        }
    }

    async setJwtToken(token) {
        try {
             localStorage.setItem("jwt", token);
        } catch (error) {
            console.error("Error while saving JWT token:", error);
        }
    }



     isLoggedIn() {
        return !!this.getJwtToken();
    }

    async decodeToken() {
        const token = await this.getJwtToken();
        if (token) {
            return jwtDecode(token);
        } else {
            return null;
        }
    }

    async getUsername() {
        if (this.isLoggedIn()) {
            console.log("oui")
            const decodedToken =await this.decodeToken();
            console.log(decodedToken)
            if (decodedToken) {
                return decodedToken.sub;
            }
        }
        return undefined;
    }
}