import CustomImage from "../components/CustomImage/CustomImage";
import React from "react";

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
            return {...item, logo: <CustomImage style={{width: "35px", height: "35px"}} src={""} alt={"img"}/>};
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
    let value = round(difference,2) + '%';
    if(difference >= 0){
        return [difference,<h3 style={{color:"rgb(54, 162, 235)"}}>+{value}</h3>]
    }else{
        return [difference,<h3 style={{color:"rgb(255, 99, 132)"}}>{value}</h3>]
    }

}

function round(value,toFixed){
    if(value !== undefined){
        return parseFloat(value.toFixed(toFixed));
    }
    return 0
}



export {getFormatStocks, getStockLogo, timestampToHHMM, timestampToDDMMYY,percentageDiff,round}