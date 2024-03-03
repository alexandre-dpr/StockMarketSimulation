import React, {useEffect, useState} from 'react';
import route from "../../utils/routes.json";
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import "./StockChat.scss";
import like from "../../assets/img/like.svg";
import Button from "../../components/Buttons/Button/Button";
import {RequestCommunity} from "../../request/RequestCommunity";

const StocksChat = ({stocks,username}) => {
    const [commentaires,setCommentaires] = useState([]);
    const [addCommentaire,setAddCommentaire] = useState("");

    const requestCommunity = new RequestCommunity();

    useEffect(()=>{
        getAllComment();
    },[])

    async function getAllComment(){
        const response = await requestCommunity.getAllComment(stocks);
        console.log(response.data)
        setCommentaires(response.data);
    }

    async function addComment(){
        if (addCommentaire.length > 0){
            await requestCommunity.addComment(username,addCommentaire,stocks);
            const response = await requestCommunity.getAllComment(stocks);

            await setCommentaires(response.data);
        }
    }

    return (
        <div className="w-100">
            <h1>{commentaires.length} commentaire(s)</h1>
            <div className="d-flex w-100 align-center justify-between">

            <InputLabel label="Ajoutez un commentaire" type="text" id="email" onInputChange={setAddCommentaire} />
            <Button children="Ajouter un commentaire" styles={"button black"} handleClick={()=> {addComment()}}/>
            </div>
            <div className="d-flex flex-column w-100">
                {
                    commentaires.map((item,index)=>(
                        <div className="card d-flex flex-column w-100 box-border">
                            <div className="d-flex w-100">
                                <p>{item.userId}</p>
                                <p className="ml-r-1">
                                    {new Date(item.date).toLocaleDateString('fr-FR', {
                                        year: 'numeric',
                                        month: '2-digit',
                                        day: '2-digit'
                                    })}
                                </p>
                            </div>
                            <p>{item.content}</p>
                            <div className="d-flex align-center">
                                <div className="like pointer">
                                    <img style={{width: 20}} src={like}/>
                                </div>
                                <p>{item.interaction}</p>
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
};

export default StocksChat;