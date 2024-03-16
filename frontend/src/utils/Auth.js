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
    getEmail() {
        try {
            return localStorage.getItem("email");
        } catch (error) {
            console.error("Error while retrieving email:", error);
            return null;
        }
    }

    setEmail(email) {
        try {
            localStorage.setItem("email", email);
        } catch (error) {
            console.error("Error while saving email:", error);
        }
    }


    getJwtToken() {
        try {
            return  localStorage.getItem("jwt");
        } catch (error) {
            console.error("Error while retrieving JWT token:", error);
            return null;
        }
    }

    setJwtToken(token) {
        try {
             localStorage.setItem("jwt", token);
        } catch (error) {
            console.error("Error while saving JWT token:", error);
        }
    }

     isLoggedIn() {
        return !!this.getJwtToken();
    }

    signOut() {
        try {
            localStorage.removeItem("jwt")
        } catch (error) {
            console.error("Error clearing JWT token:", error);
        }
    }

    decodeToken() {
        const token = this.getJwtToken();
        if (token) {
            return jwtDecode(token);
        } else {
            return null;
        }
    }

    getUsername() {
        if (this.isLoggedIn()) {
            const decodedToken = this.decodeToken();
            if (decodedToken) {
                return decodedToken.sub;
            }
        }
        return undefined;
    }


}