import React from 'react'
import './Button.scss'

function Button({ styles, children, handleClick }) {
    return (
        <div onClick={handleClick} className='containerButton'>
            <div className={styles}>{children}</div>
        </div>

    )
}

export default Button