import React, { useState } from 'react';
import './InputDecimal.scss';

function InputDecimal({ onInputChange, label, id }) {
    const [inputValue, setInputValue] = useState('');

    function handleChange(event) {
        let value = event.target.value;
        const isValid = /^\.$|^\d*(\.\d*)?$/.test(value);
        if (isValid) {
            setInputValue(value);
        } else if (value === '') {
            setInputValue('');
        }
        if (isValid && value !== '.' && onInputChange) {
            onInputChange(value);
        }
    }

    return (
        <div className="input-container">
            <input
                type="text" // 'text' permet d'entrer des décimaux facilement
                id={id}
                placeholder=" "
                value={inputValue}
                onChange={handleChange}
            />
            <label htmlFor={id}>{label}</label>
        </div>
    );
}

export default InputDecimal;
