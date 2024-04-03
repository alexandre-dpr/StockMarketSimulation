import React, {useEffect, useState} from 'react'
import {Auth} from "../../utils/Auth";
import './Leaderboard.scss';
import {RequestWallet} from "../../request/RequestWallet";
import LeaderboardTable from "../../containers/Table/LeaderboardTable/LeaderboardTable";
import Spinner from "../../components/Spinner/Spinner";

function Leaderboard() {
    const [classement,setClassement] = useState(null)
    const requestWallet = new RequestWallet();
    const [isLoading,setIsLoading] = useState(false);



    useEffect(()=>{
        getClassement()
    },[])

    async function getClassement(){
        const resp = await requestWallet.getClassement();
        await setClassement(resp.data)
        setIsLoading(true)
    }
    return (
        <div className="containerPage">
            <div className="card">
                <h1>Classement général</h1>
                {
                    isLoading ?
                        <LeaderboardTable data={classement}/>
                        :
                        <div className="mt-10"><Spinner/></div>
                }
            </div>
        </div>

    )
}

export default Leaderboard