import React, {useEffect, useState} from 'react';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import "./StockChat.scss";
import like from "../../assets/img/like.svg";
import bean from "../../assets/img/poubelle.png"
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
        setCommentaires(response.data);
    }

    async function addComment(){
        if (addCommentaire.length > 0){
            const resp = await requestCommunity.addComment(username,addCommentaire,stocks);
            setCommentaires([resp.data, ...commentaires]);
        }
    }

    async function likeAction(id){
        const resp = await requestCommunity.likeComment(id,username);
        const index = commentaires.findIndex(objet => objet.id === id);
        if (index !== -1) {
            const nouveauxObjets = [...commentaires];
            nouveauxObjets[index] = resp.data;
            setCommentaires(nouveauxObjets);
        }

    }

    async function deleteAction(id){
        await requestCommunity.deleteComment(id);
        setCommentaires(commentaires => commentaires.filter(objet => objet.id !== id));
    }

    return (
        <div className="w-100">
            <h1>{commentaires.length} commentaire(s)</h1>
            <div className="d-flex w-100 align-center justify-between">

            <InputLabel label="Ajoutez un commentaire" type="text" id="comment" value={addCommentaire} onInputChange={setAddCommentaire} />
            <Button children="Ajouter un commentaire" styles={"button black"} handleClick={()=> {addComment()}}/>
            </div>
            <div className="d-flex flex-column w-100">
                {
                    commentaires.length === 0 ?
                        <div className="d-flex w-100 justify-center">
                           <p>Il n'y a aucun commentaire</p>
                        </div>
                        :
                        <></>
                }
                {
                    commentaires.map((item,index)=>(
                        <div className="card d-flex flex-column w-100 box-border" >
                            <div className="d-flex w-100 justify-between">
                                <div className="d-flex">
                                    <p>{item.userId}</p>
                                    <p className="ml-r-1">
                                        {new Date(item.date).toLocaleDateString('fr-FR', {
                                            year: 'numeric',
                                            month: '2-digit',
                                            day: '2-digit'
                                        })}
                                    </p>
                                </div>
                                {
                                    username === item.userId ?
                                        <div className="like pointer" onClick={()=>{deleteAction(item.id)}}>
                                            <img style={{width: 20}} src={bean}/>
                                        </div>
                                        :
                                        <></>
                                }
                            </div>
                            <p>{item.content}</p>
                            <div className="d-flex align-center">
                                <div className="like pointer" onClick={()=>{likeAction(item.id)}}>
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