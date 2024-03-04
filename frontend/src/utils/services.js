import CustomImage from "../components/CustomImage/CustomImage";
import React from "react";


async function getFormatStocks(data) {
    const stocksData = data.map(item => ({
        ...item,
        logoUrl: `https://financialmodelingprep.com/image-stock/${item.ticker}.png`
    }));

    return await Promise.all(stocksData.map(async item => {
        try {
            const response = await fetch(item.logoUrl);
            if (!response.ok) throw new Error('Failed to load');
            return {...item, logo: <CustomImage style={{width: "35px", height:"35px"}} src={item.logoUrl} alt={"img"}/>};
        } catch (error) {
            return {...item, logo: <CustomImage style={{width: "35px", height:"35px"}} src={""} alt={"img"}/>};
        }
    }))

}





export {getFormatStocks}