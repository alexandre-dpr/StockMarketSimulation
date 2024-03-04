import React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import { useTranslation } from 'react-i18next';
import './MarketTable.scss'
import baissier from "../../../assets/img/baissier.png"
import haussier from "../../../assets/img/haussier.png"

function MarketTable({ sign, colorPrice, isTrends, pagination, columns, keyInter, data, totalCount, page, setPage, rowsPerPage, handleClickTicker }) {
    const { t } = useTranslation();

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    function renderContent(column) {
        // Affichage basé sur la valeur de "sign"
        if (column.label === "logo") {
            if (sign === "+") {
                return <img style={{width:"30px"}} src={haussier} alt="" />;
            } else if (sign === "-") {
                return <img style={{width:"30px"}} src={baissier} alt="" />;
            }else{
                return ""
            }
        }
        // Retourner une valeur par défaut si aucun des cas ci-dessus n'est satisfait
        return t(`${keyInter}.${column.label}`);
    }

    return (
        <div className={"tableMarket"}>

            <Paper sx={{ width: '100%', overflow: 'hidden', boxShadow: 2, borderRadius: "15px" } : { width: '100%', overflow: 'hidden', boxShadow: 0 }}>
                <TableContainer className={"tableContainer"}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead >
                            <TableRow>

                                {columns.map((column) => (
                                    <TableCell
                                        key={column.label}
                                        align={column.align || 'inherit'}
                                        style={{ minWidth: column.minWidth }}
                                        className={"tableHeader tableCell"}
                                    >
                                        {renderContent(column)}
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody className={"tableRow"}>
                            {data.map((row, rowIndex) => (
                                <TableRow onClick={() => handleClickTicker(row["ticker"])} hover role="checkbox" tabIndex={-1} key={row.id || rowIndex}>
                                    {columns.map((column) => {
                                        const value = row[column.label];
                                        return (

                                            isTrends ?
                                                <TableCell sx={column.label === "price" ? { border: "0", color: colorPrice, fontFamily: "Gabarito-Bold" } : { border: "0", padding: "7.5px" }} className={column.label === "ticker" ? "tableCell ticker" : "tableCell"} key={column.label} align={column.align}>
                                                    {column.label === "price" ? `${sign}${value}` : value}
                                                </TableCell>
                                                :
                                                <TableCell sx={{ padding: "10px" }} className={column.label === "ticker" ? "tableCell ticker" : "tableCell"} key={column.label} align={column.align}>
                                                    {value}
                                                </TableCell>

                                        );
                                    })}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                {pagination &&
                    <TablePagination
                        rowsPerPageOptions={[]} // Enlever l'option pour changer le nombre de lignes par page
                        component="div"
                        count={totalCount}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                    />
                }

            </Paper>
        </div>

    );
}

export default MarketTable;
