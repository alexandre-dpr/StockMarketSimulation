import CustomImage from "../components/CustomImage/CustomImage";
import React from "react";
import constants from "../utils/constants.json"
import cross from "../assets/img/cross.png"


const getStockLogo = (ticker) => {
    return `https://financialmodelingprep.com/image-stock/${ticker}.png`

}

async function getFormatStocks(data) {
    const stocksData = data.map(item => ({
        ...item,
        logoUrl: getStockLogo(item.ticker)
    }));

    return await Promise.all(stocksData.map(async item => {
        try {
            const response = await fetch(item.logoUrl);
            if (!response.ok) throw new Error('Failed to load');
            return {
                ...item,
                logo: <CustomImage style={{width: "35px", height: "35px"}} src={item.logoUrl} alt={"img"}/>
            };
        } catch (error) {
            return {...item, logo: <CustomImage style={{width: "35px", height: "35px"}} src={cross} alt={"img"}/>};
        }
    }))

}

function timestampToHHMM(timestamps) {
    return timestamps.map((timestamp) => {
        const date = new Date(timestamp * 1000);
        const hours = date.getHours();
        const minutes = date.getMinutes();
        const formattedHours = hours.toString().padStart(2, '0');
        const formattedMinutes = minutes.toString().padStart(2, '0');
        return `${formattedHours}:${formattedMinutes}`
    })


}

function timestampToDDMMYY(timestamps) {
    return timestamps.map((timestamp) => {
        const date = new Date(timestamp * 1000);
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear().toString().slice(-2);
        return `${day}/${month}/${year}`;
    });
}

function percentageDiff(val1, val2) {
    if (val1 === 0) {
        throw new Error("La première valeur ne doit pas être zéro pour calculer un pourcentage de différence.");
    }

    let difference = ((val2 - val1) / val1) * 100;
    let value = round(difference, 2) + '%';
    if (difference >= 0) {
        return [difference, <h3 style={{color: constants.green}}>+{value}</h3>]
    } else {
        return [difference, <h3 style={{color: constants.red}}>{value}</h3>]
    }

}

function round(value, toFixed) {
    if (value !== undefined) {
        return value.toFixed(toFixed);
    }
    return "0";
}
function formatCurrency(value) {
    if (value) {
        const units = ["", "k", "M", "B", "T"];
        let unitIndex = 0;

        while (value >= 1000 && unitIndex < units.length - 1) {
            value /= 1000;
            unitIndex++;
        }
        return `${value.toFixed(2)}${units[unitIndex]}`;
    }
    return 0
}

function isValidInteger(value) {
    const strValeur = String(value);
    const isValid = /^[0-9]+$/.test(strValeur);
    return isValid && parseInt(strValeur, 10) > 0;
}

function isValidDecimal(value) {
    const strValue = String(value);
    const isValid = /^[0-9]+(\.[0-9]+)?$/.test(strValue);
    return isValid && parseFloat(strValue) > 0;
}

export {
    getFormatStocks,
    getStockLogo,
    timestampToHHMM,
    timestampToDDMMYY,
    percentageDiff,
    round,
    formatCurrency,
    isValidInteger,
    isValidDecimal
}