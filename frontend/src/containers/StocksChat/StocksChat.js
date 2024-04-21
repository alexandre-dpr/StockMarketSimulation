import React, {useEffect, useState} from 'react';
import InputLabel from "../../components/Input/InputLabel/InputLabel";
import "./StockChat.scss";
import like from "../../assets/img/like.svg";
import bean from "../../assets/img/poubelle.png";
import edit from "../../assets/img/edit.png"
import Button from "../../components/Buttons/Button/Button";
import {RequestCommunity} from "../../request/RequestCommunity";
import send from "../../assets/img/send-message.png";
import utils from "../../utils/utils.json";
import route from "../../utils/routes.json";

const StocksChat = ({stocks, username}) => {
    const [commentaires, setCommentaires] = useState([]);
    const [addCommentaire, setAddCommentaire] = useState("");
    const [isEdit, setIsEdit] = useState(null);
    const [editMessage, setEditMessage] = useState("");

    const requestCommunity = new RequestCommunity();

    useEffect(() => {
        getAllComment();
    }, [])

    async function getAllComment() {
        const response = await requestCommunity.getAllComment(stocks);
        setCommentaires(response.data);
    }

    async function addComment() {
        if (addCommentaire.length > 0) {
            const resp = await requestCommunity.addComment(addCommentaire, stocks);
            setCommentaires([resp.data, ...commentaires]);
        }
    }

    async function likeAction(id) {
        const resp = await requestCommunity.likeComment(id, utils.interaction.LIKE);
        const index = commentaires.findIndex(objet => objet.id === id);
        if (index !== -1) {
            const nouveauxObjets = [...commentaires];
            nouveauxObjets[index] = resp.data;
            setCommentaires(nouveauxObjets);
        }

    }

    async function deleteAction(id) {
        await requestCommunity.deleteComment(id);
        setCommentaires(commentaires => commentaires.filter(objet => objet.id !== id));
    }

    async function initEditMessage(id, content) {
        await setIsEdit(id);
        await setEditMessage(content);
    }

    async function validateEdit() {
        const resp = await requestCommunity.editComment(isEdit, editMessage);
        const index = commentaires.findIndex(objet => objet.id === isEdit);
        if (index !== -1) {
            const nouveauxObjets = [...commentaires];
            nouveauxObjets[index] = resp.data;
            setCommentaires(nouveauxObjets);
            await setIsEdit(null);
            await setEditMessage("");
        }
    }

    return (
        <div className="w-100">
            <h1>{commentaires.length} commentaire(s)</h1>
            <div className="d-flex w-100 align-center">
                <InputLabel label="Ajoutez un commentaire" type="text" id="comment" value={addCommentaire}
                            onInputChange={setAddCommentaire}/>
                <img className="ml-2" src={send} style={{width: "30px", cursor: "pointer"}} onClick={() => {
                    addComment()
                }}/>
            </div>
            <div className="d-flex flex-column w-100">
                <div className="card">
                    {
                        commentaires.map((item, index) => (
                            <div className=" d-flex flex-column w-100 box-border">
                                <div className="d-flex w-100 justify-between">
                                    <div className="d-flex">
                                        <p className={"Gabarito-Bold"}>{item.userId}</p>
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
                                            <div className="d-flex">
                                                <div className="like pointer mr-1-r" onClick={() => {
                                                    initEditMessage(item.id, item.content)
                                                }}>
                                                    <img style={{width: 20}} src={edit}/>
                                                </div>
                                                <div className="like pointer" onClick={() => {
                                                    deleteAction(item.id)
                                                }}>
                                                    <img style={{width: 20}} src={bean}/>
                                                </div>
                                            </div>
                                            :
                                            <></>
                                    }
                                </div>
                                {
                                    isEdit && isEdit === item.id ?
                                        <div className="d-flex align-center">
                                            <InputLabel placeholder={item.content} type="text" id={"edit-" + item.id}
                                                        onInputChange={setEditMessage}/>
                                            <Button children={"valider"} styles={"button black ml-1-r"}
                                                    handleClick={validateEdit}/>
                                        </div>
                                        :
                                        <p>{item.content}</p>
                                }
                                <div className="d-flex align-center">
                                    <div className={item.interaction ? "like pointer isLike" : "like pointer"}
                                         onClick={() => {
                                             likeAction(item.id)
                                         }}>
                                        <img style={{width: 20}} src={like}/>
                                    </div>
                                    <p className="ml-1-r">{item.nbInteraction}</p>
                                </div>
                            </div>
                        ))
                    }

                </div>

            </div>
        </div>
    );
};

export default StocksChat;