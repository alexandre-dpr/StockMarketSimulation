import React from 'react'
import './NavTo.scss'
import { Link } from 'react-router-dom';

function NavTo({ className, onClick, path, state, children }) {
    return (
        <Link
            onClick={onClick}
            to={path}
            state={state}
            className={`custom-link ${className}`} >
            {children}
        </ Link>
    )
}

export default NavTo