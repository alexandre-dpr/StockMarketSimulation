import React, {useEffect, useState} from 'react'
import {Auth} from "../../utils/Auth";
import './Leaderboard.scss';
import {RequestWallet} from "../../request/RequestWallet";

function Leaderboard() {
    const [classement,setClassement] = useState(null)
    const requestWallet = new RequestWallet();


    useEffect(()=>{
        getClassement()
    },[])

    async function getClassement(){
        const resp = await requestWallet.getClassement();
        setClassement(resp.data)
    }
    return (
        <div className="containerPage">
            <h1>Classement général</h1>
            <div className="card">
                {
                    classement !==null && classement.leaderboard.slice(0,15).map((item,index)=>(
                        <>
                            <div className="d-flex justify-between" key={index}>
                                <div className="w-23-p box-border"><p>{item.rank}</p></div>
                                <div className="w-23-p box-border"><p>{item.username}</p></div>
                                <div className="w-23-p box-border"><p>{item.totalValue} $</p></div>
                                <div className="w-23-p box-border"><p>{item.percentage}</p></div>

                            </div>
                            {
                                index !== 14 ?
                                    <hr/>
                                    :
                                    classement.user.rank >15?
                                        <hr/>
                                        :

                                        <></>
                            }
                        </>

                    ))
                }
                {
                    classement !== null && classement.user.rank > 15?
                        <>
                            <div className="d-flex justify-center mt-1-r mb-1-r">
                                <p>.....</p>
                            </div>
                            <hr/>
                            <div className="d-flex justify-between">
                                <div className="w-23-p box-border"><p>{classement.user.rank}</p></div>
                                <div className="w-23-p box-border"><p>{classement.user.username}</p></div>
                                <div className="w-23-p box-border"><p>{classement.user.totalValue}</p></div>
                                <div className="w-23-p box-border"><p>{classement.user.percentage}</p></div>
                            </div>
                        </>
                        :
                        <></>
                }
            </div>
        </div>

    )
}

export default Leaderboard