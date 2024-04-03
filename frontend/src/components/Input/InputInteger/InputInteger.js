import React, {useState} from 'react'
import './InputInteger.scss'

function InputInteger({ onInputChange, label, id }) {
    const [inputValue, setInputValue] = useState('');

    const handleChange = (event) => {
        let value = event.target.value;
        if ( value !== '') {
            const integerValue = parseInt(value, 10);
            if (!isNaN(integerValue)) {
                value = integerValue.toString();
            } else {
                value = inputValue;
            }
        }
        setInputValue(value);
        if (onInputChange) {
            onInputChange(value);
        }
    };

    return (
        <div className="input-container">
            <input
                type="text"
                id={id}
                placeholder=" "
                value={inputValue}
                onChange={handleChange}
            />
            <label htmlFor={id}>{label}</label>
        </div>
    );
}

export default InputInteger