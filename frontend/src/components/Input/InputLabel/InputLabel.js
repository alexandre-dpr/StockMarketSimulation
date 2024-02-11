import React, {useState} from 'react'
import './InputLabel.scss'

function InputLabel({ onInputChange, label, id, type }) {
    const [inputValue, setInputValue] = useState('');

    const handleChange = (event) => {
        const value = event.target.value;
        setInputValue(value);
        if (onInputChange) {
            onInputChange(value);
        }
    };

    return (
        <div className="input-container">
            <input type={type} id={id} placeholder=" " value={inputValue} onChange={handleChange}/>
            <label htmlFor="email">{label}</label>
        </div>
    )
}

export default InputLabel