import React, { useState } from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import { useTranslation } from 'react-i18next';
import market from "../../pages/Market/Market";


function StickyHeadTable({ data, columns, keyInter }) {
    const { t } = useTranslation();
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(50);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    return (
        <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            <TableContainer sx={{ maxHeight: 600 }}>
                <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                        <TableRow>
                            {columns.map((column) =>{
                                    return(
                                        <TableCell
                                            key={column.label}
                                            align={column.align || 'inherit'}
                                            style={{ minWidth: column.minWidth }}
                                        >
                                            {t(`${keyInter}.${column.label}`)}
                                        </TableCell>
                                        );

                            })}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, rowIndex) => (
                            <TableRow hover role="checkbox" tabIndex={-1} key={row.id || rowIndex}>
                                {columns.map((column) => {
                                    const value = row[column.label];
                                    return (
                                        <TableCell key={column.label} align={column.align}>
                                            {/*column.label === "ticker" ?
                                                <img style={{width:"35px"}} src={`https://financialmodelingprep.com/image-stock/${value}.png`} alt={"img"}/>
                                                :value */
                                            }
                                            {
                                            column.format && typeof value === 'number' ? column.format(value) : value }
                                        </TableCell>
                                    );
                                })}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[10, 50, 100]}
                component="div"
                count={data.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}

export default StickyHeadTable;
