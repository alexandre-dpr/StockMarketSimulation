import React, {useState} from 'react';
import "./InputResearch.scss"
import iconLoupe from "../../../assets/img/icons8-loupe-50.png"

const InputResearch = ({ onSubmit, label, id }) => {

    const [inputValue, setIV] = useState("")
    const handleSubmit = (event) => {
        event.preventDefault()
        onSubmit(inputValue)
    };

    const handleChange = (event) =>{
        setIV(event.target.value)
    }

    return (
        <div className={"input-research-container"}>
            <form onSubmit={handleSubmit}>
                <input value={inputValue}  type="text" id={"test"} placeholder="Rechercher un actif" onChange={handleChange}/>
            </form>
            <img className={"iconLoupe"} src={iconLoupe}  alt={""}/>
        </div>
    );
};

export default InputResearch;