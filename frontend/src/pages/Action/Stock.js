import React from 'react'
import './Stock.scss'
import { useParams } from 'react-router-dom';

function Stock() {
    const { ticker } = useParams();


    return (
        <div className='containerPage pageStock'>
            <div className='title'>{ticker}</div>

        </div>
    )
}

export default Stock