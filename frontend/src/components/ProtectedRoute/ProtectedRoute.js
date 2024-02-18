import React, { useEffect, useState } from 'react';
import {Route, Navigate, Outlet} from 'react-router-dom';
import { Auth } from '../../utils/Auth';
import routes from '../../utils/routes.json';
import {jwtDecode} from "jwt-decode";


const AuthWrapper = () => {
    function isExpired(token) {
        console.log(token)
        if (token !== null) {
            const dcode = jwtDecode(token);
            const currentTime = Math.floor(Date.now() / 1000);
            return currentTime >= dcode.exp ;
        }
        return true;
    }

    return isExpired(localStorage.getItem('jwt'))
        ? <Navigate to="/" replace />
        : <Outlet />;
};

export default AuthWrapper;
