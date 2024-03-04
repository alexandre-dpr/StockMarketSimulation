import React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import {useTranslation} from 'react-i18next';
import '../Table.scss'
import baissier from "../../../assets/img/baissier.png"
import haussier from "../../../assets/img/haussier.png"

function TrendsTable({sign, colorPrice, columns, keyInter, data, handleClickTicker}) {
    const {t} = useTranslation();

    function renderContent(column) {
        if (column.label === "logo") {
            if (sign === "+") {
                return <img style={{width: "30px"}} src={haussier} alt=""/>;
            } else if (sign === "-") {
                return <img style={{width: "30px"}} src={baissier} alt=""/>;
            } else {
                return ""
            }
        }
        return t(`${keyInter}.${column.label}`);
    }

    return (
        <div className={"tableMarket"}>
            <Paper sx={{width: '100%', overflow: 'hidden', boxShadow: 2, borderRadius: "15px"}}>
                <TableContainer className={"tableContainer"}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                {columns.map((column) => (
                                    <TableCell
                                        id={column.label}
                                        key={column.label}
                                        align={column.align || 'inherit'}
                                        style={{minWidth: column.minWidth}}
                                        className={"tableHeader tableCell"}
                                    >
                                        {renderContent(column)}
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody className={"tableRow"}>
                            {data.map((row, rowIndex) => (
                                <TableRow onClick={() => handleClickTicker(row["ticker"])} hover role="checkbox"
                                          tabIndex={-1} key={row.id || rowIndex}>
                                    {columns.map((column) => {
                                        const value = row[column.label];
                                        return (
                                            <TableCell
                                                sx={column.label === "price" ? {
                                                    border: "0",
                                                    color: colorPrice,
                                                    fontFamily: "Gabarito-Bold"
                                                } : {border: "0", padding: "7.5px"}}
                                                className={column.label === "ticker" ? "tableCell ticker" : "tableCell"}
                                                key={column.label} align={column.align}
                                                id={column.label}
                                            >
                                                {column.label === "price" ? `${sign}${value}$` : value}
                                            </TableCell>
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
