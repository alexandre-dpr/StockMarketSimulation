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
    static async getEmail() {
        try {
            return localStorage.getItem("email");
        } catch (error) {
            console.error("Error while retrieving email:", error);
            return null;
        }
    }

    static async setEmail(email) {
        try {
            localStorage.setItem("email", email);
        } catch (error) {
            console.error("Error while saving email:", error);
        }
    }


    static async getJwtToken() {
        try {
            return  localStorage.getItem("jwt");
        } catch (error) {
            console.error("Error while retrieving JWT token:", error);
            return null;
        }
    }

    static async setJwtToken(token) {
        try {
             localStorage.setItem("jwt", token);
        } catch (error) {
            console.error("Error while saving JWT token:", error);
        }
    }



    static isLoggedIn() {
        return !!Auth.getJwtToken();
    }

    static async decodeToken() {
        const token = await Auth.getJwtToken();
        if (token) {
            return jwtDecode(token);
        } else {
            return null;
        }
    }

    static getUsername() {
        if (Auth.isLoggedIn()) {
            const decodedToken = Auth.decodeToken();
            if (decodedToken) {
                return decodedToken.sub;
            }
        }
        return undefined;
    }
}