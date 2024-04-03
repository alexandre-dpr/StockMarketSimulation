import React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';

const HistoriqueTable = ({data}) => {

    return (
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Ticker</TableCell>
                        <TableCell>Quantité</TableCell>
                        <TableCell>Prix</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Type</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.map((row) => (
                        <TableRow
                            key={row.ticker}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.ticker}
                            </TableCell>
                            <TableCell>{row.quantity}</TableCell>
                            <TableCell>{row.price}</TableCell>
                            <TableCell>{new Date(row.time).toLocaleDateString('fr-FR')}</TableCell>
                            <TableCell>{row.type}</TableCell>

                        </TableRow>
                    ))}

                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default HistoriqueTable;