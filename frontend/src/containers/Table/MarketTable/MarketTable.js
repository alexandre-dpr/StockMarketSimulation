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

function MarketTable({ columns, keyInter, data, totalCount, page, setPage, rowsPerPage, handleClickTicker }) {
    const { t } = useTranslation();

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    function renderContent(column) {
        return column.label !== "logo" && t(`${keyInter}.${column.label}`);
    }

    return (
        <div className={"tableMarket"}>

            <Paper sx={{ width: '100%', overflow: 'hidden', boxShadow: 0 }}>
                    <TablePagination
                        rowsPerPageOptions={[]} // Enlever l'option pour changer le nombre de lignes par page
                        component="div"
                        count={totalCount}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                    />
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
                                        id={column.label}
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
                                                <TableCell
                                                    sx={{ padding: "10px" }}
                                                    className={column.label === "ticker" ? "tableCell ticker" : "tableCell"}
                                                    key={column.label} align={column.align}
                                                    id={column.label}>
                                                    {value}
                                                </TableCell>
                                        );
                                    })}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[]}
                        component="div"
                        count={totalCount}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                    />
            </Paper>
        </div>

    );
}

export default MarketTable;
