import React, {useEffect, useState} from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableRow from '@mui/material/TableRow';
import {useTranslation} from 'react-i18next';
import './TrendsTable.scss'
import utils from "../../../utils/utils.json"
function TrendsTable({sign, colorPrice, columns, keyInter, data, handleClickTicker}) {
    const {t} = useTranslation();

    const changeTypes = {
        percentage: "%",
        currency: "$"
    }

    const [changeType, setChangeType] = useState(changeTypes.percentage);

    const onChangeType = () =>{
        if(changeType === changeTypes.currency){
            setChangeType(changeTypes.percentage)
        }else {
            setChangeType(changeTypes.currency)
        }
    }

    function formattingValue(column, value, row) {
        const isCurrency = changeType === changeTypes.currency;
        const changeKey = isCurrency ? "changeAmount" : "changePercentage";
        const formattedChange = `${sign === utils.plus ? utils.plus : ""}${row[changeKey]}${isCurrency ? "$" : ""}`;

        const tableCellProps = {
            onClick: column.label === "change" ? () => onChangeType() : () => handleClickTicker(row["ticker"]),
            className: `tableCell ${column.label === "ticker" ? "ticker" : ""}`,
            sx: {
                border: "0",
                ...(column.label === "change" ? { color: colorPrice, fontFamily: "Gabarito-Bold" } : {})
            },
            key: column.label,
            align: column.align,
            id: column.label
        };

        if (column.label !== "change") {
            return (
                <TableCell {...tableCellProps}>
                    {column.label === "price" ? `${value}$` : value}
                </TableCell>
            );
        } else {
            return (
                <TableCell {...tableCellProps}>
                    {formattedChange}
                </TableCell>
            );
        }
    }



    return (
        <div className={"tableTrends"}>
            <Paper sx={{width: '100%', overflow: 'hidden', boxShadow: 2, borderRadius: "15px", paddingBlock: "13px"}}>
                <TableContainer className={"tableContainer"}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableBody className={"tableRow"}>
                            {data.map((row, rowIndex) => (
                                <TableRow hover role="checkbox"
                                          tabIndex={-1} key={row.id || rowIndex}>
                                    {columns.map((column) => {
                                        const value = row[column.label];
                                        return (
                                           formattingValue(column,value,row)
                                        );
                                    })}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>
        </div>

    );
}

export default TrendsTable;
