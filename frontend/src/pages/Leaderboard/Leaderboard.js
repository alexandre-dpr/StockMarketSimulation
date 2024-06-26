import React, {useEffect, useState} from 'react'
import {Auth} from "../../utils/Auth";
import './Leaderboard.scss';
import {RequestWallet} from "../../request/RequestWallet";
import LeaderboardTable from "../../containers/Table/LeaderboardTable/LeaderboardTable";
import Spinner from "../../components/Spinner/Spinner";
import podium from "../../assets/img/podium2.png"


function Leaderboard() {
    const [classement,setClassement] = useState(null)
    const requestWallet = new RequestWallet();
    const [isLoading,setIsLoading] = useState(false);
    const auth = new Auth();

    useEffect( ()=>{
        getClassement()
    },[])

    async function getClassement(){
        const username = await auth.getUsername();
        const resp = await requestWallet.getClassement(username);
        await setClassement(resp.data)
        setIsLoading(true)
    }
    return (
        <div className="containerPage">
            <div className="card">
                <h1>Classement général</h1>
                {
                    isLoading ?
                        <>
                            <div className="d-flex justify-center relative d-sm-none">
                                {classement.leaderboard[0] &&
                                    <div className="podium n1">
                                        <p>{classement.leaderboard[0] && classement.leaderboard[0].username}</p>
                                        <p>{classement.leaderboard[0] && classement.leaderboard[0].percentage}</p>
                                    </div>
                                }
                                {classement.leaderboard[1] &&
                                    <div className="podium n2">
                                        <p>{classement.leaderboard[1].username}</p>
                                        <p>{classement.leaderboard[1].percentage}</p></div>
                                }
                                {classement.leaderboard[2] &&
                                    <div className="podium n3">
                                        <p>{classement.leaderboard[2].username}</p>
                                        <p>{classement.leaderboard[2].percentage}</p>
                                    </div>
                                }
                                <img src={podium} className="w-60-p"/>

                            </div>
                            {
                                classement.leaderboard.length>0 && <LeaderboardTable data={classement}/>
                            }
                        </>
                        :
                        <div className="mt-10"><Spinner/></div>
                }
            </div>
        </div>

    )
}

export default Leaderboard