import React, {useState} from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import {useTranslation} from 'react-i18next';
import TablePagination from '@mui/material/TablePagination';
import routes from "../../../utils/routes.json";
import {useNavigate} from "react-router-dom";

const HistoriqueTable = ({data}) => {
    const {t} = useTranslation();
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(20);
    const navigate = useNavigate();

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleClickTicker = (ticker) => {
        navigate(`${routes.stock_nav}/${ticker}`)
    }

    return (
        <div>
            {data &&
                <div>

                    <TableContainer component={Paper}>
                        <Table sx={{minWidth: 650}} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell className={"tableHeader"}>{t('wallet.table.ticker')}</TableCell>
                                    <TableCell className={"tableHeader"}>{t('wallet.table.quantity')}</TableCell>
                                    <TableCell className={"tableHeader"}>{t('wallet.table.price')}</TableCell>
                                    <TableCell className={"tableHeader"}>{t('wallet.table.date')}</TableCell>
                                    <TableCell className={"tableHeader"}>{t('wallet.table.type')}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => (
                                    <TableRow key={row.ticker} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                        <TableCell onClick={() => handleClickTicker(row["ticker"])} className={"ticker"}
                                                   component="th" scope="row">
                                            {row.ticker}
                                        </TableCell>
                                        <TableCell>{row.quantity}</TableCell>
                                        <TableCell>{row.price}</TableCell>
                                        <TableCell>{new Date(row.time).toLocaleDateString('fr-FR')}</TableCell>
                                        <TableCell>{row.type}</TableCell>
                                    </TableRow>
                                ))}
                                {data.length === 0 && (
                                    <TableRow>
                                        <TableCell className="alone-cell" colSpan={5}>
                                            Vous n'avez pas d'historique
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[10, 20, 50]}
                        component="div"
                        count={data.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                    />
                </div>
            }

        </div>
    );
};

export default HistoriqueTable;
