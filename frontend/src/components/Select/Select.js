import React, { useState, useRef, useEffect } from 'react';
import './Select.scss'
import chevron from "../../assets/img/chevron.png"
const Select = ({ options, onSelect }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [selectedOption, setSelectedOption] = useState(options[0]);
    const dropdownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [dropdownRef]);

    const handleSelect = (option) => {
        setSelectedOption(option);
        setIsOpen(false);
        if (onSelect) {
            onSelect(option);
        }
    };

    return (
        <div className="custom-select" ref={dropdownRef}>
            <div className="select-trigger d-flex justify-between align-center" onClick={() => setIsOpen(!isOpen)}>
                <div>{selectedOption}</div>
                <img className={isOpen ? "chevron open" : "chevron"} src={chevron}/>
            </div>
            {isOpen && (
                <ul className="options">
                    {options.map((option, index) => (
                        <li key={index} onClick={() => handleSelect(option)}>
                            {option}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default Select;
