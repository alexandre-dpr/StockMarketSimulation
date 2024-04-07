import React from 'react'
import './Button.scss'

function Button({ styles, children, handleClick,img }) {
    return (
        <div onClick={handleClick} className='containerButton'>
            <div className={styles}>
                {children}
                {
                    img && <img src={img}/>
                }
            </div>
        </div>

    )
}

export default Button