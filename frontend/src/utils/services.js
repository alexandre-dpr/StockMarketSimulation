import CustomImage from "../components/CustomImage/CustomImage";
import React from "react";


function getFormatStocks (data) {
    return (
        data.map(item => ({
            ...item,
            logo: <CustomImage
                style={{width: "35px"}}
                src={`https://financialmodelingprep.com/image-stock/${item.ticker}.png`}
                alt={"img"}
            />
        }))
    )
}

export {getFormatStocks}